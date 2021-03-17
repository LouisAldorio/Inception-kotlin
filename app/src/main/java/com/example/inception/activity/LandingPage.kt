package com.example.inception.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.inception.R
import kotlinx.android.synthetic.main.activity_landing_page.*

import com.example.inception.fragment.*
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.inception.`interface`.RecycleViewFragmentInterface

//tidak lupa untuk mengimplementasikan interface yang tadi telah dibuat ke dalam activity
class LandingPage : AppCompatActivity() {

    //dalam penggunaan fragment dalam activity kita terlebih dahulu inisiasi fragment yang ingin digunakan
    private val commodityFragment = CommodityFragment()
    private val supplierFragment = SupplierFragment()
    private val scheduleFragment = ScheduleFragment()
    private val distributorFragment = DistributorFragment()
    private val profileFragment = ProfileFragment()

    //inisiasi juga fragment manager yang nantinya akan membantu kita megngelola fragment yangada
    private val fragmentManager = supportFragmentManager
    // dikarenakan saya tidak ingin tidak tiap berganti fragment , saya membuat fragment baru
    // saya membuat sebuah variabel yang akan menampung active fragment yang dimana merupakan fragment yang akan ditampilkan ke user sesuai plihannya.
    private var activeFragment: Fragment = commodityFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        // disini kita memanggil beginTransaction fragment yang dimana akan menyembunyikan 4 dari 5 fragment
        // dan menampilkan hanya 1 fragment saja yaitu commodity fragment
        fragmentManager.beginTransaction().apply {
            add(R.id.fragmentContainer, supplierFragment).hide(supplierFragment)
            add(R.id.fragmentContainer, scheduleFragment).hide(scheduleFragment)
            add(R.id.fragmentContainer, distributorFragment).hide(distributorFragment)
            add(R.id.fragmentContainer, profileFragment).hide(profileFragment)
            add(R.id.fragmentContainer, commodityFragment)
        }.commit()

        //memasukkan icon ke dalam botton navigation dan id sebagai indikator kita dalam berpindah fragment
        bottomNavigation.show(0)
        bottomNavigation.add(MeowBottomNavigation.Model(0,R.drawable.ic_commodity))
        bottomNavigation.add(MeowBottomNavigation.Model(1,R.drawable.ic_hotel_supplier))
        bottomNavigation.add(MeowBottomNavigation.Model(2,R.drawable.ic_calendar))
        bottomNavigation.add(MeowBottomNavigation.Model(3,R.drawable.ic_supplies))
        bottomNavigation.add(MeowBottomNavigation.Model(4,R.drawable.ic_profile_user))

        //memanggil click listener agar ketika bottom naviagtion di di tekan, makan fragment yang ditampilkan akan sesuai dengan opsi yang telah dipilih user
        bottomNavigation.setOnClickMenuListener {
            // ketika user menekan salah satu opsi di dalam bottom navigation , kita akan mendapat ID sebagai indokator fragment mana yang ahrus kita tampilkan
            when (it.id) {
                0 -> {
                    //sebagi contoh, jika user memilih fragment yang memiliki id 0, maka fragment yang sekarang sedang aktif akan di hide
                    // lalu nilai dari variable active fragment akan kita update sehingga pada view akan diganti dengan fragment yang ddinginkan
                    // begitu seterusnya pada id 1,2,3, dan 4
                    // saya menghindari penggunaan replace dikarenakan tidak ingin apabila setiap user bernavigasi , kita selalu membuat fragment baru, yang dimana kita akan
                    // meminta data ulang ke server, hal ini tentu tidak efektif
                    // dalam case saya , saya juga tidak ingin ada nya backstack fragment didalm activity melainkan user dapat langsung keluar dari aplikasi ketikan menekan
                    //onBackPressed
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