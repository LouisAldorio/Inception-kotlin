package com.example.inception.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.inception.R
import com.example.inception.activity.CreateCommodity
import com.example.inception.api.Upload
import com.example.inception.constant.ACTION_UPLOAD
import com.example.inception.constant.UPLOADED_FILE_URL
import com.example.inception.constant.UPLOAD_PARAMS
import com.example.inception.data.UploadParams
import com.example.inception.data.UploadResponse
import com.example.inception.objectClass.User
import com.example.inception.service.UploadImageIntentService
import com.example.inception.utils.ImageZoomer
import com.google.android.material.shape.CornerFamily
import com.squareup.picasso.Picasso
import com.vincent.filepicker.Constant
import com.vincent.filepicker.activity.ImagePickActivity
import com.vincent.filepicker.filter.entity.ImageFile
import kotlinx.android.synthetic.main.fragment_profile.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Call
import java.io.File


class ProfileFragment : Fragment() {
    //deklarasi variabel Global kosong untuk menampung data dari receiver
    var uploadedURL : String? = ""
    //buat receiver pada fragment profile
    private val downloadReceiver = object : BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            //ambil data URL yang dikirimkan dari broadcast
            uploadedURL = p1?.getStringExtra(UPLOADED_FILE_URL)
            //load gambar yang telah terupload ke dalam avatar view
            Picasso.get().load(uploadedURL).into(requireView().profile_avatar)

            requireView().profile_avatar.setOnClickListener {
                zoomer.zoomImageFromThumb(requireContext(),requireView().profile_avatar,uploadedURL!!,requireView().container,requireView().expanded_image)
            }
        }
    }

    val zoomer = ImageZoomer()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        zoomer.shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
        // Inflate the layout for this fragment
        var objectView = inflater.inflate(R.layout.fragment_profile, container, false)

        objectView.logout.setOnClickListener {
            User.removeToken(requireContext())
            requireActivity().finish()
        }

        objectView.commodity_create.setOnClickListener {
            val intent = Intent(activity,CreateCommodity::class.java)
            startActivity(intent)
        }

        //onCreateView Profile Fragment
        objectView.btnPickUpload.setOnClickListener {
            //untuk mengakses gallery kita terlebih dahulu minta izin ke user
            if(EasyPermissions.hasPermissions(requireContext(),android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                // jika di setujui , jalankan Intent Implisit dengan activity tujuan berupa ImagePickActivity
                val i = Intent(requireActivity(), ImagePickActivity::class.java)
                i.putExtra(Constant.MAX_NUMBER,1)
                //ketika activity selesai , kita berharap activity mengembalikan hasil berupa path foto yang telah dipilih
                startActivityForResult(i, Constant.REQUEST_CODE_PICK_IMAGE)
            }else{
                // tampilkan permission request saat belum mendapat permission dari user
                EasyPermissions.requestPermissions(this,"This application need your permission to access photo gallery.",991,android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        //pada fungsi OnCreateView yng telah kita overwrite registerkan receiver yang kita buat sebelumnya
        //arahkan receiver untuk menerima broadcast dengan action ID = ACTION_UPLOAD
        var filterUpload = IntentFilter(ACTION_UPLOAD)
        requireActivity().registerReceiver(downloadReceiver,filterUpload)

        return objectView
    }


    // override method onActivityResult untuk handling data dari ImagePickActivity.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == Constant.REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null){

            view?.btnPickUpload?.visibility = View.GONE

//            view?.profile_avatar?.visibility = View.VISIBLE

            // membuat variable yang menampung path dari picked image.
            val pickedImg = data?.getParcelableArrayListExtra<ImageFile>(Constant.RESULT_PICK_IMAGE)?.get(0)?.path

            //inisiasi intent untuk mengaktifkan service setelah user berhasil memilih gambar
            var UploadService = Intent(requireContext(), UploadImageIntentService::class.java)
            //masukkan parameter yang nantinya dibutuhkan oleh service untuk mengeksekusi task
            val temp = UploadParams("GDRIVE",pickedImg!!)
            UploadService.putExtra(UPLOAD_PARAMS,temp)
            //mulai service
            UploadImageIntentService.enqueueWork(requireActivity(),UploadService)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //unregister receiver jika activity dihancurkan agar tidak terjadi memory leak
        requireActivity().unregisterReceiver(downloadReceiver)
    }






//    private fun upload() {
//
////        val loading = ProgressDialog(requireContext())
////        loading.setMessage("Please wait...")
////        loading.show()
//
//        val call = Upload().instance().upload(type_adaptor, imageFile)
//        call.enqueue(object : retrofit2.Callback<UploadResponse> {
//
//            // handling request saat fail
//            override fun onFailure(call: Call<UploadResponse>?, t: Throwable?) {
////                loading.dismiss()
//                Toast.makeText(requireContext(), "Connection error", Toast.LENGTH_SHORT).show()
//                Log.d("ONFAILURE", t.toString())
//            }
//
//            override fun onResponse(
//                call: Call<UploadResponse>?,
//                response: retrofit2.Response<UploadResponse>?
//            ) {
////                loading.dismiss()
//                if (response?.body()?.status?.contains("Success", true)!!) {
//                    Picasso.get().load(response?.body()!!.imageURL).into(requireView().profile_avatar)
//                }
//            }
//        })
//    }
}