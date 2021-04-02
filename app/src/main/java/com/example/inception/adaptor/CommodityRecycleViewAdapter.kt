package com.example.inception.adaptor

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.inception.GetCommodityQuery
import com.example.inception.R
import com.example.inception.activity.DetailPage
import com.example.inception.constant.CONTEXT_EXTRA
import com.example.inception.constant.DETAIL_EXTRA
import com.example.inception.constant.NOTIFICATION_CONTEXT
import com.example.inception.data.Commodity
import com.example.inception.data.CommodityUser
import com.squareup.picasso.Callback
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

            var intentdetail = Intent(mContext,DetailPage::class.java)
            var commodity = Commodity(
                commodities[position].name,
                commodities[position].image,
                commodities[position].unit_price.toString(),
                commodities[position].unit_type,
                commodities[position].min_purchase.toString(),
                CommodityUser(
                    commodities[position].user.username,
                    commodities[position].user.email,
                    commodities[position].user.whatsapp,
                    commodities[position].user.avatar!!
                ),""
            )
            intentdetail.putExtra(DETAIL_EXTRA,commodity)
            intentdetail.putExtra(CONTEXT_EXTRA,"Commodity")

            val pair1: Pair<View, String> = Pair.create(
                holder.itemView.imgHeroes as View?,
                holder.itemView.imgHeroes.getTransitionName()
            )

            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    mContext as Activity,
                    pair1
                )
            mContext.startActivity(intentdetail, optionsCompat.toBundle())
        }
    }
}

class CommodityHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val imgHero = view.imgHeroes

    fun bindCommodity(commodity: Commodity) {


        Picasso.get().load(commodity.image[commodity.image.size - 1])
            .error(R.drawable.ic_hotel_supplier).into(imgHero, object: Callback {
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