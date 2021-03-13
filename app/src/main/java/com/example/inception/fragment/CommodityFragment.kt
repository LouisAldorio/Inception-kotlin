package com.example.inception.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.example.inception.GetCommodityQuery
//import com.example.inception.GetSupplierQuery
import com.example.inception.R
import com.example.inception.`interface`.InterfaceData
import com.example.inception.adaptor.AllCategorizedCommodityRecycleViewAdapter
import com.example.inception.adaptor.CommodityRecycleViewAdapter
import com.example.inception.adaptor.SupplierRecycleViewAdaptor
import com.example.inception.api.apolloClient
import com.example.inception.data.AllCategorizedCommodity
import com.example.inception.data.Commodity
import kotlinx.android.synthetic.main.categorized_commodity_container.view.*
import kotlinx.android.synthetic.main.fragment_commodity.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList


class CommodityFragment : Fragment() {

    lateinit var mainCategoryRecycler: RecyclerView

    var mainRecyclerAdapter: AllCategorizedCommodityRecycleViewAdapter? = null

    lateinit var objectView: View

    //inisiasi interfaceData
    private lateinit var interfaceData: InterfaceData
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // masukkan interface yang ada pada activity kedalam variable yang telah dibuat
        interfaceData = activity as InterfaceData

        objectView = inflater.inflate(R.layout.fragment_commodity, container, false)
        //pada saat tombol di click kirim pesan dibawah dari commodity fragment ke supplier fragment
        objectView.send.setOnClickListener {
            interfaceData.KirimData("saya dikirm dari activity commodity")
        }
        return objectView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //disini kita menggunakan fungsi lifescycle scope yang merupakan penghubung dengan corountineScope
        // yang membedakan nya ialah kita menjalan proses asinkronos pada thread yang akan secara otomatis di manage oleh sistem operasi
        //proses didalam lifecycleScope akan secara otomatis di cancel apa bila activity dihancurkan, tidak perlu manual kita lakukan lagi dengan menimpa fungsi onDestroy
        lifecycleScope.launch(Dispatchers.Main) {
            //disini kita membuat request ke serve menggunakan protokol graphql dimana kita akan menjalankan query
            // query yang dijalankan berupa GetCommodityQuery dengan parameter page dan limit
            val response = try {
                //untuk lebih spesifik dan sesuai kebutuhan, ketika melakukan request ke server kami kana menggunakan IO thread
                //ketika sudah mendapat data dari server makan secara otomatis thread IO akan memberikan data kepada Main Thread lagi
                withContext(Dispatchers.IO) {
                    apolloClient(requireContext()).query(GetCommodityQuery(page = 1, limit = 10))
                        .await()
                }
            } catch (e: ApolloException) {
                //jika gagal atau terjadi error maka akan kita print
                Log.d("CommodityList", "Failure", e)
                null
            }
            // jika data berhasil dikembalikan makan kita pass ke fungsi arrange
            this@CommodityFragment.view?.let { arrange(it, response) }
        }
    }

    private fun arrange(view: View, response: Response<GetCommodityQuery.Data>?) {
        // arrange bertujuan untuk menyusun data ke dalam view setelah respon data diterima dari server
        val allCategoryList: MutableList<AllCategorizedCommodity> = ArrayList()
        val commodities = response?.data?.comoditiesByCategory
        if (commodities != null && !response.hasErrors()) {
            view.findViewById<ProgressBar>(R.id.commodity_progress_bar).visibility = View.GONE
            for(item in commodities){
                val categoryItemList: MutableList<Commodity> = ArrayList()
                for (commodity in item.nodes){
                    categoryItemList.add(
                        Commodity(commodity.name,commodity.image[0])
                    )
                }
                allCategoryList.add(AllCategorizedCommodity(item.category.name, categoryItemList))
            }
        }
        setMainCategoryRecycler(allCategoryList)
    }


    private fun setMainCategoryRecycler(allCategoryList: List<AllCategorizedCommodity>) {

        mainCategoryRecycler = objectView.findViewById(R.id.rvMain)

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        mainCategoryRecycler.setLayoutManager(layoutManager)

        mainRecyclerAdapter = AllCategorizedCommodityRecycleViewAdapter(requireActivity(), allCategoryList)
        mainCategoryRecycler.setAdapter(mainRecyclerAdapter)
    }

}