package com.example.inception

import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.inception.activity.LandingPage
import com.example.inception.activity.MainActivity
import com.example.inception.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

// disini kita akan melakukan testing Register, apakah UI kita berhasil menghandle proses register yang valid
//berbeda dari sebelumnya , kita melakukna testing terhadap seberapa baik form register kita melakukan error handling dari user
@RunWith(AndroidJUnit4::class)
class RegisterLogOutTest {

    //seperti biasa definisikan activity rule yang berlaku
    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    //karna kita akan menjalankan async task yang melakukan requets ke API yang ktia tidak tau berapa lama akan mendapat response dari server.
    // kita akan menggunakan idling resource untuk keep track proses testing kita
    //sebelum menjalankan fungsi test , kita akan registerkan terlebih dahulu idling resource kita
    @Before
    fun RegisterIdlingResource(){
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }
    @Test
    fun RegisterAndLogOut(){
        //isi semua informasi yang diperlukan
        Espresso.onView(ViewMatchers.withId(R.id.username)).perform(
            ViewActions
                .typeText("loki"))
        Espresso.onView(ViewMatchers.withId(R.id.email)).perform(
            ViewActions
                .typeText("loki@gmail.com"))
        Espresso.onView(ViewMatchers.withId(R.id.whatsapp)).perform(
            ViewActions
                .typeText("082161723455"))
        //isikan password berbeda dengan field confirm password
        Espresso.onView(ViewMatchers.withId(R.id.password)).perform(
            ViewActions
                .typeText("test123"))
        Espresso.onView(ViewMatchers.withId(R.id.confirm_password)).perform(
            ViewActions
                .typeText("test123"))
        //pastikan data diatas memang sudah sesuai dengan data yang valid unutk mendaftar

        //lakukan click pada register, karena kita telah menggunakan diling resource,
        //unit tester kita akan mengunggu hingga servermembalikkan response
        Espresso.onView(ViewMatchers.withId(R.id.register)).perform(ViewActions.scrollTo()).perform(ViewActions.click())
        //setelah server membalikkan response kita arahkan user kehalaman landing page
        Espresso.onView(ViewMatchers.withId(R.id.landing_page))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        //kami melakukan sleep disini agar proses testing dapat damati lebih jelas
        Thread.sleep(500)

        //setelah berhasil mendaftar keserver, kita akan mencoba logout
        Espresso.onView(ViewMatchers.withId(R.id.logout)).perform(ViewActions.click())
        //apakah halaman register akan muncul kembali setelah kita log out, disini kita check
        Espresso.onView(ViewMatchers.withId(R.id.regisLoginPage))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    //kita unregister idling resource setelah proses testing selesai
    @After
    fun UnRegisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }
}