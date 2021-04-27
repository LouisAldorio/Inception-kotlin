package com.example.inception

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.inception.activity.MainActivity
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

//kita akan menjalankan test dengan menggunakan jUnit
@RunWith(AndroidJUnit4::class)
class RegisterValidationTest {

    //definisikan rule/activity yang ingin dijadikan rule unutk di test
    @Rule @JvmField
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    //pada fungsi test dibawah kita akan mengecek apakah validasi/ pemeriksaan terhadap input user sudah sukses dijalankan
    @Test
    fun TestUsernameNotEmpty(){
        //kita akan langsung mencoba menyuruh bot menekan tombol register tanpa melakukan pengisian form
        onView(withId(R.id.register)).perform(scrollTo()).perform(ViewActions.click())
        //hasil yang diharapkan adalah sebuah toast yang berisi pesan seperti dibawah
        onView(withText("Username must not be empty")).inRoot(
            withDecorView(not(`is`(activityTestRule.getActivity().getWindow().getDecorView())))
        //kita menyuruh bot untuk melakukan check apabila toast message yang ditampilkan sudah sesuai dengan yang harus ditampilkan
        ).check(matches(isDisplayed()))
    }

    //test dibawah dilakukan untuk melakukan check terhadap field email yang kosong
    @Test
    fun TestEmailNotEmpty(){
        //disini kita akan menyuruh bot untuk mengetikkan username, artinya field username sudah tidak kosong
        onView(withId(R.id.username)).perform(
            ViewActions
                .typeText("louisaldorio"))
        //lakukan click terhadap button register
        onView(withId(R.id.register)).perform(scrollTo()).perform(ViewActions.click())
        //jika toast yang ditampilkan berisi pesan seperti dibawah
        onView(withText("Email must not be empty!")).inRoot(
            //perintah dibawah digunakan untuk menyuruh window menangkap apapun tampilan UI yang berisi pesan diatas
            withDecorView(not(`is`(activityTestRule.getActivity().getWindow().getDecorView())))
        //maka proses testing kita telah berhasil dilakukan dan berjalana dengan baik
        ).check(matches(isDisplayed()))
    }


    // disini kita akan melakuakn test untuk mengecek apakah email yang diinputkan oleh user untuk melakuakn register sudah valid
    @Test
    fun TestEmailInvalidPattern(){
        //suruh bot isi bagian username
        onView(withId(R.id.username)).perform(
            ViewActions
                .typeText("louisaldorio"))
        //isi bagian email , namun sengaja kita salahkan untuk melihat error handling yang dilakukan aplikasi
        //Note: email dibawah tidak valid karena hanya mengandung sebuah nama tanpa domain smtp mail yang benar
        onView(withId(R.id.email)).perform(
            ViewActions
                .typeText("louis"))
        //lakukan click button register
        onView(withId(R.id.register)).perform(scrollTo()).perform(ViewActions.click())

        //jika pesan toast dibawah muncul maka error handling email yang valid pada aplikasi email kita sudah benar.
        onView(withText("Email Seems to be Invalid!")).inRoot(
            withDecorView(not(`is`(activityTestRule.getActivity().getWindow().getDecorView())))
        ).check(matches(isDisplayed()))
    }

    //test dibawah akan melakukan check terhadap field nomor whatsapp yang kosong
    @Test
    fun TestWhatsAppNotEmpty(){
        //kita suruh bot mengisi username yang valid
        onView(withId(R.id.username)).perform(
            ViewActions
                .typeText("louisaldorio"))
        //mengisikan email yang valid, namun mengosongkan bagian whatsapp
        onView(withId(R.id.email)).perform(
            ViewActions
                .typeText("louisaldorio@gmail.com"))
        onView(withId(R.id.register)).perform(scrollTo()).perform(ViewActions.click())

        //lakukan click pada tombol register
        onView(withText("Whatsapp Number must not be empty!")).inRoot(
            //jika didapati pesan toast seperti pesan diatas, maka kita telah berhasil melakukan check terhadap pengisian nomor WA yang kosong
            withDecorView(not(`is`(activityTestRule.getActivity().getWindow().getDecorView())))
        ).check(matches(isDisplayed()))
    }

