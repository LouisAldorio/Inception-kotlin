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

class Bookmarks : AppCompatActivity() {

    val zoomer = ImageZoomer()
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

        var bookmarkedList = mutableListOf<String>()

        if (intent.getStringExtra(INTERNAL_OR_EXTERNAL_MARKER) == "Internal"){
            path = File(this.filesDir, "Images")
        }else if (intent.getStringExtra(INTERNAL_OR_EXTERNAL_MARKER) == "External"){
            path = File(this.getExternalFilesDir("Inception"), "Images")
        }


        if (path!!.exists()) {
            if (path!!.listFiles().size != 0){
                for(file in path!!.listFiles()) {
                    bookmarkedList.add(file.toString())
                }

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