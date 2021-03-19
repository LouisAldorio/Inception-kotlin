package com.example.inception.utils

import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.inception.api.Upload
import com.example.inception.data.UploadResponse
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class ImageUpload {

    fun upload(type_adaptor: String, imageFile : String) : String {

        val requestBody = RequestBody.create(MediaType.parse("multipart"), File(imageFile))

        val imageFile = MultipartBody.Part.createFormData("File", File(imageFile)?.name,requestBody)

        val type_adaptor = MultipartBody.Part.createFormData("Type_Adaptor",type_adaptor)
        var url = ""

        val call = Upload().instance().upload(type_adaptor, imageFile)
        val response = call.execute().body()
//        call.enqueue(object : retrofit2.Callback<UploadResponse> {
//
//            // handling request saat fail
//            override fun onFailure(call: Call<UploadResponse>?, t: Throwable?) {
//                Log.d("ONFAILURE", t.toString())
//            }
//
//            override fun onResponse(call: Call<UploadResponse>?, response: retrofit2.Response<UploadResponse>?) {
//                Log.i("upload",response.toString())
//                if (response?.body()?.status?.contains("Success", true)!!) {
////                    Log.i("upload",response.body()!!.imageURL)
//                    url = response.body()!!.imageURL
//
//                }
//            }
//            Log.i("upload",url)
//        })

        url = response!!.imageURL
        Log.i("upload",url)
        return url
    }
}