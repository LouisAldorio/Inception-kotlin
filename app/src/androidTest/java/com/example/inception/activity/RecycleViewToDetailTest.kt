package com.example.inception.activity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.example.inception.R
import com.example.inception.adaptor.CommodityHolder
import com.example.inception.adaptor.ImageHolder
import com.example.inception.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

//kali ini kita akan melakukan test pada recycle view, untuk beberapa item yang ada didalam nya
//kita akan menggunakan annotation @LargeTest untuk menyatakan bahwa test ini cukup besar
@LargeTest
@RunWith(AndroidJUnit4::class)
class RecycleViewToDetailTest {

    //definiskan activity rule untuk activity yang merender recycle view nya
    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(LandingPage::class.java)

    //kita akan menggunakan idling resource, karna kita akan meminta data commodity ke API
    @Before
    fun RegisterIdlingResource(){
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    //disini buat buat function yang menjalankan test, untuk setiap elemen recycleView
    fun RecycleViewToDetailPageTest(index : Int) {
        //pada recycle View lakukan scroll kepada index / posisi dari item yang didalam recycle view nya
        onView(withId(R.id.user_commodity_rv)).perform(actionOnItemAtPosition<CommodityHolder>(index, scrollTo()))
        //lakukan click pada item di recycle view pada posisi yang sudah ditentukan
        onView(withId(R.id.user_commodity_rv)).perform(actionOnItemAtPosition<CommodityHolder>(index, click()))
        //check apakah yang muncul adalah detail dari commodity tersebut
        onView(withId(R.id.detail_activity)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
        //lakukan click pada recycleview carousel image yg ada pada bagian detail, apakah image berhasil ter zoom
        onView(withId(R.id.image_carousel_rv)).perform(actionOnItemAtPosition<ImageHolder>(0, click()))

        //lakukan sleep agar, pergerakan lebih dapat diamati
        Thread.sleep(500)
        //pressback dilakukan agar kita dapat kembali ke parent activity yang meng hold recycle view, agar kita dapat menekan item lain lagi untuk dilakukan test
        pressBack()
        Thread.sleep(500)
    }

    //di function test ini kita akan memanggil function yang telah kita buat sebelumnya dengan memberikan berbagai posisi dari item yang ada didalam recycle view
    @Test
    fun RunTest(){
        RecycleViewToDetailPageTest(5)
        RecycleViewToDetailPageTest(10)
        RecycleViewToDetailPageTest(3)
        RecycleViewToDetailPageTest(15)
        RecycleViewToDetailPageTest(20)
        RecycleViewToDetailPageTest(35)
        RecycleViewToDetailPageTest(34)
        RecycleViewToDetailPageTest(12)
    }

    //setelah selesai , lakukan unregister terhadap diling resource nya
    @After
    fun UnRegisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }
}
