package com.example.inception.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import com.example.inception.R
import com.example.inception.objectClass.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_subscriber.*

//pada activity inilah user dapat melakukan subcription utk menghilangkan semua iklan yang ada
class SubscriberActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Subscribe to Deactivate Ads"
        setContentView(R.layout.activity_subscriber)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        Picasso.get().load("https://s3-eu-west-1.amazonaws.com/bigmayor/wp-content/uploads/2020/07/15214625/membership-and-Subscription-website.png").into(subscription_image)

        //ambil data apakah user telah subcribe atau blm
        var currentSubscriptionStatus = User.getSubscription(this)
        if(currentSubscriptionStatus == 0) {
            //jika user telah subcribe
            subscribe_button.text = "Subscribe Now"
        }else {
            //jika user belum subcribe
            subscribe_button.text = "Cancel Subscription"
        }

        subscribe_button.setOnClickListener {
            //ambil data apakah user telah subcribe atau blm dari Shared Preferences dengan
            // function getSubcription yang telah dibuat
            currentSubscriptionStatus = User.getSubscription(this)
            if(currentSubscriptionStatus == 1) {
                //ubah data user menjadi unsubcribe dengan function setScubcription
                User.setSubscription(this, 0)
                subscribe_button.text = "Subscribe Now"
            }else {
                //ubah data user menjadi subcribed
                User.setSubscription(this, 1)
                subscribe_button.text = "Cancel Subscription"
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