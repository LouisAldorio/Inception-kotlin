package com.example.inception.utils

import androidx.test.espresso.idling.CountingIdlingResource

//kita membuat sebuah object class dengan nama espresso idling resource yang nantinya akan membantu kita memanage proses idling saat request ke API
//kita gunakan object agar ketika kita memanggil kelas ini, kelas ini langsung memberikan kita object, tanpa perlu kita invoke
//menggunakan "()", sehingga akan lebih memudahkan kita untuk membuat object instance dari kelas ini.
object EspressoIdlingResource {

    //buat sebuah variabel , bebas nama nya apa, disini kami memberikan nama GLOBAL sesuai best practice dari developer.android.google.com
    private const val RESOURCE = "GLOBAL"

    //seperti biasa kiter perlu bantuan java virtual machine untuk membantu proses idling
    // buat sebuah object instance dari kelas CountingIdlingResource, dan berikan variable yang telah kita buat sebelumnya.
    @JvmField
    val countingIdlingResource = CountingIdlingResource(RESOURCE)

    //fungsi increment disini akan membantu kita untuk memulai proses idling
    fun increment() {
        countingIdlingResource.increment()
    }

    //decrement akan membantu kita mengakhiri proses idling.
    fun decrement() {
        countingIdlingResource.decrement()
    }

}