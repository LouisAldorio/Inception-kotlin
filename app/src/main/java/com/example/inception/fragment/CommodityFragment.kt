package com.example.inception.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inception.R
import com.example.inception.adaptor.CommodityRecycleViewAdapter
import com.example.inception.data.Commodity
import kotlinx.android.synthetic.main.fragment_commodity.view.*


class CommodityFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val listHeroes = listOf(
            Commodity("Maria Ozawa", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcThcsdrANTmext5iM3tef0GKGGgv2seeWpNPA&usqp=CAU"),
            Commodity("Aoi Sora", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSe1_d38bWCsr5GoHupSX82WkjWujntiRiPLQ&usqp=CAU"),
            Commodity("Sakura Haruno", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTT2o3vUrZBTiZ4msyyL8MeK6N2UlYkzVEZ_Q&usqp=CAU"),
            Commodity("Maria Ozawa", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcThcsdrANTmext5iM3tef0GKGGgv2seeWpNPA&usqp=CAU"),
            Commodity("Aoi Sora", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSe1_d38bWCsr5GoHupSX82WkjWujntiRiPLQ&usqp=CAU"),
            Commodity("Sakura Haruno", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTT2o3vUrZBTiZ4msyyL8MeK6N2UlYkzVEZ_Q&usqp=CAU"),
            Commodity("Maria Ozawa", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcThcsdrANTmext5iM3tef0GKGGgv2seeWpNPA&usqp=CAU"),
            Commodity("Aoi Sora", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSe1_d38bWCsr5GoHupSX82WkjWujntiRiPLQ&usqp=CAU"),
            Commodity("Sakura Haruno", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTT2o3vUrZBTiZ4msyyL8MeK6N2UlYkzVEZ_Q&usqp=CAU"),
            Commodity("Maria Ozawa", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcThcsdrANTmext5iM3tef0GKGGgv2seeWpNPA&usqp=CAU"),
            Commodity("Aoi Sora", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSe1_d38bWCsr5GoHupSX82WkjWujntiRiPLQ&usqp=CAU"),
            Commodity("Sakura Haruno", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTT2o3vUrZBTiZ4msyyL8MeK6N2UlYkzVEZ_Q&usqp=CAU"),
            Commodity("Maria Ozawa", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcThcsdrANTmext5iM3tef0GKGGgv2seeWpNPA&usqp=CAU"),
            Commodity("Aoi Sora", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSe1_d38bWCsr5GoHupSX82WkjWujntiRiPLQ&usqp=CAU"),
            Commodity("Sakura Haruno", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTT2o3vUrZBTiZ4msyyL8MeK6N2UlYkzVEZ_Q&usqp=CAU"),
            Commodity("Maria Ozawa", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcThcsdrANTmext5iM3tef0GKGGgv2seeWpNPA&usqp=CAU"),
            Commodity("Aoi Sora", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSe1_d38bWCsr5GoHupSX82WkjWujntiRiPLQ&usqp=CAU"),
            Commodity("Sakura Haruno", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTT2o3vUrZBTiZ4msyyL8MeK6N2UlYkzVEZ_Q&usqp=CAU"),
            Commodity("Maria Ozawa", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcThcsdrANTmext5iM3tef0GKGGgv2seeWpNPA&usqp=CAU"),
            Commodity("Aoi Sora", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSe1_d38bWCsr5GoHupSX82WkjWujntiRiPLQ&usqp=CAU"),
            Commodity("Sakura Haruno", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTT2o3vUrZBTiZ4msyyL8MeK6N2UlYkzVEZ_Q&usqp=CAU"),
            Commodity("Maria Ozawa", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcThcsdrANTmext5iM3tef0GKGGgv2seeWpNPA&usqp=CAU"),
            Commodity("Aoi Sora", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSe1_d38bWCsr5GoHupSX82WkjWujntiRiPLQ&usqp=CAU"),
            Commodity("Sakura Haruno", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTT2o3vUrZBTiZ4msyyL8MeK6N2UlYkzVEZ_Q&usqp=CAU"),
            Commodity("Maria Ozawa", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcThcsdrANTmext5iM3tef0GKGGgv2seeWpNPA&usqp=CAU"),
            Commodity("Aoi Sora", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSe1_d38bWCsr5GoHupSX82WkjWujntiRiPLQ&usqp=CAU"),
            Commodity("Sakura Haruno", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTT2o3vUrZBTiZ4msyyL8MeK6N2UlYkzVEZ_Q&usqp=CAU")
        )

        var objectView = inflater.inflate(R.layout.fragment_commodity, container, false)

        val CommodityAdapter = CommodityRecycleViewAdapter(listHeroes)
        objectView.rvMain.apply {
            layoutManager = GridLayoutManager(activity,4)
            adapter = CommodityAdapter
        }


        return objectView
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            CommodityFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}