package com.example.inception.data

import com.google.gson.annotations.SerializedName

data class UploadResponse (

    @SerializedName("ImageURL")
    var imageURL:String,

    @SerializedName("Status")
    var status : String,

    @SerializedName("Width")
    var width : String,

    @SerializedName("Height")
    var height : String

)