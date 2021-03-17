package com.example.inception.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.example.inception.GetCommodityQuery
//import com.example.inception.GetSupplierQuery
import com.example.inception.R
import com.example.inception.`interface`.RecycleViewFragmentInterface
import com.example.inception.adaptor.AllCategorizedCommodityRecycleViewAdapter
import com.example.inception.api.apolloClient
import com.example.inception.data.AllCategorizedCommodity
import com.example.inception.data.Commodity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList


class CommodityFragment : Fragment() {

    lateinit var mainCategoryRecycler: RecyclerView

    var mainRecyclerAdapter: AllCategorizedCommodityRecycleViewAdapter? = null

    lateinit var objectView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        objectView = inflater.inflate(R.layout.fragment_commodity, container, false)
        return objectView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch(Dispatchers.Main) {
            val response = try {
                withContext(Dispatchers.IO) {
                    apolloClient(requireContext()).query(GetCommodityQuery(page = 1, limit = 10))
                        .await()
                }
            } catch (e: ApolloException) {
                Log.d("CommodityList", "Failure", e)
                null
            }
            this@CommodityFragment.view?.let { arrange(it, response) }
        }
    }

    private fun arrange(view: View, response: Response<GetCommodityQuery.Data>?) {
        val allCategoryList: MutableList<AllCategorizedCommodity> = ArrayList()
        val commodities = response?.data?.comoditiesByCategory
        if (commodities != null && !response.hasErrors()) {
            view.findViewById<ProgressBar>(R.id.commodity_progress_bar).visibility = View.GONE
            for(item in commodities){
                val categoryItemList: MutableList<Commodity> = ArrayList()
                for (commodity in item.nodes){
                    categoryItemList.add(
                        Commodity(commodity.name,commodity.image)
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