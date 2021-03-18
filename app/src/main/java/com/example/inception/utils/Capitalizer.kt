package com.example.inception.utils

import android.text.method.TextKeyListener

class Capitalizer {

    fun Capitalize(text : String) : String{
        val arr = text.split(" ").toTypedArray()

        var result : String = ""
        for(item in arr){
            result += item.substring(0,1).toUpperCase() + item.substring(1).toLowerCase() + " "
        }
        return result
    }
}