package com.example.inception.fragment

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.BADGE_ICON_SMALL
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.example.inception.CustomLayoutManager.SpeedLinearLayoutManager
import com.example.inception.GetUserByUsernameQuery
import com.example.inception.R
import com.example.inception.activity.CreateCommodity
import com.example.inception.activity.DetailPage
import com.example.inception.adaptor.CommodityRecycleViewAdapter
import com.example.inception.adaptor.DistributorWantedItemsRecycleViewAdaptor
import com.example.inception.adaptor.RecentlyAddedImageAdapter
import com.example.inception.api.apolloClient
import com.example.inception.constant.*
import com.example.inception.data.Commodity
import com.example.inception.data.CommodityUser
import com.example.inception.data.MediaLoaderData
import com.example.inception.data.UploadParams
import com.example.inception.internalreceiver.EXTRA_HIDE
import com.example.inception.internalreceiver.SMSReceiver
import com.example.inception.objectClass.User
import com.example.inception.service.UploadImageIntentService
import com.example.inception.utils.Capitalizer
import com.example.inception.utils.EspressoIdlingResource
import com.example.inception.utils.ImageZoomer
import com.squareup.picasso.Picasso
import com.vincent.filepicker.Constant
import com.vincent.filepicker.activity.ImagePickActivity
import com.vincent.filepicker.filter.entity.ImageFile
import kotlinx.android.synthetic.main.fragment_commodity_detail.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.fragment_profile.view.container
import kotlinx.android.synthetic.main.fragment_profile.view.expanded_image
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pub.devrel.easypermissions.EasyPermissions



//kami akan mengimplementasikan Loader(CursorLoader ke dalam Fragment
//untuk mengambil data gambar yang baru baru ini diambil user dengan tujuan incase jika user telah mengambil gambar komoditas tetapi lupa mengupload data
// ini tentunya akan membantu user dalam mengingatkan nya kembali
class ProfileFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {
    val cap = Capitalizer()

    var uploadedURL : String? = ""
    val itemList : MutableList<Commodity> = arrayListOf()
    var adapter : CommodityRecycleViewAdapter? = null
    var LookingForAdapter : DistributorWantedItemsRecycleViewAdaptor? = null

    //Gallery Cursor Loader
    //definisikan loader ID
    private val IMAGE_LOADER_ID = 1325
    //buat array kosong yang akan di supply ke Adapter
    private val GalleryImages = ArrayList<MediaLoaderData>()
    //persiapkan projectionField , kolom yang ingin kita ambil datanya dari Content Provider(dalam hal ini = Gallery(MediaStore))
    var projectionField = arrayOf(
        MediaStore.Images.Media._ID
    )
    //karna kami akan mengambil semuanya, maka kami tidak akan memberikan selection Criteria
    val selectionCriteria : String? = null
    //definisikan URI tujuan (URI dari content Provider)
    var queryUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI


    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: NotificationCompat.Builder

