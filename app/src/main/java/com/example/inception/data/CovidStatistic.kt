package com.example.inception.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CovidStatistic(
    val country : String,
    val code : String,
    val confirmed : String,
    val recovered : String,
    val critical : String,
    val deaths : String,
    val latitude : String,
    val longitude : String,
    val lastChange : String,
    val lastUpdate : String
) : Parcelable