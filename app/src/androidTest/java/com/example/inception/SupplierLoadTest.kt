package com.example.inception

import androidx.test.platform.app.InstrumentationRegistry
import com.example.inception.`interface`.SupplierInterface
import com.example.inception.presenter.SupplierPresenter
import kotlinx.coroutines.GlobalScope
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

//disini kita akan melakuakn mocking pada unit test kita
//kita akan mock sebuah function dari interface yaitu function render ke view
@RunWith(MockitoJUnitRunner::class)
class SupplierLoadTest {

    //instruksikan mockito untuk melakukan mock terhadap member function yang ada didalam interface
    val view : SupplierInterface = Mockito.mock(SupplierInterface::class.java)
    //function didalam interface nantinya akan kita panggil didalam presenter setelah data berhasil diperoleh
    //maka kita akan pass kedalam member function interface yang telah di mock oleh mockito,
    // disini presenter yang kami buat menerima 2 buah paramter , yaitu context dan mock function interfacenya
    val presenter = SupplierPresenter(InstrumentationRegistry.getInstrumentation().targetContext,view)

    //lakukan testing terhadap function didalam presenter untuk melakukan load data dari Graphql
    @Test
    fun LoadSupplierList(){
        //berikan parameter ang diperlukan berupa sebuah coroutine
        //function yang melakukan load ke Graphql hanya mereturn sebuah unit, dikarenakan data yang didapat
        //akan langsung dirender kedalam view, secara asynchrounous
        val unitResult = presenter.LoadSupplierList(GlobalScope)
        assertEquals(unitResult,Unit)
    }
}