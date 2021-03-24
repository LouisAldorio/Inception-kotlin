package com.example.inception.adaptor

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.example.inception.GetCommodityByCategoryQuery
import com.example.inception.R
import com.example.inception.activity.DetailPage
import com.example.inception.constant.CONTEXT_EXTRA
import com.example.inception.constant.DETAIL_EXTRA
import com.example.inception.data.Commodity
import com.example.inception.data.CommodityUser
import com.example.inception.utils.Capitalizer
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.commodity_item_layout.view.*
import kotlinx.android.synthetic.main.commodity_more_item_layout.view.*

class CommodityGridViewAdapter(private val context: Context,private val commodityList : List<GetCommodityByCategoryQuery.Comodities_by_category>) : BaseAdapter() {

    val cap = Capitalizer()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val commodity = commodityList[position]

        var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var commodityView = inflator.inflate(R.layout.commodity_more_item_layout, null)
        Picasso.get().load(commodity.image[commodity.image.size - 1]!!).into(commodityView.commodity_more_image)
        commodityView.commodity_more_name.text = cap.Capitalize(commodity.name)

        commodityView.item.setOnClickListener {
            var intentdetail = Intent(context, DetailPage::class.java)
            var commodity = Commodity(
                commodity.name,
                commodity.image,
                commodity.unit_price.toString(),
                commodity.unit_type,
                commodity.min_purchase.toString(),
                CommodityUser(
                    commodity.user.username,
                    commodity.user.email,
                    commodity.user.whatsapp,
                    commodity.user.image.link!!
                )
            )
            intentdetail.putExtra(DETAIL_EXTRA,commodity)
            intentdetail.putExtra(CONTEXT_EXTRA,"Commodity")

            val pair1: Pair<View, String> = Pair.create(
                commodityView.commodity_more_image as View?,
                commodityView.commodity_more_image.getTransitionName()
            )

            val pair2: Pair<View, String> = Pair.create(
                commodityView.commodity_more_name as View?,
                commodityView.commodity_more_name.getTransitionName()
            )

            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    context as Activity,
                    pair1,
                    pair2
                )
            context.startActivity(intentdetail, optionsCompat.toBundle())
        }

        return commodityView
    }

    override fun getItem(position: Int): Any {
        return commodityList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return commodityList.size
    }
}