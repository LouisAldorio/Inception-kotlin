package com.example.inception.adaptor

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.inception.R
import com.example.inception.data.Commodity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.commodity_item_layout.view.*

class CommodityRecycleViewAdapter(private val commodities: List<Commodity>) : RecyclerView.Adapter<CommodityHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommodityHolder {
        return CommodityHolder(LayoutInflater.from(parent.context).inflate(R.layout.commodity_item_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return commodities.size
    }

    override fun onBindViewHolder(holder: CommodityHolder, position: Int) {
        holder.bindCommodity(commodities[position])
    }
}

class CommodityHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val imgHero = view.imgHeroes

    fun bindCommodity(hero: Commodity) {


        Picasso.get().load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTT2o3vUrZBTiZ4msyyL8MeK6N2UlYkzVEZ_Q&usqp=CAU")
            .error(R.drawable.ic_calendar).fit().into(imgHero, object: com.squareup.picasso.Callback {
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