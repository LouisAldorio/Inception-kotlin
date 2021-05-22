package com.example.inception.api

import android.content.Context
import com.apollographql.apollo.ApolloClient
import com.example.inception.objectClass.User
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import java.util.concurrent.TimeUnit


//instance means the client
private var instance: ApolloClient? = null

fun apolloClient(context: Context): ApolloClient {

    if (instance != null) {
        return instance!!
    }

    instance =  ApolloClient.builder()
        .serverUrl("https://inception.louisaldorio.site/query")
        .okHttpClient(
            OkHttpClient.Builder().callTimeout(30,TimeUnit.SECONDS)
            .addInterceptor(AuthorizationInterceptor(context))
            .build()
        )
        .build()

    return instance!!
}


//add header token here
private class AuthorizationInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", User.getToken(context) ?: "")
            .build()

        return chain.proceed(request)
    }
}