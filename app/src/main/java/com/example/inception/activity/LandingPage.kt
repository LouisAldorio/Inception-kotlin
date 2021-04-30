package com.example.inception.activity

import android.annotation.TargetApi
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.media.AudioManager
import android.media.SoundPool
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.inception.R
import kotlinx.android.synthetic.main.activity_landing_page.*

import com.example.inception.fragment.*
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.inception.constant.ALARM_MANAGER_CHANNELID
import com.example.inception.constant.EXTRA_PESAN
import com.example.inception.internalreceiver.SMSReceiver
import com.example.inception.internalreceiver.ScheduledAlarmReceiver
import java.util.*

class LandingPage : AppCompatActivity() {

    var sp : SoundPool? = null
    var soundID  : Int = 0

    private val commodityFragment = CommodityFragment()
    private val supplierFragment = SupplierFragment()
    private val scheduleFragment = ScheduleFragment()
    private val distributorFragment = DistributorFragment()
    private val profileFragment = ProfileFragment()

    private val fragmentManager = supportFragmentManager

    private var activeFragment: Fragment = profileFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        fragmentManager.beginTransaction().apply {
            add(R.id.fragmentContainer, supplierFragment).hide(supplierFragment)
            add(R.id.fragmentContainer, scheduleFragment).hide(scheduleFragment)
            add(R.id.fragmentContainer, commodityFragment).hide(commodityFragment)
            add(R.id.fragmentContainer, profileFragment)
            add(R.id.fragmentContainer, distributorFragment).hide(distributorFragment)
        }.commit()

        bottomNavigation.show(4)
        bottomNavigation.add(MeowBottomNavigation.Model(0,R.drawable.ic_commodity))
        bottomNavigation.add(MeowBottomNavigation.Model(1,R.drawable.ic_hotel_supplier))
        bottomNavigation.add(MeowBottomNavigation.Model(2,R.drawable.ic_calendar))
        bottomNavigation.add(MeowBottomNavigation.Model(3,R.drawable.ic_supplies))
        bottomNavigation.add(MeowBottomNavigation.Model(4,R.drawable.ic_profile_user))

        bottomNavigation.setOnClickMenuListener {
            when (it.id) {
                0 -> {
                    fragmentManager.beginTransaction().setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out).hide(activeFragment).show     (commodityFragment).commit()
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater = menuInflater
        inflater.inflate(R.menu.overflow_menu,menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.covid_info -> {
                val intent = Intent(this, CovidActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
            }
            R.id.phone_contact -> {
                val intent = Intent(this, ContactActivtity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
            }
        }
        return true
    }

    override fun onStart() {
        super.onStart()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            createNewSoundPool()
        else
            createOldSoundPool()

        sp?.setOnLoadCompleteListener{soundPool, id, status ->
            if(status != 0)
                Toast.makeText(this,"Sound Load Failed",Toast.LENGTH_SHORT)
                    .show()
            else{
                sp?.play(soundID,.99f,.99f,1,0,.99f)
            }
        }
        soundID = sp?.load(this, R.raw.sound,1) ?: 0

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun createNewSoundPool() {
        sp = SoundPool.Builder()
            .setMaxStreams(15)
            .build()
    }

    @Suppress("DEPRECATION")
    private fun createOldSoundPool() {
        sp = SoundPool(15, AudioManager.STREAM_MUSIC,0)
    }

    override fun onStop() {
        super.onStop()
        sp?.release()
        sp = null
    }















//    private fun replaceFragment(fragment:Fragment){
//        val fragmentTransition = supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
//        fragmentTransition.replace(R.id.fragmentContainer,fragment).addToBackStack(Fragment::class.java.simpleName).commit()
//    }
//
//    private fun addFragment(fragment:Fragment){
//        val fragmentTransition = supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
//        fragmentTransition.add(R.id.fragmentContainer,fragment).addToBackStack(Fragment::class.java.simpleName).commit()
//    }
}