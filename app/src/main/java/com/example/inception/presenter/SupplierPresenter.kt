package com.example.inception.presenter

import android.content.Context
import android.util.Log
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.example.inception.GetSupplierQuery
import com.example.inception.`interface`.SupplierInterface
import com.example.inception.api.apolloClient
import com.example.inception.data.Supplier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SupplierPresenter(private val context : Context,setView : SupplierInterface) {

    private  var view = setView
    private var SupplierModel = mutableListOf<Supplier>()

    fun LoadSupplierList(scope: CoroutineScope) {

        scope.launch {

            val response = try {
                apolloClient(context).query(GetSupplierQuery(role = "Supplier")).await()
            }catch (e: ApolloException){
                Log.d("SupplierList", "Failure", e)
                null
            }

            val suppliers = response?.data?.users_by_role

            if(suppliers != null && !response.hasErrors()) {

                for(item in suppliers){
                    SupplierModel.add(
                        Supplier(
                            item!!.image.link,
                            item!!.username
                        )
                    )
                }

                view.RenderSupplierList(SupplierModel)
            }
        }
    }

    private fun writeToCache(){

    }
}