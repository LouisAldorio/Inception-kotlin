package com.example.inception.activity

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.inception.R
import com.example.inception.adaptor.RegistrationPagerAdaptor
import com.example.inception.constant.ALARM_MANAGER_CHANNELID
import com.example.inception.constant.EXTRA_PESAN
import com.example.inception.internalreceiver.ScheduledAlarmReceiver
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

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

    }



}