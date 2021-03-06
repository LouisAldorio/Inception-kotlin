package com.example.inception.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.inception.R
import com.example.inception.constant.DETAIL_EXTRA
import com.example.inception.data.Commodity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_commodity_detail.view.*

class CommodityDetailFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var objecView = inflater.inflate(R.layout.fragment_commodity_detail, container, false)

        //pada fragment detail commodity yang dimana merupakan fragment yang akan di attach ke activity tujuan(detail Activity)
        //ambil parcelable extra yang telah di passing dari activity sebelumnya
        //masukkan data nya ke dalam view yang telah disediakan untuk ditampilkan
        var Commodity = activity?.intent?.getParcelableExtra<Commodity>(DETAIL_EXTRA)

        Picasso.get().load(Commodity?.image)
            .error(R.drawable.ic_hotel_supplier).resize(180,170).into(objecView.detail_commodity_image, object: com.squareup.picasso.Callback {
                override fun onSuccess() {
                    //set animations here

                }

                override fun onError(e: java.lang.Exception?) {
                    //do smth when there is picture loading error
                    Log.i("image load error",e.toString())
                }
            })

        objecView.detail_commodity_name.text = Commodity?.name
        return objecView
    }

}