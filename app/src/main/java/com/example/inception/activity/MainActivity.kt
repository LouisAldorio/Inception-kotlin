package com.example.inception.activity

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.inception.R
import com.example.inception.adaptor.RegistrationPagerAdaptor
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*


fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("masuk pak eko", "oncreate")
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

    override fun onPause() {
        Toast.makeText(this, "on pause", Toast.LENGTH_SHORT).show()
        super.onPause()
    }

    override fun onDestroy() {
        Toast.makeText(this, "on destroy", Toast.LENGTH_SHORT).show()
        super.onDestroy()
    }

}