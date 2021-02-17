package com.example.inception.adaptor

import agency.tango.android.avatarview.views.AvatarView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.inception.GetSupplierQuery
import com.example.inception.R
import com.squareup.picasso.Picasso

class SupplierRecycleViewAdaptor (val suppliers: List<GetSupplierQuery.Users_by_role>) : RecyclerView.Adapter<SupplierHolder>(){
    var onItemClicked: ((GetSupplierQuery.Users_by_role) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupplierHolder {
        return SupplierHolder(LayoutInflater.from(parent.context).inflate(R.layout.supplier_item_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return suppliers.size
    }

    override fun onBindViewHolder(holder: SupplierHolder, position: Int) {
        Picasso.get().load(suppliers[position].profile_image).into(holder.profileImage)
        holder.username.text = suppliers[position].username

        holder.itemView.setOnClickListener {
            onItemClicked?.invoke(suppliers[position])
        }
    }
}

class SupplierHolder(view: View) : RecyclerView.ViewHolder(view){
    var profileImage : AvatarView
    var username : TextView

    init {
        profileImage = view.findViewById(R.id.profile_image)
        username = view.findViewById(R.id.username)
    }
}