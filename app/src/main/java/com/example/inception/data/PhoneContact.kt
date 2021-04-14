package com.example.inception.data

import android.graphics.Bitmap

data class PhoneContact (
    var id : String,
    var first_name : String,
    var last_name : String,
    var avatar_path : Bitmap,
    var email : String,
    val phone_number : String
)