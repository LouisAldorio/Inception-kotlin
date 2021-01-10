package com.example.inception

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        myPager.adapter = RegistrationPagerAdaptor(this)

        TabLayoutMediator(tab_layout,myPager){tab,pos ->
            when(pos) {
                0 -> tab.setText("Register")
                1 -> {
                    tab.setText("Login")
                }
            }
        }.attach()
    }
}