package com.example.inception.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.inception.R
import com.example.inception.data.MediaLoaderData
import com.example.inception.utils.ImageZoomer
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.commodity_item_layout.view.*
import kotlinx.android.synthetic.main.image_carousel.view.*

class RecentlyAddedImageAdapter(context: Context,private val imageURIs : List<MediaLoaderData>,
                                private val callback: (position: Int, thumbView: View) -> Unit) : RecyclerView.Adapter<RecentlyAddedImageHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentlyAddedImageHolder {
        return RecentlyAddedImageHolder(LayoutInflater.from(parent.context).inflate(R.layout.commodity_item_layout,parent,false))

    }

    override fun getItemCount(): Int {
        return imageURIs.size
    }

    override fun onBindViewHolder(holder: RecentlyAddedImageHolder, position: Int) {
        holder.bindMedia(imageURIs[position])
        holder.itemView.imgHeroes.setOnClickListener {
            callback(position, holder.itemView.imgHeroes)
        }
    }
}

class RecentlyAddedImageHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val image = view.imgHeroes
    fun bindMedia(tmp: MediaLoaderData) {
        Picasso.get().load(tmp.uri).into(image)
    }
}