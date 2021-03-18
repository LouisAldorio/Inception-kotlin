package com.example.inception.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CommodityUser(
    val username : String,
    val email : String,
    val whatsapp : String,
    val avatar : String
): Parcelable