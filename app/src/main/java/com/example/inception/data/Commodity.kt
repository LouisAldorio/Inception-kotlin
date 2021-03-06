package com.example.inception.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//data class dari Commodity di berikan anotasi @parcelize dan di inherit dari kelas Parcelable agar dapat digunakan sebagai parcelable
@Parcelize
data class Commodity (
    val name: String,
    val image: String?
): Parcelable