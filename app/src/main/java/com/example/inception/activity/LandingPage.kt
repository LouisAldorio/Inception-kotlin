package com.example.inception.activity

import android.annotation.TargetApi
import android.content.Intent
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.inception.R
import com.example.inception.constant.INTERNAL_OR_EXTERNAL_MARKER
import com.example.inception.fragment.*
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_landing_page.*
import java.io.File


class LandingPage : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    //kita akan menggunakan soundpool, untuk memasukkan musik singkat ketika user pertama kali membuka aplikasi
    //definisikan sebuat variable yang akan berisi intance souncpool nantinya
    var sp : SoundPool? = null
    //jangan lupa pula variable untuk sound yang akan kita gunakan nanti, berikans aja nilai 0 sebagai nilai default
    var soundID  : Int = 0

    private val commodityFragment = CommodityFragment()
    private val supplierFragment = SupplierFragment()
    private val scheduleFragment = ScheduleFragment()
    private val distributorFragment = DistributorFragment()
    private val profileFragment = ProfileFragment()

    private val fragmentManager = supportFragmentManager

    private var activeFragment: Fragment = profileFragment

    var drawerLayout: DrawerLayout? = null
    var actionBarDrawerToggle: ActionBarDrawerToggle? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)

        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawer_layout, R.string.nav_open, R.string.nav_close)
        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout?.addDrawerListener(actionBarDrawerToggle!!)
        actionBarDrawerToggle!!.syncState()

        // to make the Navigation drawer icon always appear on the action bar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        nav_view.setNavigationItemSelectedListener(this)



        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        //ketika proses onCreate activity landing page kita akan melakukan toast dan menampilkan sisa space yang ada pada external storage
        Toast.makeText(this,"Space Left In Device is " + getAvailableExternalMemorySize(),Toast.LENGTH_LONG).show()

        //pada lifecycle onCreate, kita amakn membuat soundpool dan menyuruh soundpool untuk melalkukan load terhadap resource file .mp3 singkatnya
        //kita check terlebih dahulu apakah versi adnroid > lolipop jika ya, kita akan menggunakan cara baru untuk menginisiasi instance soundpool
        //jika tidak kita akan menggunakan cara lama untuk menginisiasi soudpool
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            createNewSoundPool()
        else
            createOldSoundPool()

        //setelah soundpool berhasil diinisiasi, kita buat sebuah listener yang akan mengamati proses load, file resource audi ke dalam soundpool
        sp?.setOnLoadCompleteListener{soundPool, id, status ->
            //disini kita akan check apakah status laad berhasil apa tidak, jika 0 maka load gagal
            //jika status != 0 , maka proses load berhasil maka kita akan langsung memainkan sound yang berhasil do load
            if(status != 0)
                Toast.makeText(this,"Sound Load Failed",Toast.LENGTH_SHORT)
                    .show()
            else{
                // disini kita akan memainkan sound jika load berhasil, dengan kecepatan normal, dan volme yang stabil
                // kita mau sound berulang maka kita set loop menjadi 0
                sp?.play(soundID,.99f,.99f,1,0,.99f)
            }
        }
        //setelah listener/observer berhasil ditambahkan, maka kita segera load file resouce sound nya, fungsi load akan mengembalikan sound id
        // dimana nantinya akan kita gunakan sebagai identifier untuk melakukan play terhadap file resource yang dinginkan
        soundID = sp?.load(this, R.raw.sound,1) ?: 0


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
        if (actionBarDrawerToggle!!.onOptionsItemSelected(item)) {
            return true;
        }
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
            R.id.song -> {
                val intent = Intent(this, MusicRecommendation::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
            }


            //ketika menu bookmark_internal dipilih, brarti user ingin menampilkan file gambar yang terismpan didalm storage internal
            R.id.bookmark_internal -> {
                //buat intent dan kirimkan extra yang memuat penanda bahwa yang harus ditampilkan adalah file dari internal storage
                val intent = Intent(this, Bookmarks::class.java)
                intent.putExtra(INTERNAL_OR_EXTERNAL_MARKER,"Internal")
                startActivity(intent)
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
            }
            //jika yang dipilih adalah menu bookmark_external, maka kita harus menampilkan file yang tersimpan pada storage external
            R.id.bookmark_external -> {
                //buat intent dan kirim extra yang menandakan ingin mengambil file dari directory private di external storage
                val intent = Intent(this, Bookmarks::class.java)
                intent.putExtra(INTERNAL_OR_EXTERNAL_MARKER,"External")
                startActivity(intent)
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
            }
        }
        return true
    }

    //kita berikan anotasi, ke function creat soundpool nya yang menyatakan function dibawah hanya bisa dijalankan apabila
    // level API > lolipop
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun createNewSoundPool() {
        //panggil builder dari soundpool, dan berikan maxstreams 15,
        // artinya kita membuat soundpooldapat menangani proses play sound sebanyak 15 buah secara bersamaan
        //lalu kita build
        sp = SoundPool.Builder()
            .setMaxStreams(15)
            .build()
    }

    @Suppress("DEPRECATION")
    private fun createOldSoundPool() {
        //pada proses create old soundpool kita hanya perlu memanggil constructor dan memberikan parameter yang dibutuhkan , seperti maxstreams
        //maupun tipe apa yang ingin di stream
        sp = SoundPool(15, AudioManager.STREAM_MUSIC,0)
    }

    override fun onStop() {
        super.onStop()
        //jangan lupa pula pada lifecycle onStop dari activity, kita release soundpool nya agar tidak memakan memori
        sp?.release()
        sp = null
    }


    //disini untuk membaca space yang tersisa pada external storage, kita harus check terlebih dahulu apakah penyimpana external tersedia/terpasang
    fun externalMemoryAvailable(): Boolean {
        return Environment.getExternalStorageState() ==
                Environment.MEDIA_MOUNTED
    }

    //function dibawa akan membantu kita untuk mendapatkan ukuran dari external memory
    fun getAvailableExternalMemorySize(): String? {
        //panggil function yang mengecek apakah external memory terpasang
        return if (externalMemoryAvailable()) {
            //tentukan path yaitu external storage
            val path: File = Environment.getExternalStorageDirectory()
            //fungsi StatFs akan membantu kita menjabarkan statistik atau keadaan saat ini dari path external storage yang kita berikan
            val stat = StatFs(path.getPath())

            //sebelum menentukan ukuran, kita harus telebih dahulu mencari tahu ukuran dari setiap block memory melalui stats yang telah didapat melalui fungsi StatFs
            val blockSize: Long = stat.getBlockSizeLong()
            //kita juga perlu tahu berapa block yang masih tersedia
            val availableBlocks: Long = stat.getAvailableBlocksLong()

            //untuk  mengetahui ukuran total , maka kita harus mengalikan jumlah block memory yang tersedia dengan ukuran setiap block
            //dengan begitu kita akan mendapatkan ukuran total memory dalam satuan byte

            return (availableBlocks * blockSize).toString()
        } else {
            ""
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.todo -> {
                val intent = Intent(this, NoteTodoActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true;
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