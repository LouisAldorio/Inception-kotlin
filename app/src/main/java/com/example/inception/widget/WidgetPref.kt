package com.example.inception.widget

import android.content.Context

val PREF_NAME = "widget_pref"
val KEYS_WIDGET_IDS = "widget_ids"

class WidgetPref(context: Context) {

    private val page_key = "page"

    val pref = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE)

    fun setIds(ids : MutableSet<String>){
        val editor = pref.edit()
        editor.putStringSet(KEYS_WIDGET_IDS,ids)
        editor.apply()
    }

    fun getIds() = pref.getStringSet(KEYS_WIDGET_IDS,hashSetOf())

    var page : Int
        get() = pref.getInt(page_key,1)
        set(value) {
            if(value <= 1)
                pref.edit().putInt(page_key,1).commit()
            else
                pref.edit().putInt(page_key,value).commit()
        }

}