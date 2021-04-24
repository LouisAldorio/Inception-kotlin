package com.example.inception.data

//kita definiskan model untuk design MVP kita, disini kita akan melakukan fetch Data supllier yang dimana proses fetch akan kita pisahkan
// dari view, dan model nya
data class Supplier (
    val photoUrl : String,
    val username : String
)
