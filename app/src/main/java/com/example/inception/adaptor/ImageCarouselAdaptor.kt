package com.example.inception.adaptor

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.inception.R
import com.example.inception.`interface`.RecycleViewFragmentInterface
import com.example.inception.data.Commodity
import com.example.inception.fragment.CommodityDetailFragment
import com.example.inception.utils.ImageZoomer
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.commodity_item_layout.view.*
import kotlinx.android.synthetic.main.fragment_commodity_detail.view.*
import kotlinx.android.synthetic.main.image_carousel.view.*

class ImageCarouselAdaptor(
    var mContext: Context,
    private val images: List<String?>,
    private val callback: (position: Int, thumbView: View) -> Unit) : RecyclerView.Adapter<ImageHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        return ImageHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.image_carousel, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.itemView.commodity_image.clipToOutline = true
        holder.bindImage(images[position]!!)
//        holder.itemView.commodity_image.resume()
        holder.itemView.commodity_image.setOnClickListener {
            callback(position, holder.itemView.commodity_image)
        }
    }
}

class ImageHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val img = view.commodity_image

    fun bindImage(image: String) {
        Picasso.get().load(image)
            .error(R.drawable.ic_hotel_supplier).into(img, object : com.squareup.picasso.Callback {
                override fun onSuccess() {
                    //set animations here
                }

                override fun onError(e: java.lang.Exception?) {
                    //do smth when there is picture loading error
                    Log.i("image load error", e.toString())
                }
            })
    }
}