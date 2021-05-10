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

//berikut gridview adapter yang akan membantu kita dalam merender file dari external maupun internal storage
class BookMarkGridViewAdapter(private val context: Context, private val bookmarkedImageList : List<String>,
                              private val callback: (position: Int, thumbView: View) -> Unit) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        //pada function getView pertama kita memerlukan inflator untuk menginflate view
        var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        //kita inflate layout item view nya yang akan menampung single item dari setiap elemen didalam array
        var commodityView = inflator.inflate(R.layout.commodity_item_layout, null)

        //disini kita menggunakan fungsi setImageDrawable untuk memasukkan file drawble kedalam image view
        //file drawable akan dibuat secara langsung menggunakan class Drawable, melalui method createFromPath
        //method ini akan membantu kita mengkonversi path yang diberikan menjadi sebuah objek drawble yang nantinya membantu kita
        //melemparnya kedalam image view
        commodityView.imgHeroes.setImageDrawable(Drawable.createFromPath(bookmarkedImageList[position]))

        //ketika objek view nya di click , masukkan callback function yang akan melakuakn zoomming
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