package com.example.inception.`interface`

import com.example.inception.data.Supplier

//buat sebuah interface
interface SupplierInterface {
    //didalam nya akan berisi member function yang akan bertugas unutk merender data yang dikirmkan model ke view
    //nantinya function ini akan kita override
    fun RenderSupplierList(SupplierModel : MutableList<Supplier>)
}