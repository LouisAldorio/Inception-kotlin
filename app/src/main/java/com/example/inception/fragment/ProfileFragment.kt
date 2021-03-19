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
import com.example.inception.api.Upload
import com.example.inception.constant.ACTION_UPLOAD
import com.example.inception.constant.UPLOADED_FILE_URL
import com.example.inception.constant.UPLOAD_PARAMS
import com.example.inception.data.UploadParams
import com.example.inception.data.UploadResponse
import com.example.inception.objectClass.User
import com.example.inception.service.UploadImageIntentService
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
    var uploadedURL : String? = ""
    private val downloadReceiver = object : BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            uploadedURL = p1?.getStringExtra(UPLOADED_FILE_URL)
            Picasso.get().load(uploadedURL).into(requireView().profile_avatar)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var objectView = inflater.inflate(R.layout.fragment_profile, container, false)

        objectView.logout.setOnClickListener {
            User.removeToken(requireContext())
            requireActivity().finish()
        }

        // memberikan onClick listener pada btnPickUpload
        objectView.btnPickUpload.setOnClickListener {

            // check permission untuk android M dan ke atas.
            // saat permission disetujui oleh user maka jalan script untuk intent ke pick image.
            if(EasyPermissions.hasPermissions(requireContext(),android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                val i = Intent(requireActivity(), ImagePickActivity::class.java)
                i.putExtra(Constant.MAX_NUMBER,1)
                startActivityForResult(i, Constant.REQUEST_CODE_PICK_IMAGE)
            }else{
                // tampilkan permission request saat belum mendapat permission dari user
                EasyPermissions.requestPermissions(this,"This application need your permission to access photo gallery.",991,android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }

        }
        var filterUpload = IntentFilter(ACTION_UPLOAD)
        requireActivity().registerReceiver(downloadReceiver,filterUpload)

        return objectView
    }

    // override method onActivityResult untuk handling data dari pickImageActivity.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == Constant.REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null){

            view?.btnPickUpload?.visibility = View.GONE

            view?.profile_avatar?.visibility = View.VISIBLE

            // membuat variable yang menampung path dari picked image.
            val pickedImg = data?.getParcelableArrayListExtra<ImageFile>(Constant.RESULT_PICK_IMAGE)?.get(0)?.path

            var UploadService = Intent(requireContext(), UploadImageIntentService::class.java)
            val temp = UploadParams("GDRIVE",pickedImg!!)

            UploadService.putExtra(UPLOAD_PARAMS,temp)
            UploadImageIntentService.enqueueWork(requireActivity(),UploadService)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
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