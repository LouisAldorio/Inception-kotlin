package com.example.inception.data

import com.google.gson.annotations.SerializedName

data class UploadResponse (

//    @SerializedName("ImageURL")
//    var imageURL:String,
//
//    @SerializedName("Status")
//    var status : String,
//
//    @SerializedName("Width")
//    var width : String,
//
//    @SerializedName("Height")
//    var height : String

    @SerializedName("status")
    var status : Boolean,

    @SerializedName("data")
    var data : List<UploadResponseData>,

    @SerializedName("error")
    var error : String
)

data class UploadResponseData (
    @SerializedName("path")
    var path : String,

    @SerializedName("serve_link")
    var serve_link : String,

    @SerializedName("downloadable")
    var downloadable : String,

    @SerializedName("downloadable_path")
    var downloadable_path : String,

    @SerializedName("sizes")
    var sizes : UploadResponseDataSizes
)

data class UploadResponseDataSizes (
    @SerializedName("Small")
    var small : String,

    @SerializedName("Medium")
    var medium : String,

    @SerializedName("Large")
    var large : String
)