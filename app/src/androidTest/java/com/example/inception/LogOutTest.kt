package com.example.inception

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.inception.activity.MainActivity
import com.example.inception.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


// disini kita akan melakukan hit API login dengan data yang user yang sudah terdaftar
// kita akan menggunakan idling resource
@RunWith(AndroidJUnit4::class)
class LogOutTest {

    //definsikan rule activity ynag berlaku
    @Rule @JvmField
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    //persiapkan idling resource yang nanti nya dapat melakukan tracking selama proses API login berjalan
    @Before
    fun RegisterIdlingResource(){
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }
    @Test
    fun LogInAndLogOut(){

        //ketika activity dimulai, kita akan bergeser ke tab login
        onView(ViewMatchers.withText("LOGIN")).perform(ViewActions.click())

        //memasukkan data user yang sudah terdaftar
        onView(withId(R.id.username_login)).perform(ViewActions.closeSoftKeyboard()).perform(
        ViewActions.typeText("louisaldorio"))
        onView(withId(R.id.password_login)).perform(ViewActions.closeSoftKeyboard()).perform(
        ViewActions.typeText("123456"))

        //lakukan click pada button login, karena kita telah menggunakan diling resource, testing otomastis akan masuk ke mode idle selama proses melakuakn requets ke API
        onView(withId(R.id.login)).perform(ViewActions.click())
        //setelah berhasil mendapat response dari server, kita check apakah activity selanjutnya yang tampil sudah benar adalah landing page
        onView(withId(R.id.landing_page)).check(matches(isDisplayed()))

        //kita perform log out dan keluar kembali ke parent activity
        onView(withId(R.id.logout)).perform(click())
        onView(withId(R.id.regisLoginPage)).check(matches(isDisplayed()))
    }

    // unregister idling resource yang sebelumnya telah kita daftarkan
    @After
    fun UnRegisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }
}