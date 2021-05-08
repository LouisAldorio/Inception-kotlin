package com.example.inception.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.example.inception.CustomLayoutManager.CustomAutoScrollCenterZoomLayoutManager
import com.example.inception.R
import com.example.inception.activity.DetailPage
import com.example.inception.adaptor.ImageCarouselAdaptor
import com.example.inception.constant.DETAIL_EXTRA
import com.example.inception.data.Commodity
import com.example.inception.utils.Capitalizer
import com.example.inception.utils.DownloadImageAndSaveToStorage
import com.example.inception.utils.DownloadImageAndSaveToInternalStorage
import com.example.inception.utils.ImageZoomer
import kotlinx.android.synthetic.main.fragment_commodity_detail.*
import kotlinx.android.synthetic.main.fragment_commodity_detail.view.*
import java.lang.ref.WeakReference
import java.util.*


class CommodityDetailFragment : Fragment() {
    private var myActivity: DetailPage? = null
    val zoomer = ImageZoomer()
    val cap = Capitalizer()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        zoomer.shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
        // Inflate the layout for this fragment
        var objecView = inflater.inflate(R.layout.fragment_commodity_detail, container, false)
        var Commodity = activity?.intent?.getParcelableExtra<Commodity>(DETAIL_EXTRA)
        objecView.detail_commodity_name.text = cap.Capitalize(Commodity?.name ?: "")

        return objecView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myActivity = activity as DetailPage
        var Commodity = activity?.intent?.getParcelableExtra<Commodity>(DETAIL_EXTRA)
        val adaptor = ImageCarouselAdaptor(requireContext(), Commodity!!.image) { position, imageView ->
            activity?.let { ac ->
                zoomer.zoomImageFromThumb(
                    ac,
                    imageView,
                    Commodity!!.image[position]!!,
                    view.container,
                    view.expanded_image,
                    null
                )
            }
        }

        val layoutManager = CustomAutoScrollCenterZoomLayoutManager(activity, LinearLayoutManager.HORIZONTAL, true)
        layoutManager.stackFromEnd = true

        //auto center views
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(view.image_carousel_rv)
        view.image_carousel_rv.layoutManager = layoutManager
        view.image_carousel_rv.adapter = adaptor

        //auto scrolling
        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if ((view.image_carousel_rv.layoutManager as LinearLayoutManager).findLastVisibleItemPosition() == 0) {
                    view.image_carousel_rv.smoothScrollToPosition(Commodity!!.image.size - 1)
                }else{
                    view.image_carousel_rv.smoothScrollToPosition((view.image_carousel_rv.layoutManager as LinearLayoutManager).findLastVisibleItemPosition() - 1)
                }

            }
        }, 1000, (3000..6000).random().toLong())

        //download
        download_commodity_image.setOnClickListener {
            val position = layoutManager.findFirstVisibleItemPosition()

            DownloadImageAndSaveToStorage(requireContext(), WeakReference(requireContext()).get()?.filesDir).execute(Commodity!!.image[position]!!)
        }

        save_to_external.setOnClickListener {
            if(isExternalStorageReadable()){
                val position = layoutManager.findFirstVisibleItemPosition()
                DownloadImageAndSaveToStorage(requireContext(),requireContext().getExternalFilesDir("Inception")).execute(Commodity!!.image[position]!!)
            }else{
                Toast.makeText(requireContext(),"Permission Needed to Access External Storage",Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun isExternalStorageReadable(): Boolean{
        if(ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                777)
        }
        var state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
        {
            return true
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            777 -> {
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    Toast.makeText(requireContext(), "Access Granted, Try to save now!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}