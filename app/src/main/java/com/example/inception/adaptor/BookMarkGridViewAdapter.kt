package com.example.inception.adaptor

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.inception.GetCommodityByCategoryQuery
import com.example.inception.R
import kotlinx.android.synthetic.main.commodity_item_layout.view.*

class BookMarkGridViewAdapter(private val context: Context, private val bookmarkedImageList : List<String>,
                              private val callback: (position: Int, thumbView: View) -> Unit) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var commodityView = inflator.inflate(R.layout.commodity_item_layout, null)

        commodityView.imgHeroes.setImageDrawable(Drawable.createFromPath(bookmarkedImageList[position]))
        commodityView.imgHeroes.setOnClickListener {
            callback(position,commodityView.imgHeroes)
        }

        return commodityView
    }

    override fun getItem(position: Int): Any {
        return bookmarkedImageList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return bookmarkedImageList.size
    }

}