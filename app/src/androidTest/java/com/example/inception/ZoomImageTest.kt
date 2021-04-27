package com.example.inception

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.inception.activity.LandingPage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

//kali ini kita akan melakukan pengujian terhadap fitur zoomg image yang ktia miliki
@RunWith(AndroidJUnit4::class)
class ZoomImageTest {

    //definiskan rule , disini kita menggunakan activity landing page yang berisi fragment profile
    @Rule @JvmField
    var activityTestRule = ActivityTestRule(LandingPage::class.java)

    @Test
    fun ZoomProfile(){
        //kita malakukan sleep selama 4 detik, behubung aplikasi kita sudah terhubung dengan API,
        // jadi kita perlu menunggu hingga data dari API dibalikkan agar view dapat ditampilkan, baru kita bisa mulai melakukan test
        Thread.sleep(4000);
        //setelah view muncul, cari object view dengan id profile_avatar lakukan click
        onView(withId(R.id.profile_avatar)).perform(ViewActions.click())
        //check apakah setelah profile_avatar diclick , memunculkan container expended_image yang menampung image yang dizoom
        // jika pass maka fitur zoom image kita telah berhasil dilakukan
        onView(withId(R.id.expanded_image)).check(matches(isDisplayed()))
        Thread.sleep(1000);
    }
}