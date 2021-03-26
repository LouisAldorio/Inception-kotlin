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
import androidx.lifecycle.lifecycleScope
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.bumptech.glide.Glide
import com.example.inception.GetCommodityQuery
import com.example.inception.GetUserByUsernameQuery
import com.example.inception.R
import com.example.inception.activity.CreateCommodity
import com.example.inception.api.Upload
import com.example.inception.api.apolloClient
import com.example.inception.constant.ACTION_UPLOAD
import com.example.inception.constant.CREATE_COMMODITY_REQUEST_CODE
import com.example.inception.constant.UPLOADED_FILE_URL
import com.example.inception.constant.UPLOAD_PARAMS
import com.example.inception.data.UploadParams
import com.example.inception.data.UploadResponse
import com.example.inception.objectClass.User
import com.example.inception.service.UploadImageIntentService
import com.example.inception.utils.ImageZoomer
import com.google.android.material.shape.CornerFamily
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.vincent.filepicker.Constant
import com.vincent.filepicker.activity.ImagePickActivity
import com.vincent.filepicker.filter.entity.ImageFile
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        var objectView = inflater.inflate(R.layout.fragment_profile, container, false)

        objectView.logout.setOnClickListener {
            User.removeToken(requireContext())
            requireActivity().finish()
        }

        objectView.commodity_create.setOnClickListener {
            val intent = Intent(activity,CreateCommodity::class.java)
            startActivityForResult(intent, CREATE_COMMODITY_REQUEST_CODE)
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
        var filterUpload = IntentFilter(ACTION_UPLOAD)
        requireActivity().registerReceiver(downloadReceiver,filterUpload)

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
            UploadService.putExtra(UPLOAD_PARAMS,temp)
            UploadImageIntentService.enqueueWork(requireActivity(),UploadService)
        }else if(requestCode == CREATE_COMMODITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {

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