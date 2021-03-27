package com.example.inception.fragment

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.example.inception.GetUserByUsernameQuery
import com.example.inception.R
import com.example.inception.activity.CreateCommodity
import com.example.inception.adaptor.CommodityRecycleViewAdapter
import com.example.inception.api.apolloClient
import com.example.inception.constant.*
import com.example.inception.data.Commodity
import com.example.inception.data.CommodityUser
import com.example.inception.data.UploadParams
import com.example.inception.objectClass.User
import com.example.inception.service.UploadImageIntentService
import com.example.inception.utils.ImageZoomer
import com.squareup.picasso.Picasso
import com.vincent.filepicker.Constant
import com.vincent.filepicker.activity.ImagePickActivity
import com.vincent.filepicker.filter.entity.ImageFile
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pub.devrel.easypermissions.EasyPermissions
import java.net.URL


class ProfileFragment : Fragment() {

    var uploadedURL : String? = ""
    val itemList : MutableList<Commodity> = arrayListOf()
    var adapter : CommodityRecycleViewAdapter? = null


    //notification manager
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder

    private val downloadReceiver = object : BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            if(p1?.getStringExtra(UPLOAD_CONTEXT) == "profile_pic") {
                uploadedURL = p1?.getStringExtra(UPLOADED_FILE_URL)
                Picasso.get().load(uploadedURL).into(requireView().profile_avatar)

                requireView().profile_avatar.setOnClickListener {
                    zoomer.zoomImageFromThumb(requireContext(),requireView().profile_avatar,uploadedURL!!,requireView().container,requireView().expanded_image)
                }
            }
        }
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

        objectView.logout.setOnClickListener {
            User.removeToken(requireContext())
            requireActivity().finish()
        }

        objectView.commodity_create.setOnClickListener {
            val intent = Intent(activity,CreateCommodity::class.java)
            startActivityForResult(intent, CREATE_COMMODITY_REQUEST_CODE)
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


        //registrasi notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CreateNotificationChannelGroup()
            CreateNotificationChannel()
        }

        return objectView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        }
    }

    private fun LoadToView(view: View, response: Response<GetUserByUsernameQuery.Data>?) {
        view.commodity_profile_progress_bar.visibility = View.GONE
        view.loaded_view.visibility = View.VISIBLE
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
                        )
                    )
                )
            }
        }
        if(response?.data?.user_by_username?.role == "Supplier"){

            val layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            adapter = CommodityRecycleViewAdapter(requireContext(),
                itemList
            )
            view.user_commodity_rv.layoutManager = layoutManager
            view.user_commodity_rv.adapter = adapter

        }else{
            view.user_commodity_rv.visibility = View.GONE
            view.commodity_create.visibility = View.GONE
        }

        Picasso.get().load(response?.data?.user_by_username?.image?.link).into(view.profile_avatar)
        view.profile_avatar.setOnClickListener {
            zoomer.zoomImageFromThumb(requireContext(),view.profile_avatar,response?.data?.user_by_username?.image?.link!!,view.container,view.expanded_image)
        }

        view.username.text = response?.data?.user_by_username?.username
        view.role.text = response?.data?.user_by_username?.role
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == Constant.REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null){

            view?.btnPickUpload?.visibility = View.GONE
            val pickedImg = data?.getParcelableArrayListExtra<ImageFile>(Constant.RESULT_PICK_IMAGE)?.get(0)?.path
            var UploadService = Intent(requireContext(), UploadImageIntentService::class.java)
            val temp = UploadParams("GDRIVE",pickedImg!!)

            UploadService.putExtra(UPLOAD_CONTEXT, "profile_pic")
            UploadService.putExtra(UPLOAD_PARAMS,temp)
            UploadImageIntentService.enqueueWork(requireActivity(),UploadService)

        }else if(requestCode == CREATE_COMMODITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val createdCommodity = data.getParcelableExtra<Commodity>(CREATED_COMMODITY_DATA)

            itemList.add(createdCommodity!!)
            adapter!!.notifyDataSetChanged()

            requireView().user_commodity_rv.smoothScrollToPosition(adapter!!.itemCount - 1)

            //ketika commodity berhasil di buat, kirim notifikasi
            val contentView = RemoteViews(activity?.packageName, R.layout.custom_notification_layout)
            contentView.setTextViewText(R.id.sub_title,createdCommodity.name)
            contentView.setTextViewText(R.id.notif_title,"New Commodity Created!")
//            val newurl = URL(createdCommodity.image[0])
//            val mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream())
//            contentView.setImageViewBitmap(R.id.image,mIcon_val)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder = Notification.Builder(activity, "profile_fragment")
                    .setCustomContentView(contentView)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_launcher_background))
//                    .setContentIntent(pendingIntent)
            }else {
                builder = Notification.Builder(activity)
                    .setCustomContentView(contentView)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_launcher_background))
            }
            notificationManager.notify(1234,builder.build())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().unregisterReceiver(downloadReceiver)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun CreateNotificationChannelGroup(){
        val list = mutableListOf<NotificationChannelGroup>()
        list.add(
            NotificationChannelGroup(
                "WholeSaler",
                "WholeSaler"
            )
        )
        notificationManager!!.createNotificationChannelGroups(list)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun CreateNotificationChannel(){
        notificationChannel = NotificationChannel("profile_fragment", "Create Commodity", NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.group = "WholeSaler"
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.GREEN
        notificationChannel.enableVibration(true)
        notificationManager.createNotificationChannel(notificationChannel)
    }

}