package com.example.inception.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.inception.CustomLayoutManager.CustomAutoScrollCenterZoomLayoutManager
import com.example.inception.R
import com.example.inception.`interface`.SupplierInterface
import com.example.inception.adaptor.SupplierRecycleViewAdaptor
import com.example.inception.data.Supplier
import com.example.inception.presenter.SupplierPresenter

//pada fragment supplier yang akan merender list supplier kita implementasi kan Supplier Interface yang telah kita buat sebelumnya
class SupplierFragment : Fragment()
//    ,SupplierInterface
{

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var objectView = inflater.inflate(R.layout.fragment_supplier, container, false)
        return objectView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        //init object dari class presenter dan berikan context , serta this, karena kita telah mengimplementasi SupplierInterface pada fragment
//        var presenter = SupplierPresenter(requireContext(),this)
//        //panggil fungsi yang sebelumnya telah kita buat di presenter, dan jangan lupa memberikan Scope Coroutine, disini saya menggunakan lifecycleScope
//        presenter.LoadSupplierList(lifecycleScope)
    }
    // disinilah kita akan mengoverride  fungsi yang telah ktia buat pada interface
//    override fun RenderSupplierList(SupplierModel: MutableList<Supplier>) {
//        //ketika menerima dari model, maka masukkan data kedalam adapter recycle view dan render seperti biasanya
//        //dengan begini, kita telah memisahkan, model(data), view serta logic proses(presenter)
//        val layoutManager = CustomAutoScrollCenterZoomLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
//        requireView().findViewById<ProgressBar>(R.id.progress_bar).visibility = View.GONE
//
//        val supplierRv = requireView().findViewById<RecyclerView>(R.id.rv_supplier)
//        val adapter = SupplierRecycleViewAdaptor(SupplierModel)
//
//        val snapHelper = LinearSnapHelper()
//        snapHelper.attachToRecyclerView(supplierRv)
//
//        supplierRv.layoutManager = layoutManager
//        supplierRv.adapter = adapter
//    }
}