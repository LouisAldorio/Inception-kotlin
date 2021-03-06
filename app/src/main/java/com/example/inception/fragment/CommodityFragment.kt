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
import kotlinx.coroutines.launch
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
        lifecycleScope.launch {
            val response = try {
                apolloClient(requireContext()).query(GetCommodityQuery(page = 1, limit = 100))
                    .await()


            } catch (e: ApolloException) {
                Log.d("CommodityList", "Failure", e)
                null
            }
            this@CommodityFragment.view?.let { arrange(it, response) }
        }
    }

    private fun arrange(view: View, response: Response<GetCommodityQuery.Data>?) {
        val categoryAmount = 5

        val allCategoryList: MutableList<AllCategorizedCommodity> = ArrayList()
        lifecycleScope.launchWhenResumed {


            val commodities = response?.data?.comodities
            if (commodities != null && !response.hasErrors()) {
                view.findViewById<ProgressBar>(R.id.commodity_progress_bar).visibility = View.GONE

                var page = 2
                val limit = commodities.nodes.size / categoryAmount
                var counter = 0
                for (i in 0 until categoryAmount) {
                    val offset = limit * (page - 1)
                    val categoryItemList: MutableList<Commodity> = ArrayList()

                    for (j in counter until offset) {
                        Log.i("images", commodities.nodes[j].image[0].toString())
                        categoryItemList.add(
                            Commodity(
                                commodities.nodes[j].name, commodities.nodes[j].image[0].toString()
                            )
                        )
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

        mainRecyclerAdapter =
            AllCategorizedCommodityRecycleViewAdapter(requireActivity(), allCategoryList)
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