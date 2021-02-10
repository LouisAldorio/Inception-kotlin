package com.example.inception.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        Picasso.get().load(hero.image).into(imgHero)
    }
}