package com.example.inception.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.apollographql.apollo.ApolloQueryCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.example.inception.CustomLayoutManager.CustomAutoScrollCenterZoomLayoutManager
import com.example.inception.GetCommodityQuery
import com.example.inception.GetDistributorQuery
import com.example.inception.R
import com.example.inception.adaptor.DistributorRecycleViewAdaptor
import com.example.inception.api.apolloClient
import com.example.inception.loader.DistributorAsyncTaskLoader
import com.example.inception.utils.EspressoIdlingResource
import kotlinx.android.synthetic.main.distributor_item_layout.view.*
import kotlinx.android.synthetic.main.fragment_distributor.*
import kotlinx.android.synthetic.main.fragment_distributor.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


// Kami akan menggunakan AsyncTaskLoader untuk melakukan Load Schema Graphql Sekaligus Load Data Distributor ke dalam View yang telah kami sediakan
//disini AsyncTaskLoader kami implementasikan di fragment distributor
class DistributorFragment : Fragment(),
    //AsyncTaskLoader ini akan mengambalikan sebuat callback yang bernilai ApolloQueryCall yang membawa schema untuk Query Distributor
    LoaderManager.LoaderCallbacks<ApolloQueryCall<GetDistributorQuery.Data>> {

    //definisikan laoder ID
    val DISTRIBUTOR_LOADER_ID = 1326

    //definisikan adapter distributor
    var DistributorAdapater: DistributorRecycleViewAdaptor? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_distributor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //pada lifecycle onViewCreated assign adapter yang telah kita buat sebelumnya dengan isi yang kosong
        // pertama kali di create recycewl view akan merender recycle view yang kosong
        DistributorAdapater = DistributorRecycleViewAdaptor(requireContext(), ArrayList())

        //snaphelper disini hanya membantu penyesuaian layout manager agar user experince tercapai
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(rv_distributor)

        //berikan adapter sebelumnya yang telah dibuat kepada recycel View Adapter nya
        rv_distributor.adapter = DistributorAdapater
        //berikan juga layout manager kepada recycleView adaptor
        rv_distributor.layoutManager = CustomAutoScrollCenterZoomLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        //disin onViewCreated kita juga melakukan inisialisasi Loader agar nantinya loader ini dipanggil
        LoaderManager.getInstance(this).initLoader(DISTRIBUTOR_LOADER_ID, null, this).forceLoad()
    }

    //pada onCretaeLoader kita akan mereturn sebauh AsyntaskLoader yang telah ktia definisikan pada file dan class terpisah
    override fun onCreateLoader(
        id: Int,
        args: Bundle?
    ): Loader<ApolloQueryCall<GetDistributorQuery.Data>> {
        //jangan lupa berikan context nya

        return DistributorAsyncTaskLoader(requireContext())
    }

    //pada onLoad Finished disini artinya kita telah berhasil melakukan load terhadap schema graphql tujuan yang digunkan untuk melakukan fetch
    // terhadap data distributor
    override fun onLoadFinished(
        loader: Loader<ApolloQueryCall<GetDistributorQuery.Data>>,
        data: ApolloQueryCall<GetDistributorQuery.Data>?
    ) {
        //setelah schema berhasil di load pada asyncTask Loader kita lakukan fetch data  distributor ke server Graphql
        //seperti biasa kita melakukan upaya fetch pada thread IO lalu mengupdate Main Thread ketika data berhasil di fetch
        EspressoIdlingResource.increment()
        lifecycleScope.launch(Dispatchers.Main) {
            //request dimulai pada thread IO
            val response = try {
                withContext(Dispatchers.IO) {
                    data?.await()
                }
            } catch (e: ApolloException) {
                Log.d("DistributorList", "Failure", e)
                null
            }

            //setelah response diterima
            this@DistributorFragment.view?.let {
                val distributors = response?.data?.users_by_role
                //validasi bahwa data ada
                if (distributors != null && !response.hasErrors()) {
                    //sembunyikan progressbar
                    it.progress_bar_distributor.visibility = View.GONE

                    // pada adapter distributor kami ada membuat sebuah method yang berguna unutk mengupdateseluruh data yang ada pada recycle view,
                    //maka setelah data diterima, lakukan update pada recycle view
                    DistributorAdapater!!.setDistributors(distributors as List<GetDistributorQuery.Users_by_role>)
                    EspressoIdlingResource.decrement()
                }
            }
        }
    }


    //pada onLoaderReset kali ini kita akan menosongkan data recycle view jika terjadi perubahan config
    override fun onLoaderReset(loader: Loader<ApolloQueryCall<GetDistributorQuery.Data>>) {
        //panggil method yang sama seperti yang ktia gunkana ketika data berhasil di fetch,
        //tapi berikan array kosong
        DistributorAdapater!!.setDistributors(ArrayList())
    }
}