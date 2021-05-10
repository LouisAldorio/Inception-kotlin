package com.example.inception.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import com.example.inception.R
import com.example.inception.adaptor.BookMarkGridViewAdapter
import com.example.inception.constant.INTERNAL_OR_EXTERNAL_MARKER
import com.example.inception.utils.ImageZoomer
import kotlinx.android.synthetic.main.activity_bookmarks.*
import kotlinx.android.synthetic.main.activity_bookmarks.view.*
import kotlinx.android.synthetic.main.fragment_commodity_detail.view.*
import kotlinx.android.synthetic.main.fragment_commodity_detail.view.container
import java.io.File

//activity ini akan membantu kita menampilkan/membaca file gambar yang disimpan didalam internal dan external storage dalam bentuk grid view
class Bookmarks : AppCompatActivity() {

    //objeck zoomer akan membantu dalam proses zooming gambar ketika di click
    val zoomer = ImageZoomer()
    //variable path kita kosongkan terlebih dahulu, karna kita belum tau user ingin menampilkan file dari internal atau external storage
    var path : File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Bookmarked Images"

        setContentView(R.layout.activity_bookmarks)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        zoomer.shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
    }

    override fun onStart() {
        super.onStart()

        //pada lifecycle on start , kita buat sebuah list kosong yang nantinya akan menampung file dari internal atau external
        var bookmarkedList = mutableListOf<String>()

        //ketika user memilih ingin menampilkan file dari internal , maka kita check pada intent apakah ada extra yang dikirimkan berupa string "Internal"
        if (intent.getStringExtra(INTERNAL_OR_EXTERNAL_MARKER) == "Internal"){
            //jika ya, makapath yang kita berikan akan berupa internal path directory, dengan tambahan directory Images yang telah kita buat ketika kita ingin menyimpan
            path = File(this.filesDir, "Images")
            //jika intent yang datang membawa intruksi  External
        }else if (intent.getStringExtra(INTERNAL_OR_EXTERNAL_MARKER) == "External"){
            //berikan path berupa path external Directory, dengan nama Inception dan path tambahan directory Images
            // jika kita akann membaca dari directory yang sama ketika kita menyimpan file
            path = File(this.getExternalFilesDir("Inception"), "Images")
        }

        //ketika path sudah kita tentukan
        //maka kita check terlebih dahulu apakah path yang dimaksud ada atau tidak
        if (path!!.exists()) {
            //jika ada, ubah file yang ada didalam kedalam list
            //check apakah size dari list > 0 , ini menandakan bahwa dalam directory Images ada file yang tersimpan
            if (path!!.listFiles().size != 0){
                //jika ada, masukkan semua path yang telah di list dalam bentuk string kedalam arraylist yang telah kita buat sebelumnya
                for(file in path!!.listFiles()) {
                    bookmarkedList.add(file.toString())
                }

                //buat instance untuk adapter dari gridview, jangan lupa berikan list dari path yang telah kita kumpulkan sebelumnya
                // pada paramter ketiga jangan lupa melemparkan sebuah callback function yang akan menghandle proses zooming image
                val adapters = BookMarkGridViewAdapter(this@Bookmarks,bookmarkedList){ position, imageView ->
                    this@Bookmarks?.let { ac ->

                        val temp = File(bookmarkedList[position])

                        zoomer.zoomImageFromThumb(
                            ac,
                            imageView,
                            "",
                            bookmark_container,
                            expanded_image_bookmark,
                            temp
                        )
                    }
                }

                //pada gridview, berikan adapter yang telah dibuat
                gv_bookmark.apply {
                    adapter = adapters
                }
            }
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                onBackPressed()
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}