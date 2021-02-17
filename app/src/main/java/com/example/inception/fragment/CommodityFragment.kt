package com.example.inception.fragment

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
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.example.inception.GetCommodityQuery
import com.example.inception.GetSupplierQuery
import com.example.inception.R
import com.example.inception.adaptor.AllCategorizedCommodityRecycleViewAdapter
import com.example.inception.adaptor.CommodityRecycleViewAdapter
import com.example.inception.adaptor.SupplierRecycleViewAdaptor
import com.example.inception.api.apolloClient
import com.example.inception.data.AllCategorizedCommodity
import com.example.inception.data.Commodity
import kotlinx.android.synthetic.main.categorized_commodity_container.view.*
import kotlinx.android.synthetic.main.fragment_commodity.view.*
import java.util.ArrayList


class CommodityFragment : Fragment() {

    lateinit var mainCategoryRecycler: RecyclerView

    var mainRecyclerAdapter: AllCategorizedCommodityRecycleViewAdapter? = null

    lateinit var objectView : View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        objectView = inflater.inflate(R.layout.fragment_commodity, container, false)

//        val categoryItemList: MutableList<Commodity> = ArrayList()
//        categoryItemList.add(Commodity("Maria Ozawa", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcThcsdrANTmext5iM3tef0GKGGgv2seeWpNPA&usqp=CAU"))
//        categoryItemList.add(Commodity("Aoi Sora", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSe1_d38bWCsr5GoHupSX82WkjWujntiRiPLQ&usqp=CAU"))
//        categoryItemList.add(Commodity("Sakura Haruno", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTT2o3vUrZBTiZ4msyyL8MeK6N2UlYkzVEZ_Q&usqp=CAU"))
//        categoryItemList.add(Commodity("Ino Yamanaka", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRGBTlbSz9UGFiU9ErCRl06P-wAwBi8Yw-oPA&usqp=CAU"))
//        categoryItemList.add(Commodity("Hinata Hyuga", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSgs_iBwZ97480GesBwA7A3Jc0BBXxtuw8hDw&usqp=CAU"))
//        categoryItemList.add(Commodity("Tenten", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR7y-ZWfFb2aLI7n2Lb2m8jQnCeRUOy9zQiqQ&usqp=CAU"))
//        categoryItemList.add(Commodity("Maria Ozawa", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcThcsdrANTmext5iM3tef0GKGGgv2seeWpNPA&usqp=CAU"))
//        categoryItemList.add(Commodity("Aoi Sora", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSe1_d38bWCsr5GoHupSX82WkjWujntiRiPLQ&usqp=CAU"))
//        categoryItemList.add(Commodity("Sakura Haruno", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTT2o3vUrZBTiZ4msyyL8MeK6N2UlYkzVEZ_Q&usqp=CAU"))
//        categoryItemList.add(Commodity("Ino Yamanaka", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRGBTlbSz9UGFiU9ErCRl06P-wAwBi8Yw-oPA&usqp=CAU"))
//        categoryItemList.add(Commodity("Hinata Hyuga", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSgs_iBwZ97480GesBwA7A3Jc0BBXxtuw8hDw&usqp=CAU"))
//        categoryItemList.add(Commodity("Tenten", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR7y-ZWfFb2aLI7n2Lb2m8jQnCeRUOy9zQiqQ&usqp=CAU"))
//
//        val categoryItemList2: MutableList<Commodity> = ArrayList()
//        categoryItemList2.add(Commodity("Maria Ozawa", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcThcsdrANTmext5iM3tef0GKGGgv2seeWpNPA&usqp=CAU"))
//        categoryItemList2.add(Commodity("Aoi Sora", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSe1_d38bWCsr5GoHupSX82WkjWujntiRiPLQ&usqp=CAU"))
//        categoryItemList2.add(Commodity("Sakura Haruno", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTT2o3vUrZBTiZ4msyyL8MeK6N2UlYkzVEZ_Q&usqp=CAU"))
//        categoryItemList2.add(Commodity("Ino Yamanaka", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRGBTlbSz9UGFiU9ErCRl06P-wAwBi8Yw-oPA&usqp=CAU"))
//        categoryItemList2.add(Commodity("Hinata Hyuga", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSgs_iBwZ97480GesBwA7A3Jc0BBXxtuw8hDw&usqp=CAU"))
//        categoryItemList2.add(Commodity("Tenten", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR7y-ZWfFb2aLI7n2Lb2m8jQnCeRUOy9zQiqQ&usqp=CAU"))
//
//        val categoryItemList3: MutableList<Commodity> = ArrayList()
//        categoryItemList3.add(Commodity("Maria Ozawa", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcThcsdrANTmext5iM3tef0GKGGgv2seeWpNPA&usqp=CAU"))
//        categoryItemList3.add(Commodity("Aoi Sora", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSe1_d38bWCsr5GoHupSX82WkjWujntiRiPLQ&usqp=CAU"))
//        categoryItemList3.add(Commodity("Sakura Haruno", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTT2o3vUrZBTiZ4msyyL8MeK6N2UlYkzVEZ_Q&usqp=CAU"))
//        categoryItemList3.add(Commodity("Maria Ozawa", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcThcsdrANTmext5iM3tef0GKGGgv2seeWpNPA&usqp=CAU"))
//        categoryItemList3.add(Commodity("Aoi Sora", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSe1_d38bWCsr5GoHupSX82WkjWujntiRiPLQ&usqp=CAU"))
//        categoryItemList3.add(Commodity("Sakura Haruno", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTT2o3vUrZBTiZ4msyyL8MeK6N2UlYkzVEZ_Q&usqp=CAU"))
//
//        val categoryItemList4: MutableList<Commodity> = ArrayList()
//        categoryItemList4.add(Commodity("Maria Ozawa", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcThcsdrANTmext5iM3tef0GKGGgv2seeWpNPA&usqp=CAU"))
//        categoryItemList4.add(Commodity("Aoi Sora", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSe1_d38bWCsr5GoHupSX82WkjWujntiRiPLQ&usqp=CAU"))
//        categoryItemList4.add(Commodity("Sakura Haruno", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTT2o3vUrZBTiZ4msyyL8MeK6N2UlYkzVEZ_Q&usqp=CAU"))
//        categoryItemList4.add(Commodity("Maria Ozawa", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcThcsdrANTmext5iM3tef0GKGGgv2seeWpNPA&usqp=CAU"))
//        categoryItemList4.add(Commodity("Aoi Sora", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSe1_d38bWCsr5GoHupSX82WkjWujntiRiPLQ&usqp=CAU"))
//        categoryItemList4.add(Commodity("Sakura Haruno", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTT2o3vUrZBTiZ4msyyL8MeK6N2UlYkzVEZ_Q&usqp=CAU"))
//
//        val categoryItemList5: MutableList<Commodity> = ArrayList()
//        categoryItemList5.add(Commodity("Maria Ozawa", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcThcsdrANTmext5iM3tef0GKGGgv2seeWpNPA&usqp=CAU"))
//        categoryItemList5.add(Commodity("Aoi Sora", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSe1_d38bWCsr5GoHupSX82WkjWujntiRiPLQ&usqp=CAU"))
//        categoryItemList5.add(Commodity("Sakura Haruno", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTT2o3vUrZBTiZ4msyyL8MeK6N2UlYkzVEZ_Q&usqp=CAU"))
//        categoryItemList5.add(Commodity("Maria Ozawa", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcThcsdrANTmext5iM3tef0GKGGgv2seeWpNPA&usqp=CAU"))
//        categoryItemList5.add(Commodity("Aoi Sora", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSe1_d38bWCsr5GoHupSX82WkjWujntiRiPLQ&usqp=CAU"))
//        categoryItemList5.add(Commodity("Sakura Haruno", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTT2o3vUrZBTiZ4msyyL8MeK6N2UlYkzVEZ_Q&usqp=CAU"))




//        allCategoryList.add(AllCategorizedCommodity("Groceries", categoryItemList))
//        allCategoryList.add(AllCategorizedCommodity("Most Viewed", categoryItemList2))
//        allCategoryList.add(AllCategorizedCommodity("Snacks", categoryItemList3))
//        allCategoryList.add(AllCategorizedCommodity("Category 4th", categoryItemList4))
//        allCategoryList.add(AllCategorizedCommodity("Category 5th", categoryItemList5))
//        setMainCategoryRecycler(allCategoryList)

