package com.example.inception.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.example.inception.CustomLayoutManager.CustomAutoScrollCenterZoomLayoutManager
import com.example.inception.R
import com.example.inception.`interface`.RecycleViewFragmentInterface
import com.example.inception.activity.DetailPage
import com.example.inception.adaptor.ImageCarouselAdaptor
import com.example.inception.constant.DETAIL_EXTRA
import com.example.inception.data.Commodity
import com.example.inception.utils.ImageZoomer
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_commodity_detail.*
import kotlinx.android.synthetic.main.fragment_commodity_detail.view.*
import kotlinx.android.synthetic.main.image_carousel.view.*

class CommodityDetailFragment : Fragment() {

    private var pos: Int = 0
    private lateinit var thumbViews: View
    private var myActivity: DetailPage? = null
    val zoomer = ImageZoomer()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        zoomer.shortAnimationDuration = resources.getInteger(android.R.integer.config_mediumAnimTime)
        // Inflate the layout for this fragment
        var objecView = inflater.inflate(R.layout.fragment_commodity_detail, container, false)
        var Commodity = activity?.intent?.getParcelableExtra<Commodity>(DETAIL_EXTRA)
        objecView.detail_commodity_name.text = Commodity?.name

        return objecView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myActivity = activity as DetailPage
        var Commodity = activity?.intent?.getParcelableExtra<Commodity>(DETAIL_EXTRA)
        val adaptor = ImageCarouselAdaptor(requireContext(), Commodity!!.image) { t1, t2 ->
            activity?.let { ac ->
                zoomer.zoomImageFromThumb(
                    ac,
                    image_carousel_rv,
                    Commodity!!.image[t1]!!,
                    view.container,
                    view.expanded_image
                )
            }
        }

        val layoutManager =
            CustomAutoScrollCenterZoomLayoutManager(activity, LinearLayoutManager.HORIZONTAL, true)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true

        //auto center views
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(view.image_carousel_rv)
        view.image_carousel_rv.layoutManager = layoutManager
        view.image_carousel_rv.adapter = adaptor
    }

}