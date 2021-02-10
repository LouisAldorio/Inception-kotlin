package com.example.inception.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inception.R
import com.example.inception.data.AllCategorizedCommodity
import com.example.inception.data.Commodity

class AllCategorizedCommodityRecycleViewAdapter(private val context: Context, private val categorizedCommodityItemList: List<AllCategorizedCommodity>) : RecyclerView.Adapter<AllCategorizedCommodityHolder>() {
    var pos = categorizedCommodityItemList.size/2;

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
        holder.categoryTitle.text = categorizedCommodityItemList[position].categoryTitle
        setCatItemRecycler(holder.itemRecycler, categorizedCommodityItemList[position].categoryCommodityList)
    }


    private fun setCatItemRecycler(recyclerView: RecyclerView, categoryItemList: List<Commodity>) {
        val itemRecyclerAdapter = CommodityRecycleViewAdapter(categoryItemList)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recyclerView.adapter = itemRecyclerAdapter

        //make the start display position exact from middle
        pos = categoryItemList.size/2

        //make left array functioning
        objectView.leftArrow.setOnClickListener(View.OnClickListener {
            if ((recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition() > 0) {
                recyclerView.smoothScrollToPosition((recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition() - 1)
            } else {
                recyclerView.smoothScrollToPosition(0)
            }
        })

        //make the right array functioning
        objectView.rightArrow.setOnClickListener(View.OnClickListener {
            recyclerView.smoothScrollToPosition((recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition() + 1)
        })

        //determine first position
        recyclerView.scrollToPosition(pos)
    }
}

class AllCategorizedCommodityHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var categoryTitle: TextView
    var itemRecycler: RecyclerView
    var leftArrow : ImageView
    var rightArrow : ImageView

    init {
        categoryTitle = itemView.findViewById(R.id.cat_title)
        itemRecycler = itemView.findViewById(R.id.item_recycler)
        leftArrow = itemView.findViewById(R.id.img_LeftScroll)
        rightArrow = itemView.findViewById(R.id.img_right_scroll)
    }
}