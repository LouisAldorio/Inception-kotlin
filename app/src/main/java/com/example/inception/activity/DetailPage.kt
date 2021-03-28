package com.example.inception.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import android.transition.ChangeBounds
import com.example.inception.R
import com.example.inception.constant.CONTEXT_EXTRA
import com.example.inception.constant.NOTIFICATION_CONTEXT
import com.example.inception.fragment.CommodityDetailFragment
import com.example.inception.objectClass.User


class DetailPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bounds = ChangeBounds()
        bounds.setDuration(500)
        window.sharedElementEnterTransition = bounds

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        setContentView(R.layout.activity_detail_page)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        var context = intent.getStringExtra(CONTEXT_EXTRA)
        if(context == "Commodity"){
            var isNotification = intent.getBooleanExtra(NOTIFICATION_CONTEXT,false)
            if(isNotification == true) {
                User.setNotificationAmount(this,(User.getNotificationAmount(this)!!.toInt() - 1).toString())
            }
            replaceFragment(CommodityDetailFragment())
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


    private fun replaceFragment(fragment: Fragment){
        val fragmentTransition = supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
        fragmentTransition.replace(R.id.fragmentContainer,fragment).addToBackStack(Fragment::class.java.simpleName).commit()
    }

    private fun addFragment(fragment: Fragment){
        val fragmentTransition = supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
        fragmentTransition.add(R.id.fragmentContainer,fragment).addToBackStack(Fragment::class.java.simpleName).commit()
    }
}