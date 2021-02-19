package com.example.inception.adaptor

import agency.tango.android.avatarview.views.AvatarView
import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inception.GetDistributorQuery
import com.example.inception.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.distributor_item_layout.view.*

class DistributorRecycleViewAdaptor(private val context: Context,val distributors: List<GetDistributorQuery.Users_by_role>): RecyclerView.Adapter<DistributorHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DistributorHolder {
        return DistributorHolder(LayoutInflater.from(parent.context).inflate(R.layout.distributor_item_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return distributors.size
    }

    override fun onBindViewHolder(holder: DistributorHolder, position: Int) {
        Picasso.get().load(distributors[position].profile_image).into(holder.profileImage)
        holder.username.text = distributors[position].username

        //bind the wanted items
        val wantedItemsAdapter = DistributorWantedItemsRecycleViewAdaptor(distributors[position].looking_for)
        holder.wantedItemRecycler.adapter = wantedItemsAdapter
        holder.wantedItemRecycler.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
    }
}

class DistributorHolder(view: View) : RecyclerView.ViewHolder(view){
    var profileImage : AvatarView
    var username : TextView
    var wantedItemRecycler : RecyclerView

    init {

        profileImage = view.profile_image_distributor
        username = view.username_distributor
        wantedItemRecycler = view.rv_distributor_wanted_items
    }
}