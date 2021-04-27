package com.example.inception

import android.content.Context
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.test.platform.app.InstrumentationRegistry
import com.example.inception.`interface`.SupplierInterface
import com.example.inception.presenter.SupplierPresenter
import kotlinx.coroutines.CoroutineScope
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import kotlin.coroutines.CoroutineContext

@RunWith(MockitoJUnitRunner::class)
class SupplierLoadTest {

    val view : SupplierInterface = Mockito.mock(SupplierInterface::class.java)
    val presenter = SupplierPresenter(InstrumentationRegistry.getInstrumentation().targetContext,view)

    @Test
    fun LoadSupplierList(){
//        presenter.LoadSupplierList()
    }
}