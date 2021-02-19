package com.example.inception.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.inception.GetDistributorQuery
import com.example.inception.R
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.distributor_wanted_item_layout.view.*

class DistributorWantedItemsRecycleViewAdaptor(val wantedItems: List<String>) : RecyclerView.Adapter<WantedItemHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WantedItemHolder {
        return WantedItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.distributor_wanted_item_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return wantedItems.size
    }

    override fun onBindViewHolder(holder: WantedItemHolder, position: Int) {
        holder.item.text = wantedItems[position]
    }

}

class WantedItemHolder(view: View) : RecyclerView.ViewHolder(view){
    var item : Chip

    init {
        item = view.wanted_item
    }
}