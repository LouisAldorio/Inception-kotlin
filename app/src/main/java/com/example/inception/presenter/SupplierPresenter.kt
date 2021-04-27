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

//buat sebuah class SupplierPresenter yang menerima 2 parameter yaitu context yang akan dikirim dari parent pemanggil
//dan sebuah param interface yang telah kita buat sembelumnya.
class SupplierPresenter(private val context : Context,setView : SupplierInterface) {
    //simpan interface kedalam variable private
    private  var view = setView
    //buat sebuah variable yang akan menampung list dari data supplier yang akan kita dapatkan dari server
    private var SupplierModel = mutableListOf<Supplier>()

    // dalam class presenter ini, kita akan mengeksekusi logic untuk fetchd ata ke server Graphql
    //function dibawah menerima sebuah parameter berupa sebuah coroutine yang nantinya akna membuat proses
    // fetching data ke server bersifat asynchronous
    fun LoadSupplierList(scope: CoroutineScope) {

        //mulai proses saat proses diinvoke
        scope.launch {
            //lakukan request ke server dan berikan parameter yang sesuai
            val response = try {
                apolloClient(context).query(GetSupplierQuery(role = "Supplier")).await()
            }catch (e: ApolloException){
                Log.d("SupplierList", "Failure", e)
                null
            }
            //data yang dikembalikan ktia simpan terlebih dahulu kedalam variable
            val suppliers = response?.data?.users_by_role

            //cek apakah tidak ada error dand ata tidak null
            if(suppliers != null && !response.hasErrors()) {
                //loop untuk mengisi data yang didapat dari server kedalam model dan disimpan ke variable list yang telah kita buat sebelumnya
                for(item in suppliers){
                    SupplierModel.add(
                        Supplier(
                            item!!.image.link,
                            item!!.username
                        )
                    )
                }
                //panggil fungsi RenderSupplierList yang kita buat pada interface, kia belum mengoverride function ini
                // tetapi nanti akan ktia buat
                // berikan varibale list yang sudah berisi model dari supplier
                view.RenderSupplierList(SupplierModel)
            }
        }
    }
}