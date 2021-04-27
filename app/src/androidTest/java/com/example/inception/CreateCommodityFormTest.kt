package com.example.inception

import android.content.Intent
import android.net.Uri
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.inception.activity.LandingPage
import org.hamcrest.CoreMatchers
import org.hamcrest.core.AllOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


//disini kita akan melakukan testing untuk memanggil activity melalui activity lainnya
@RunWith(AndroidJUnit4::class)
class CreateCommodityFormTest {

    //definisikan rule , kita akan mulai dari Activity Landing Page
    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(LandingPage::class.java)


    @Test
    fun CallCommodityCreateForm(){
        //tunggu hingga API membalikkan data agar view sudah dirender
        Thread.sleep(2500);
        //cari tombol create commodity lalu click
        onView(withId(R.id.commodity_create)).perform(ViewActions.click())
        //jika yang dimunculkan dalah object view activity yang berisi form untuk melakukan create commodity
        // maka kita telah berhasil melakukan testing untuk pmenaggilan antar activity
        onView(withId(R.id.activity_create_commodity)).check(matches(isDisplayed()))
        Thread.sleep(1000);
    }



















//    @Test
//    fun CheckCommodityNameNotEmpty(){
//        CallCommodityCreateForm()
//        onView(withId(R.id.btn_create_commodity)).perform(ViewActions.scrollTo())
////        Espresso.onView(ViewMatchers.withText("Commodity Name must not be empty")).inRoot(
////            RootMatchers.withDecorView(
////                CoreMatchers.not(
////                    CoreMatchers.`is`(
////                        activityTestRule.getActivity().getWindow().getDecorView()
////                    )
////                )
////            )
////        ).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
//    }

}