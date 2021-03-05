package com.example.inception.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.ListFragment
import com.example.inception.R
import kotlinx.android.synthetic.main.activity_landing_page.*

import com.example.inception.fragment.*
import com.etebarian.meowbottomnavigation.MeowBottomNavigation


class LandingPage : AppCompatActivity() {

    var fragments = mutableListOf<Fragment>(CommodityFragment(),SupplierFragment(),ScheduleFragment(),DistributorFragment(),ProfileFragment())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN



        addFragment(fragments[0])
        bottomNavigation.show(0)
        bottomNavigation.add(MeowBottomNavigation.Model(0,R.drawable.ic_commodity))
        bottomNavigation.add(MeowBottomNavigation.Model(1,R.drawable.ic_hotel_supplier))
        bottomNavigation.add(MeowBottomNavigation.Model(2,R.drawable.ic_calendar))
        bottomNavigation.add(MeowBottomNavigation.Model(3,R.drawable.ic_supplies))
        bottomNavigation.add(MeowBottomNavigation.Model(4,R.drawable.ic_profile_user))

        bottomNavigation.setOnClickMenuListener {
//            when(it.id){
//                0 -> {
//                    replaceFragment(CommodityFragment.newInstance())
//                }
//                1 -> {
//                    replaceFragment(SupplierFragment.newInstance())
//                }
//                2 -> {
//                    replaceFragment(ScheduleFragment.newInstance())
//                }
//                3 -> {
//                    replaceFragment(DistributorFragment.newInstance())
//                }
//                4 -> {
//                    replaceFragment(ProfileFragment.newInstance())
//                }
//                else -> {
//                    replaceFragment(CommodityFragment.newInstance())
//                }
//            }
            replaceFragment(fragments[it.id])
        }
    }

    override fun onBackPressed() {
        finishAffinity()
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