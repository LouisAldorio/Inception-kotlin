package com.example.inception.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import okhttp3.MultipartBody

@Parcelize
data class UploadParams (
    val type_adapter : String,
    val file:  String
): Parcelable