    private val downloadReceiver = object : BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            if(p1?.getStringExtra(UPLOAD_CONTEXT) == "profile_pic") {
                uploadedURL = p1?.getStringExtra(UPLOADED_FILE_URL)
                Picasso.get().load(uploadedURL).into(requireView().profile_avatar)

                requireView().profile_avatar.setOnClickListener {
                    zoomer.zoomImageFromThumb(requireContext(),requireView().profile_avatar,uploadedURL!!,requireView().container,requireView().expanded_image,null)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if(User.getSubscription(requireActivity()) == 0) {
            point.text = "Points : " + User.getPoint(requireActivity()).toString()
        }else {
            point.text = "Subscribed"
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LoaderManager.getInstance(this).initLoader(IMAGE_LOADER_ID,null,this)
    }

    val zoomer = ImageZoomer()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        zoomer.shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)

        //inisiasi service untuk notifikasi
        notificationManager = activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        var objectView = inflater.inflate(R.layout.fragment_profile, container, false)

        //ketika logout kita menghapus token dari shared preference
        objectView.logout.setOnClickListener {
            User.removeToken(requireContext())
            requireActivity().finish()
        }

        objectView.btnPickUpload.setOnClickListener {
            if(EasyPermissions.hasPermissions(requireContext(),android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                val i = Intent(requireActivity(), ImagePickActivity::class.java)
                i.putExtra(Constant.MAX_NUMBER,1)
                startActivityForResult(i, Constant.REQUEST_CODE_PICK_IMAGE)
            }else{
                EasyPermissions.requestPermissions(this,"This application need your permission to access photo gallery.",991,android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
        var filterUpload = IntentFilter(ACTION_UPLOAD_PROFILE)
        requireActivity().registerReceiver(downloadReceiver,filterUpload)


        //registrasi notification channel dan notifikasi channel group dengan memanggil function
        //yang telah dibuat sebelumnya , registrasi pada lifecycle fragment (onCreateView)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CreateNotificationChannelGroup()
            CreateNotificationChannel()
        }

        return objectView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EspressoIdlingResource.increment()
        lifecycleScope.launch(Dispatchers.Main) {
            val response = try {
                withContext(Dispatchers.IO) {
                    apolloClient(requireContext()).query(GetUserByUsernameQuery(username = User.getUsername(requireContext())!!))
                        .await()
                }
            } catch (e: ApolloException) {
                Log.d("User Profile", "Failure", e)
                null
            }
            this@ProfileFragment.view?.let { LoadToView(it, response) }
            EspressoIdlingResource.decrement()
        }
    }

    private fun LoadToView(view: View, response: Response<GetUserByUsernameQuery.Data>?) {
        view.commodity_profile_progress_bar.visibility = View.GONE
        view.loaded_view.visibility = View.VISIBLE

        if(response?.data?.user_by_username?.role == "Supplier"){
            val commodities = response?.data?.user_by_username?.comodity
            if (commodities != null) {
                for(item in commodities){
                    itemList.add(
                        Commodity(
                            item!!.name,
                            item!!.image,
                            item!!.unit_price.toString(),
                            item!!.unit_type,
                            item!!.min_purchase.toString(),
                            CommodityUser(
                                "","","",""
                            ),
                            item.description
                        )
                    )
                }
            }

            val layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            adapter = CommodityRecycleViewAdapter(requireContext(),
                itemList
            )
            view.user_commodity_rv.layoutManager = layoutManager
            view.user_commodity_rv.adapter = adapter



            view.commodity_create.commodity_create.setOnClickListener {
                val intent = Intent(activity,CreateCommodity::class.java)
                startActivityForResult(intent, CREATE_COMMODITY_REQUEST_CODE)
            }

        }else{
            //case distributor login
            //view.user_commodity_rv.visibility = View.GONE
            //view.commodity_create.visibility = View.GONE

            val layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            LookingForAdapter = DistributorWantedItemsRecycleViewAdaptor(response?.data?.user_by_username?.looking_for as List<String>)
            view.user_commodity_rv.layoutManager = layoutManager
            view.user_commodity_rv.adapter = adapter

            view.commodity_create.text = "Add What ur Looking for!"

        }

        Picasso.get().load(response?.data?.user_by_username?.image?.link).into(view.profile_avatar)
        view.profile_avatar.setOnClickListener {
            zoomer.zoomImageFromThumb(requireContext(),view.profile_avatar,response?.data?.user_by_username?.image?.link!!,view.container,view.expanded_image,null)
        }

        view.username.text = cap.Capitalize(response?.data?.user_by_username?.username!!)
        view.role.text = response?.data?.user_by_username?.role


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == Constant.REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null){

            view?.btnPickUpload?.visibility = View.GONE
            val pickedImg = data?.getParcelableArrayListExtra<ImageFile>(Constant.RESULT_PICK_IMAGE)?.get(0)?.path
            var UploadService = Intent(requireContext(), UploadImageIntentService::class.java)
            val temp = UploadParams("DO",pickedImg!!)

            UploadService.putExtra(UPLOAD_CONTEXT, "profile_pic")
            UploadService.putExtra(UPLOAD_PARAMS,temp)
            UploadImageIntentService.enqueueWork(requireActivity(),UploadService)

        }else if(requestCode == CREATE_COMMODITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val createdCommodity = data.getParcelableExtra<Commodity>(CREATED_COMMODITY_DATA)

            itemList.add(createdCommodity!!)
            adapter!!.notifyDataSetChanged()

            requireView().user_commodity_rv.smoothScrollToPosition(adapter!!.itemCount - 1)

            //setelah commodiy berhasil dibuat, susun data respon dari server kedalam custom layout
            //yang akan kita pakai untuk menampilkan gaya notifikasi nantinya
            val contentView = RemoteViews(activity?.packageName, R.layout.custom_notification_layout)
            contentView.setTextViewText(R.id.sub_title,createdCommodity.name)
            contentView.setTextViewText(R.id.notif_title,"New Commodity Created!")

            //result pending intent dibuat agar nanti action open pada notification dapat ditekan, dam masuk ke DetailPage
            val resultIntent = Intent(activity, DetailPage::class.java)
            var commodity = Commodity(
                createdCommodity.name,
                createdCommodity.image,
                createdCommodity.unit_price,
                createdCommodity.unit_type,
                createdCommodity.min_purchase,
                CommodityUser("","","",""),
                createdCommodity.description
            )
            resultIntent.putExtra(DETAIL_EXTRA,commodity)
            resultIntent.putExtra(CONTEXT_EXTRA,"Commodity")
            resultIntent.putExtra(NOTIFICATION_CONTEXT,true)

            //deep link dengan notifikasi
            // Buat sebuah TackStackBuilder yang nantinya akan membantu kita memanggil backstack untuk mengelola activity dari aksi notifikasi untuk mekanisme deep link
            val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(activity).run {
                // masukkan intent kedalam Stack, beserta parent activity
                addNextIntentWithParentStack(resultIntent)
                //berikan request code dan flag
                getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT)
            }

//            contentView.setOnClickPendingIntent(R.id.button_open,resultPendingIntent)
            var notificationAmount = User.getNotificationAmount(requireContext())!!.toInt() + 1
            User.setNotificationAmount(requireContext(),notificationAmount.toString())

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //pada builder kita set komponen notification sesuai kebutuhan, seperti title, dan contentText berupa nama komoditas yang baru dibuat,
                //serta bigText kita berikan description dari commodity yang barusan kita buat, jangan lupa juga berikan channel id
                builder = NotificationCompat.Builder(requireContext(), "profile_fragment")
                    .setSmallIcon(R.drawable.ic_commodity)
                    .setLargeIcon(BitmapFactory.decodeResource(context?.getResources(), R.drawable.ic_commodity))
                    .setContent(contentView)
                    .setChannelId("profile_fragment")
                        //bagian ini akan menyimpan pending intent yang akan dipanggil ketika notifikasi ditekan
                    .setContentIntent(resultPendingIntent)
                    .setBadgeIconType(BADGE_ICON_SMALL)
                    .setNumber(User.getNotificationAmount(requireContext())!!.toInt())


            }else {
                //jika tidak invoke seperti biasanya tanpa channel
                builder = NotificationCompat.Builder(activity)
                    .setSmallIcon(R.drawable.ic_commodity)
                    .setCustomContentView(contentView)
                    .setContentIntent(resultPendingIntent)
            }

            //membuat intent untuk hide notification yang dibuat agar nantinya tombol action "Hide" pada notification dapat berfungsi
            //kita arahkan action ke dalam receiver yang telah kita buat
//            val hideIntent = Intent(activity,SMSReceiver::class.java)
//            //set action agar nantinya receiver dapat mengenali aksi apa yang harus diambil ke tika tombol hide di notification di panggil
//            hideIntent.setAction(EXTRA_HIDE)
//            val hidePendingIntent = PendingIntent.getBroadcast(activity,0,hideIntent,0)
//            builder.addAction(R.drawable.ic_baseline_email_24,"Hide",hidePendingIntent)
//
//            //buat pending intent dan berikan reuslt intent yang telah kita buat sebelumnya untuk membuka activity detail
//            val openPendingIntent = PendingIntent.getActivity(activity,0,resultIntent,0)
//            builder.addAction(R.drawable.ic_baseline_email_24,"Open",openPendingIntent)

            notificationManager.notify(notificationAmount,builder.build())
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        requireActivity().unregisterReceiver(downloadReceiver)
    }


    //pertama kita harus define function yang bertugas membuat channel Group jika version android >= android O
    @RequiresApi(Build.VERSION_CODES.O)
    private fun CreateNotificationChannelGroup(){
        //buat mutable list yang bertugas membuat beberapa grup channel
        val list = mutableListOf<NotificationChannelGroup>()
        //dalam kasus ini kita hanya membutuhkan 1 group yaitu activity
        list.add(
            NotificationChannelGroup(
                "Activity",
                "Activity"
            )
        )
        //invoke function untuk membuat channel group dan berikan parameter list dari object id dan nama untuk channel group
        notificationManager!!.createNotificationChannelGroups(list)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun CreateNotificationChannel(){
        notificationChannel = NotificationChannel("profile_fragment", "Create Commodity", NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.group = "Activity"
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.GREEN
        notificationChannel.enableVibration(true)
        notificationChannel.setShowBadge(true)
        notificationManager.createNotificationChannel(notificationChannel)
    }

    //kita implement member dari Class Loader
    //onCreateLoader
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        //disini kita melakukan query ke content Provider(Gallery), dengan menyertakan
        //context, URI tujuan , field yang ingin kita ambil , selection Criteria, dan order berdasarkan waktu foto tersebut di ambil
        //dalam hal ini DESCENDING(Dari terbaru ke terlama)
        return CursorLoader(
            requireContext(),
            queryUri,
            projectionField,
            selectionCriteria,
            null,
            MediaStore.Files.FileColumns.DATE_ADDED + " DESC"
        );
    }

    //pada onLoadFinished kita akan membaca data yang telah dikembalikan
    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        //pastikan array penampung kosong
        GalleryImages.clear()
        //cek apakah ada data
        if(data != null){
            //jika ada data, berhubung kursor sudah berada pada posisi terkahir, arahkan kursor untuk pindah ke bagian awal
            data.moveToFirst()
            //lakukam pembacaan data dari awal sampai akhir
            while (!data.isAfterLast){
                // setiap data yang berhasil dibaca masukkan kedalam array kosong yang telah kita buat sebelumnya
                GalleryImages.add(
                    MediaLoaderData(
                        //untuk mendapat URI yang dapat nantinya di load kedalam imageView maka kita akan menggabungkan queryURI dengan id yang kita dapatkan dari hasil query sebelumnya
                        //misal queryURI = "/content/media/gallery/image"
                        //dan ID = 15432345
                        // kita gabungkan menjadi /content/media/gallery/image/15432345
                        //hal ini bertujuan agar sistem operasi mengetahui gambar mana yang ingin kita tampilkan ke imgaeIvew
                        Uri.withAppendedPath(queryUri, "" + data.getLong(data.getColumnIndex(MediaStore.Images.Media._ID))).toString()
                    )
                )
                //instruksikan cursor untuk pindah ke baris selanjutnya dan membaca data selanjutnya
                data.moveToNext()
            }

            //setelah data selesai dibaca, masukkan data URI gambar yang telah kita dapatkan kedalam recycle view
            recently_added_photo_rv.apply {
                //masukkan context dan array of URI yang telah kita produce dari proses sebelumnya
                //jangan lupa mempassing callback function yang dapat membantu kita dalam melakukan zoom terhadap image yang terload ke recycle view
                adapter = RecentlyAddedImageAdapter(requireContext(),GalleryImages){ position, imageView ->
                    activity?.let { ac ->
                        zoomer.zoomImageFromThumb(
                            ac,
                            imageView,
                            GalleryImages[position].uri!!,
                            requireView().container,
                            requireView().expanded_image,
                            null
                        )
                    }
                }
                //masukkan layout manager yang dibutuhkan oleh recycel view, disini kami menggunakan layout manager yang telah kami custom sesuai kebutuhan
                layoutManager = SpeedLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
        }
    }

    //pada onLoaderReset kita panggil adaptor untuk menjalankan notifyDatasetChanged, agar jika terjadi perubahan configurasi
    //loader dapat tahu dan mengupdate recycle view
    override fun onLoaderReset(loader: Loader<Cursor>) {
//        if(recently_added_photo_rv.adapter != null) {
//            recently_added_photo_rv.adapter?.notifyDataSetChanged()
//        }
    }

}