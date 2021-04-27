package com.example.inception

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.inception.activity.MainActivity
import org.hamcrest.CoreMatchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

//pada login validation kita juga akan melakukan test terhadap validasi input user ketika user ingin melakukan login
@RunWith(AndroidJUnit4::class)
class LoginValidationTest {

    //tentukan rule atau aktivity maupun fragment yang ingin di test
    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    //kita akan melakukan testing dan melakukan check terhadap username yang diinputkan user apakah kosong
    @Test
    fun TestLoginUsernameNotEmpty(){
        //dikarenakan view by default yang muncul adalah bagian register
        // kita menyuruh bot untuk mencari text LOGIN yang merupakan object view button uantuk menggeser view page ke bagian login, setelah itu kita click
        Espresso.onView(ViewMatchers.withText("LOGIN")).perform(ViewActions.click())

        //setelah berhasil menavigasi ke halaman login, kita suruh bot untuk menutup keyboard agar tidak menghalangi button login, lalu tekan
        // tanpa mnegisi form
        Espresso.onView(ViewMatchers.withId(R.id.login)).perform(closeSoftKeyboard())
            .perform(ViewActions.click())
        //jika toast berisi pesan seperti dibawah berhasil ditampilkan, maka kita telah berhasil melakukan error handling pada aplikasi kita
        // ketika user ingin login
        Espresso.onView(ViewMatchers.withText("Username must not be empty")).inRoot(
            //perintah dibawah sama dengan apa yang dituliskan pada bagian register, namun disini kita langsung menuliskan class nya sehingga terlihat lebih jelas
            RootMatchers.withDecorView(
                CoreMatchers.not(
                    CoreMatchers.`is`(
                        activityTestRule.getActivity().getWindow().getDecorView()
                    )
                )
            )
        ).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    //check terhadap password ynag kosong ketika user ingin login
    @Test
    fun TestLoginPasswordNotEmpty(){
        // lakukan navigasi ke bagian login
        Espresso.onView(ViewMatchers.withText("LOGIN")).perform(ViewActions.click())
        //isikan username dan kosong kan bagian password
        Espresso.onView(ViewMatchers.withId(R.id.username_login)).perform(closeSoftKeyboard()).perform(
            ViewActions
                .typeText("louisaldorio"))
        //tekan login
        Espresso.onView(ViewMatchers.withId(R.id.login))
            .perform(ViewActions.click())
        // jika toast dengan pesan bahwa "Password must not be empty!" muncul maka kita telah berhasil melakukan handle apabila user tidak mengisi password.
        Espresso.onView(ViewMatchers.withText("Password must not be empty!")).inRoot(
            RootMatchers.withDecorView(
                CoreMatchers.not(
                    CoreMatchers.`is`(
                        activityTestRule.getActivity().getWindow().getDecorView()
                    )
                )
            )
        ).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}