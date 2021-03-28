package com.example.inception.api

import com.example.inception.data.UploadResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

class Upload {

    companion object {
        const val BASE_URL = "https://upload.dextion.com/"
    }

    // init retrofit
    private fun retrofit() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun instance() : ApiInterface {
        return retrofit().create(ApiInterface::class.java)
    }
}

// interface dari retrofit
interface ApiInterface{

    @Multipart
    @POST("upload/all")
    fun upload(@Part Type_Adapter : MultipartBody.Part, @Part File : MultipartBody.Part) : Call<UploadResponse>

}