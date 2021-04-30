package com.example.inception.objectClass

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.inception.constant.PREVIOUS_PLAYED_SONG
import com.example.inception.constant.PREVIOUS_SONG_INDEX

object User {

    private const val KEY_TOKEN = "TOKEN"
    private const val USERNAME = "USERNAME"
    private val NOTIFICATION_AMOUNT = "NOTIFICATION_AMOUNT"

    private fun preferences(context: Context): SharedPreferences {
        val masterKeyAlias: String = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
            "secret_shared_prefs",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        return sharedPreferences
    }

    fun getToken(context: Context): String? {
        return preferences(context).getString(KEY_TOKEN, null)
    }

    fun setToken(context: Context, token: String) {
        preferences(context).edit().apply {
            putString(KEY_TOKEN, token)
            apply()
        }
    }

    fun removeToken(context: Context) {
        preferences(context).edit().apply {
            remove(KEY_TOKEN)
            apply()
        }
    }


    fun setPreviousPlayedSong(context: Context,songUri : String) {
        preferences(context).edit().apply {
            putString(PREVIOUS_PLAYED_SONG,songUri)
            apply()
        }
    }
    fun setPreviousSongIndex(context: Context,songIndex : Int) {
        preferences(context).edit().apply {
            putInt(PREVIOUS_SONG_INDEX,songIndex)
            apply()
        }
    }
    fun getPreviousPlayedSong(context: Context): String? {
        return preferences(context).getString(PREVIOUS_PLAYED_SONG, null)
    }
    fun getpreviousSongIndex(context: Context): String? {
        return preferences(context).getString(PREVIOUS_SONG_INDEX, null)
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