        return objectView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoryAmount = 5

        val allCategoryList: MutableList<AllCategorizedCommodity> = ArrayList()
        lifecycleScope.launchWhenResumed {
            val response = try {
                apolloClient.query(GetCommodityQuery(page = 1,limit = 100)).await()
            }catch (e: ApolloException){
                Log.d("CommodityList", "Failure", e)
                null
            }

            val commodities = response?.data?.comodities
            if(commodities != null && !response.hasErrors()) {
                view.findViewById<ProgressBar>(R.id.commodity_progress_bar).visibility = View.GONE

                var page = 2
                val limit = commodities.nodes.size / categoryAmount
                var counter = 0
                for (i in 0 until categoryAmount) {
                    val offset = limit * (page - 1)
                    val categoryItemList: MutableList<Commodity> = ArrayList()

                    for(j in counter until offset){
                        Log.i("images",commodities.nodes[j].image[0].toString())
                        categoryItemList.add(Commodity(
                            commodities.nodes[j].name,commodities.nodes[j].image[0].toString()
                        ))
                    }
                    counter = offset
                    page++
                    allCategoryList.add(AllCategorizedCommodity("Groceries", categoryItemList))
                }

                setMainCategoryRecycler(allCategoryList)
            }
        }
    }

    private fun setMainCategoryRecycler(allCategoryList: List<AllCategorizedCommodity>) {

        mainCategoryRecycler = objectView.findViewById(R.id.rvMain)

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        mainCategoryRecycler.setLayoutManager(layoutManager)

        mainRecyclerAdapter = AllCategorizedCommodityRecycleViewAdapter(requireActivity(), allCategoryList)
        mainCategoryRecycler.setAdapter(mainRecyclerAdapter)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            CommodityFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}