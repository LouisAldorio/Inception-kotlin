package com.example.inception.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.inception.GetCommodityByCategoryQuery
import com.example.inception.R
import com.example.inception.utils.Capitalizer
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.commodity_item_layout.view.*
import kotlinx.android.synthetic.main.commodity_more_item_layout.view.*

class CommodityGridViewAdapter(private val context: Context,private val commodityList : List<GetCommodityByCategoryQuery.Comodities_by_category>) : BaseAdapter() {

    val cap = Capitalizer()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val commodity = commodityList[position]

        var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var commodityView = inflator.inflate(R.layout.commodity_more_item_layout, null)
        Picasso.get().load(commodity.image[commodity.image.size - 1]!!).into(commodityView.commodity_more_image)
        commodityView.commodity_more_name.text = cap.Capitalize(commodity.name)

        return commodityView
    }

    override fun getItem(position: Int): Any {
        return commodityList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return commodityList.size
    }
}