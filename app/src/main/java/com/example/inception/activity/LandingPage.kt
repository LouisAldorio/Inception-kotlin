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

    private val commodityFragment = CommodityFragment()
    private val supplierFragment = SupplierFragment()
    private val scheduleFragment = ScheduleFragment()
    private val distributorFragment = DistributorFragment()
    private val profileFragment = ProfileFragment()
    private val fragmentManager = supportFragmentManager
    private var activeFragment: Fragment = commodityFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        fragmentManager.beginTransaction().apply {
            add(R.id.fragmentContainer, supplierFragment).hide(supplierFragment)
            add(R.id.fragmentContainer, scheduleFragment).hide(scheduleFragment)
            add(R.id.fragmentContainer, distributorFragment).hide(distributorFragment)
            add(R.id.fragmentContainer, profileFragment).hide(profileFragment)
            add(R.id.fragmentContainer, commodityFragment)
        }.commit()

//        addFragment(fragments[0])
        bottomNavigation.show(0)
        bottomNavigation.add(MeowBottomNavigation.Model(0,R.drawable.ic_commodity))
        bottomNavigation.add(MeowBottomNavigation.Model(1,R.drawable.ic_hotel_supplier))
        bottomNavigation.add(MeowBottomNavigation.Model(2,R.drawable.ic_calendar))
        bottomNavigation.add(MeowBottomNavigation.Model(3,R.drawable.ic_supplies))
        bottomNavigation.add(MeowBottomNavigation.Model(4,R.drawable.ic_profile_user))

        bottomNavigation.setOnClickMenuListener {
//            replaceFragment(fragments[it.id])
            when (it.id) {
                0 -> {
                    fragmentManager.beginTransaction().setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out).hide(activeFragment).show(commodityFragment).commit()
                    activeFragment = commodityFragment
                    true
                }
                1 -> {
                    fragmentManager.beginTransaction().setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out).hide(activeFragment).show(supplierFragment).commit()
                    activeFragment = supplierFragment
                    true
                }
                2 -> {
                    fragmentManager.beginTransaction().setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out).hide(activeFragment).show(scheduleFragment).commit()
                    activeFragment = scheduleFragment
                    true
                }
                3 -> {
                    fragmentManager.beginTransaction().setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out).hide(activeFragment).show(distributorFragment).commit()
                    activeFragment = distributorFragment
                    true
                }
                4 -> {
                    fragmentManager.beginTransaction().setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out).hide(activeFragment).show(profileFragment).commit()
                    activeFragment = profileFragment
                    true
                }
                else -> false
            }
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