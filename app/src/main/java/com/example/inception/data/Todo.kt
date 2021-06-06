package com.example.inception.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//pertama-tama buat sebuah class parcelable.
@Parcelize
class Todo : Parcelable {
    var id: Int = 0
    var content: String = ""
    var status: Int = 0
}