    //test dibawah akan melakukan check terhadap password kosong yang diinput oleh user
    @Test
    fun TestPasswordNotEmpty(){

        //instruksikan bot untuk mengisi elemen yang diperlukan selain password
        onView(withId(R.id.username)).perform(
            ViewActions
                .typeText("louisaldorio"))
        onView(withId(R.id.email)).perform(
            ViewActions
                .typeText("louisaldorio@gmail.com"))
        onView(withId(R.id.whatsapp)).perform(
            ViewActions
                .typeText("082161723455"))

        //langsung kita tekan tombol register
        onView(withId(R.id.register)).perform(scrollTo()).perform(ViewActions.click())

        //jika terdeteksi view dengan pesan dibawah maka, penanganan error kita telah berhasil
        onView(withText("Password must not be empty!")).inRoot(
            withDecorView(not(`is`(activityTestRule.getActivity().getWindow().getDecorView())))
        ).check(matches(isDisplayed()))
    }


    //lakukan hal yang sama dengan field confirm password, dikarenakan keduanya diperlukan untuk melakukan validasi pendaftaran
    @Test
    fun TestConfirmPasswordNotEmpty(){
        //kita inputkan semua yang sebelumnya diperlukan
        onView(withId(R.id.username)).perform(
            ViewActions
                .typeText("louisaldorio"))
        onView(withId(R.id.email)).perform(
            ViewActions
                .typeText("louisaldorio@gmail.com"))
        onView(withId(R.id.whatsapp)).perform(
            ViewActions
                .typeText("082161723455"))
        //disini kita akan isi sebuah password dummy, dan mengosongkan field confirm password
        onView(withId(R.id.password)).perform(
            ViewActions
                .typeText("test123"))
        //instruksikan untuk melakukan click pada tombol register
        onView(withId(R.id.register)).perform(scrollTo()).perform(scrollTo()).perform(ViewActions.click())

        //selanjutnya bot akan mencari object view yang menmapikan test "Confirm Password must not be Empty!", jika ditemukan
        onView(withText("Confirm Password must not be Empty!")).inRoot(
            withDecorView(not(`is`(activityTestRule.getActivity().getWindow().getDecorView())))
        //maka kita telah berhasil melakukan handling error jika user tidak mengisi bagian confirm password
        ).check(matches(isDisplayed()))
    }

    //pada unit test terkahir di bagian registration validation ini kita akan mencoba mencocokan isi dair field password dan confirm password yang diinputkan user
    //jika berbeda maka akan kita handle dengan memberi tahu user bahwa password yang diinputkan tidak sama dengan field confirm password
    @Test
    fun TestPasswordNotMatchConfirmPassword(){
        //isi semua informasi yang diperlukan
        onView(withId(R.id.username)).perform(
            ViewActions
                .typeText("louisaldorio"))
        onView(withId(R.id.email)).perform(
            ViewActions
                .typeText("louisaldorio@gmail.com"))
        onView(withId(R.id.whatsapp)).perform(
            ViewActions
                .typeText("082161723455"))
        //isikan password berbeda dengan field confirm password
        onView(withId(R.id.password)).perform(
            ViewActions
                .typeText("test123"))
        onView(withId(R.id.confirm_password)).perform(
            ViewActions
                .typeText("test111"))

        // dapat dilihat diatas kita memberikan password "test123" tetapi pada bagian confirm password kita memberikan "test111"
        //tekan tombol register
        onView(withId(R.id.register)).perform(scrollTo()).perform(ViewActions.click())

        //jika ditemukan pesan "Password does not match!" pada window view activity maka kita telah berhasil menghandle error
        // ketika user memberikan password yang tidak sesuai dengan confirm password.
        onView(withText("Password does not match!")).inRoot(
            withDecorView(not(`is`(activityTestRule.getActivity().getWindow().getDecorView())))
        ).check(matches(isDisplayed()))
    }
}