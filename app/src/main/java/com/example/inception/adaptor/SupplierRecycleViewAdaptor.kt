package com.example.inception.adaptor

import agency.tango.android.avatarview.views.AvatarView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.inception.GetSupplierQuery
//import com.example.inception.GetSupplierQuery
import com.example.inception.R
import com.example.inception.data.Supplier
import com.flaviofaria.kenburnsview.KenBurnsView
import com.squareup.picasso.Picasso

//param =
class SupplierRecycleViewAdaptor (val suppliers: List<Supplier>) : RecyclerView.Adapter<SupplierHolder>(){
    var onItemClicked: ((Supplier) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupplierHolder {
        return SupplierHolder(LayoutInflater.from(parent.context).inflate(R.layout.supplier_item_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return suppliers.size
    }

    override fun onBindViewHolder(holder: SupplierHolder, position: Int) {
        Picasso.get().load(suppliers[position].photoUrl).into(holder.profileImage)
        holder.username.text = suppliers[position].username

        holder.itemView.setOnClickListener {
            onItemClicked?.invoke(suppliers[position])
        }
    }
}

class SupplierHolder(view: View) : RecyclerView.ViewHolder(view){
    var profileImage : KenBurnsView
    var username : TextView

    init {
        profileImage = view.findViewById(R.id.profile_image)
        username = view.findViewById(R.id.username)
    }
}