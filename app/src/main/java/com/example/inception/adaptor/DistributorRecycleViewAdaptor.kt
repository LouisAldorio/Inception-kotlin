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
//import com.example.inception.GetDistributorQuery
import com.example.inception.R
import com.example.inception.utils.Capitalizer
import com.flaviofaria.kenburnsview.KenBurnsView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.distributor_item_layout.view.*

class DistributorRecycleViewAdaptor(private val context: Context,private val distributors : List<GetDistributorQuery.Users_by_role>): RecyclerView.Adapter<DistributorHolder>() {
    val cap = Capitalizer()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DistributorHolder {
        return DistributorHolder(LayoutInflater.from(parent.context).inflate(R.layout.distributor_item_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return distributors.size
    }

    override fun onBindViewHolder(holder: DistributorHolder, position: Int) {
        Picasso.get().load(distributors[position].image.link).into(holder.profileImage)
        holder.username.text = cap.Capitalize(distributors[position].username)

        //bind the wanted items
        val wantedItemsAdapter = DistributorWantedItemsRecycleViewAdaptor(distributors[position].looking_for as List<String>)
        holder.wantedItemRecycler.adapter = wantedItemsAdapter
        holder.wantedItemRecycler.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
    }
}

class DistributorHolder(view: View) : RecyclerView.ViewHolder(view){
    var profileImage : KenBurnsView
    var username : TextView
    var wantedItemRecycler : RecyclerView

    init {

        profileImage = view.profile_image_distributor
        username = view.username_distributor
        wantedItemRecycler = view.rv_distributor_wanted_items
    }
}