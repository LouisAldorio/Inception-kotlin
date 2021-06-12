package com.example.inception.objectClass

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.inception.constant.CURRENT_POINT
import com.example.inception.constant.PREVIOUS_PLAYED_SONG
import com.example.inception.constant.PREVIOUS_SONG_INDEX
import com.example.inception.constant.SUBSCRIPTION

//Shared Preference
// disini kita membuat sebuah objek class bernama user, yang dimana shared preference akan terisolasi proses nya didalam kelas ini
object User {

    //kita akan menggunakan shared preference untuk beberapa kasus
    //kasus pertama kita akan gunakan untuk menyimpan token authentikasi dari user
    private const val KEY_TOKEN = "TOKEN"
    private const val USERNAME = "USERNAME"
    private val NOTIFICATION_AMOUNT = "NOTIFICATION_AMOUNT"

    //karna kita menyimpan token, yang dimana token ini yang akan digunakan untuk mengakses API backend
    //kita akan menyimpan nya ke dalam ecrypted shared preference
    //sama saja dengan shared preference biasa , yang membedakan hanyalah setiap data yang disimpan didalam akan di enkripsi dengan algoritma AES256
    private fun preferences(context: Context): SharedPreferences {
        //kita memerlukan kita untuk melakukan enkrip dan dekrip, maka dari itu kita generate key terlebih dahulu
        val masterKeyAlias: String = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        //buat instance untuk shared preference dengan memberikan parameter yang dibutuhkan, berupa nama file, key yang akan digunakan untuk proses kriptografinya
        //context, serta algoritma dan skema proses dalam melakuakn enkripsi datanya.
        val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
            "secret_shared_prefs",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        //setelah berhasil membuat shared preference nya, kita akan mengembalikan instacne yang telah dibuat
        // agar nantinya dapat kita gunakan untuk menyimpan dan mengambil data dari sharef preference
        return sharedPreferences
    }

    //disini kita buat function untuk mengambil token yang ada dari shared preference, function ini akan dipanggil ketika user pertama membuka aplikasi
    // jika token tidak ada, maka akan mengembalikan nilai null yang menandakan user belum login, dan akan di redirect ke activity login/register
    fun getToken(context: Context): String? {
        //dapat dilihat disini kita melakukan get nilai string yang dimana key nya berupa KEY_TOKEN
        return preferences(context).getString(KEY_TOKEN, null)
    }

    //set token akan digunakan untuk menyimpan token yang didapat dari server setelah berhasil login /register kedalam shared preference
    fun setToken(context: Context, token: String) {
        //disini kita mengedit shared preference yang sudah dibuat sebelumnya agar nantinya kit dapat menaruh token string kedalamnya
        preferences(context).edit().apply {
            //masukka token dengan key KEY_TOKEN
            putString(KEY_TOKEN, token)

            //apply agar token tersimpan
            apply()
        }
    }

    //remove token akan kita gunakan untuk menghapus token yang ada didalam shared preference agar session dari user berakhir
    //function ini akan dipanggil ketika user melakukan logout
    fun removeToken(context: Context) {
        preferences(context).edit().apply {
            //disini kita melakukan remove terhadap token yang disimpan pada saat login/register
            remove(KEY_TOKEN)
            //jangan lupa untuk kita apply
            apply()
        }
    }


    //pada kasus kedua kita akan menggunakan shared preference untuk menyimpan state dari lagu yang diputar pada media player nantinya
    fun setPreviousPlayedSong(context: Context,songUri : String) {
        //disini kita akan menyimpan URL dari lagu yang diputar oleh user
        preferences(context).edit().apply {
            //kami menamai variable dengan kata previous, karena memang state yang akan disimpan adalah lagu yang diputar sebelumnya
            // hal ini diperlukan agar kita dapat mengatur state dari recycle view nantinya, agar bertindak sesuai keinginan dan sesuai dengan lagu yang sedang diputar
            putString(PREVIOUS_PLAYED_SONG,songUri)
            //jangan lupa kita apply
            apply()
        }
    }
    //sama seperti function diatas, tapi disini kita akan menyimpan index dari URL lagu yang diputar sebelumnya berupa sebuah bilangan bulat Int
    fun setPreviousSongIndex(context: Context,songIndex : Int) {
        preferences(context).edit().apply {
            putInt(PREVIOUS_SONG_INDEX,songIndex)
            apply()
        }
    }
    //tak lupa setelah kita menyimpan , kita juga harus membuat function untuk mengambil data lagu maupun index lagu yang telah disimpan,
    //hal ini bertujuan agar nantinya ketika proses penyelarasan view dengan service lagu , akan lebih baik
    fun getPreviousPlayedSong(context: Context): String? {
        return preferences(context).getString(PREVIOUS_PLAYED_SONG, null)
    }
    fun getpreviousSongIndex(context: Context): Int? {
        return preferences(context).getInt(PREVIOUS_SONG_INDEX, -1)
    }


    //Reward Preference
    fun getPoint(context: Context): Int {
        return preferences(context).getInt(CURRENT_POINT, 0)
    }

    fun setPoint(context: Context, points: Int) {
        preferences(context).edit().apply {
            putInt(CURRENT_POINT, points)

            //apply agar token tersimpan
            apply()
        }
    }

    //subsciption reference
    fun getSubscription(context: Context): Int {
        return preferences(context).getInt(SUBSCRIPTION, 0)
    }

    fun setSubscription(context: Context, points: Int) {
        preferences(context).edit().apply {
            putInt(SUBSCRIPTION, points)

            //apply agar token tersimpan
            apply()
        }
    }












    fun getUsername(context: Context): String? {
        return preferences(context).getString(USERNAME, null)
    }

    fun setUsername(context: Context, username: String) {
        preferences(context).edit().apply {
            putString(USERNAME, username)
            apply()
        }
    }


    //dot notification State
    fun getNotificationAmount(context: Context): String? {
        return preferences(context).getString(NOTIFICATION_AMOUNT, "0")
    }

    fun setNotificationAmount(context: Context, amount: String) {
        preferences(context).edit().apply {
            putString(NOTIFICATION_AMOUNT, amount)
            apply()
        }
    }


}