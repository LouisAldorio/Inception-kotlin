package com.example.inception.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inception.CustomLayoutManager.SpeedLinearLayoutManager
import com.example.inception.GetCommodityQuery
import com.example.inception.R
import com.example.inception.utils.Capitalizer
import java.util.*


class AllCategorizedCommodityRecycleViewAdapter(private val context: Context, private val categorizedCommodityItemList: List<GetCommodityQuery.ComoditiesByCategory>) : RecyclerView.Adapter<AllCategorizedCommodityHolder>() {
    var pos = categorizedCommodityItemList.size/2;

    val cap = Capitalizer()

    lateinit var objectView : AllCategorizedCommodityHolder

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AllCategorizedCommodityHolder {

        objectView = AllCategorizedCommodityHolder(LayoutInflater.from(context).inflate(R.layout.categorized_commodity_container, parent, false))
        return objectView
    }

    override fun getItemCount(): Int {
        return categorizedCommodityItemList.size
    }

    override fun onBindViewHolder(holder: AllCategorizedCommodityHolder, position: Int) {
        holder.categoryTitle.text = cap.Capitalize(categorizedCommodityItemList[position].category.name)
        setCatItemRecycler(holder.itemRecycler, categorizedCommodityItemList[position].nodes)
    }


    private fun setCatItemRecycler(recyclerView: RecyclerView, categoryItemList: List<GetCommodityQuery.Node>) {
        val itemRecyclerAdapter = CommodityRecycleViewAdapter(context,categoryItemList)
        recyclerView.layoutManager = SpeedLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = itemRecyclerAdapter

        //make the start display position exact from middle
        pos = categoryItemList.size/2

        //determine first position
        recyclerView.scrollToPosition(pos)

        //auto scrolling
        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if ((recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition() == (categoryItemList.size - 1)) {
                    recyclerView.smoothScrollToPosition(0)
                }else{
                    recyclerView.smoothScrollToPosition((recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition() + 1)
                }

            }
        }, 0, (3000..6000).random().toLong())
    }
}

class AllCategorizedCommodityHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var categoryTitle: TextView
    var itemRecycler: RecyclerView

    init {
        categoryTitle = itemView.findViewById(R.id.cat_title)
        itemRecycler = itemView.findViewById(R.id.item_recycler)
    }
}