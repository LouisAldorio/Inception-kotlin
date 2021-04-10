package com.example.inception.loader

import android.content.Context
import androidx.loader.content.AsyncTaskLoader
import com.apollographql.apollo.ApolloQueryCall
import com.example.inception.GetDistributorQuery
import com.example.inception.api.apolloClient

//berikut class DistributorAsyncTaskLoader yang ktia inherit dari class AsyncTaskLoader
class DistributorAsyncTaskLoader(context: Context) : AsyncTaskLoader<
        ApolloQueryCall<GetDistributorQuery.Data>>(context) {

    //kita override sebuah member wajib yaitu loadInBackground yang akan berjalan secar async di background
    override fun loadInBackground(): ApolloQueryCall<GetDistributorQuery.Data>? {
        //definisikan query params
        val params = "Distributor"
        //load schema dan berikan param yang telah didefinisikan
        val loadedSchema = GetDistributorQuery(role = params)
        //kirimkan kembali schema Graphql yang berhasil dibentuk dan di load agar nantinya pada memberFunction (onLoadFinished)
        //kita dapat mengaksesnya dari statement params "data" dengan tipe ApolloQueryCall<GetDistributorQuery.Data>?
        // yang nantinya digunakan untuk fetch data distributors
        return apolloClient(context).query(loadedSchema)
    }
}