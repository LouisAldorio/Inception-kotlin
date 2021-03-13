package com.example.inception.adaptor

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
//import com.example.inception.GetCommodityQuery
import com.example.inception.R
import com.example.inception.activity.DetailPage
import com.example.inception.constant.CONTEXT_EXTRA
import com.example.inception.constant.DETAIL_EXTRA
import com.example.inception.data.Commodity
import com.google.android.material.internal.ContextUtils.getActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.commodity_item_layout.view.*

class CommodityRecycleViewAdapter(var mContext:Context,private val commodities: List<Commodity>) : RecyclerView.Adapter<CommodityHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommodityHolder {
        return CommodityHolder(LayoutInflater.from(parent.context).inflate(R.layout.commodity_item_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return commodities.size
    }

    override fun onBindViewHolder(holder: CommodityHolder, position: Int) {
        holder.bindCommodity(commodities[position])
        holder.itemView.setOnClickListener {

            //memanggil intent explisit dengan membawa object sebagai parcelable
            //membuat intent dengan memasukan activity tujuan
            var intentdetail = Intent(mContext,DetailPage::class.java)
            //membentuk objeck parcelable dari data list yang di click
            var commodity = Commodity(commodities[position].name,commodities[position].image)
            //memasukkan objeck parcelable ke dalam extra agar nantinya dapat diambil di aktivity tujuan
            intentdetail.putExtra(DETAIL_EXTRA,commodity)
            //dikarenakan kami menggunakan fragment pada activity tujuan, maka harus di beritahu, fragment mana yang harus dipanggil
            // case ini fragment detail commodity
            intentdetail.putExtra(CONTEXT_EXTRA,"Commodity")
            //mulai activity tujuan dengan melemparkan intent yang telah dibentuk
            mContext.startActivity(intentdetail)
        }
    }
}

class CommodityHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val imgHero = view.imgHeroes

    fun bindCommodity(commodity: Commodity) {


        Picasso.get().load(commodity.image)
            .error(R.drawable.ic_hotel_supplier).resize(180,170).into(imgHero, object: com.squareup.picasso.Callback {
            override fun onSuccess() {
                //set animations here

            }

            override fun onError(e: java.lang.Exception?) {
                //do smth when there is picture loading error
                Log.i("image load error",e.toString())
            }
        })
    }
}