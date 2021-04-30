package com.example.inception

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import com.example.inception.activity.LandingPage
import com.example.inception.activity.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

//disini kita akan melakukan testing terhadap overflow menu yang telah kita buat yang akan memanggil 2 buat activity
//berupa contact activity yang akan melakuakn fetch ke content provider dan mengambil data kontak
//serta covid activity yang akan melakukan fetch ke API covid untuk mengambil data terbaru covid
@RunWith(AndroidJUnit4::class)
class OptionMenuTest {
    //definisikan rule activity dari mana kita ingin memulai test
    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(LandingPage::class.java)

    @Test
    fun OptionMenuOpenActivity(){
        //pertama kita buka overflow menu yang telah dibuat
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        //suruh bot untuk mencari view dengan text "Covid Info Update", setelah itu lakukan click
        onView(withText("Covid Info Update")).perform(click());
        //jika activity yang muncul adalah activity covid update makan overflow menu kita telah berhasil terimplementasi
        onView(ViewMatchers.withId(R.id.activity_covid)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
        //disini kita sleep yang bertujuan agar pengamatan view lebih mudah
        Thread.sleep(1000)
        //lakukan pressback agar kembali ke parent activity
        pressBack()

        //panggil kembali overflow menu
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        //cari text "Phone Contact" dan click
        onView(withText("Phone Contact")).perform(click());
        //jika activity yang muncul adalah contact activity , maka overflow menu kita overall bekerja dengan baik
        onView(ViewMatchers.withId(R.id.contact_activity)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
        Thread.sleep(1000)
        //press back kembali ke parent activity
        pressBack()
    }
}