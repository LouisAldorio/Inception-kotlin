package com.example.inception.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.inception.R
import kotlinx.android.synthetic.main.activity_landing_page.*

import com.example.inception.fragment.*
import com.etebarian.meowbottomnavigation.MeowBottomNavigation


class LandingPage : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        addFragment(CommodityFragment.newInstance())
        bottomNavigation.show(0)
        bottomNavigation.add(MeowBottomNavigation.Model(0,R.drawable.ic_commodity))
        bottomNavigation.add(MeowBottomNavigation.Model(1,R.drawable.ic_hotel_supplier))
        bottomNavigation.add(MeowBottomNavigation.Model(2,R.drawable.ic_calendar))
        bottomNavigation.add(MeowBottomNavigation.Model(3,R.drawable.ic_supplies))
        bottomNavigation.add(MeowBottomNavigation.Model(4,R.drawable.ic_profile_user))

        bottomNavigation.setOnClickMenuListener {
            when(it.id){
                0 -> {
                    replaceFragment(CommodityFragment.newInstance())
                }
                1 -> {
                    replaceFragment(ScheduleFragment.newInstance())
                }
                2 -> {
                    replaceFragment(SupplierFragment.newInstance())
                }
                3 -> {
                    replaceFragment(DistributorFragment.newInstance())
                }
                4 -> {
                    replaceFragment(ProfileFragment.newInstance())
                }
                else -> {
                    replaceFragment(CommodityFragment.newInstance())
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        System.exit(0)
    }

    private fun replaceFragment(fragment:Fragment){
        val fragmentTransition = supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
        fragmentTransition.replace(R.id.fragmentContainer,fragment).addToBackStack(Fragment::class.java.simpleName).commit()
    }

    private fun addFragment(fragment:Fragment){
        val fragmentTransition = supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
        fragmentTransition.add(R.id.fragmentContainer,fragment).addToBackStack(Fragment::class.java.simpleName).commit()
    }
}