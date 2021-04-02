package com.example.inception.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Commodity (
    val name: String,
    val image: List<String?>,
    val unit_price: String,
    val unit_type: String,
    val min_purchase: String,
    val user : CommodityUser,
    val description: String?
): Parcelable