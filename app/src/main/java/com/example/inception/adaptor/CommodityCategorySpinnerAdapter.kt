package com.example.inception.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.inception.GetCategoryQuery
import com.example.inception.R
import com.example.inception.utils.Capitalizer

class CommodityCategorySpinnerAdapter( private var context: Context, private var list: List<GetCategoryQuery.Category_list>) : BaseAdapter() {

    val cap = Capitalizer()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null) {
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.spinner_commodity_category_item_layout, parent, false)
        }

        val textView = view!!.findViewById<TextView>(R.id.category)
        textView.text = cap.Capitalize(list[position].name)

        return textView
    }

    override fun getItem(position: Int): Any? {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return list[position].id.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }
}