package com.example.todoapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Todo : Parcelable {
    var id: Int = 0
    var content: String = ""
    var status: Int = 0
}