package com.example.inception.activity

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.inception.R
import com.example.inception.adaptor.RegistrationPagerAdaptor
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        val mAdapter = RegistrationPagerAdaptor(this)
        myPager.adapter = mAdapter

        TabLayoutMediator(tab_layout,myPager){tab,pos ->
            when(pos) {
                0 -> tab.setText("REGISTER")
                1 -> {
                    tab.setText("LOGIN")
                }
            }
        }.attach()

//        myPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                Log.d("Masuk", "Masuk onPageSelected pak eko")
//                super.onPageSelected(position)
//                val view = mAdapter.getViewAtPosition(position) ?: return
//                updatePagerHeightForChild(view, myPager)
//            }
//
//            fun updatePagerHeightForChild(view: View, pager: ViewPager2) {
//                Log.d("Masuk", "Masuk updatePagerHeightForChild pak eko")
//                view.post {
//                    val wMeasureSpec =
//                        View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
//                    val hMeasureSpec =
//                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
//                    view.measure(wMeasureSpec, hMeasureSpec)
//
//                    if (pager.layoutParams.height != view.measuredHeight) {
//                        pager.layoutParams = (pager.layoutParams)
//                            .also { lp ->
//                                lp.height = view.measuredHeight
//                            }
//                    }
//                }
//            }
//        })
    }
}