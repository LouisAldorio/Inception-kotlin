package com.example.inception.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.example.inception.CustomLayoutManager.CustomAutoScrollCenterZoomLayoutManager
import com.example.inception.GetSupplierQuery
//import com.example.inception.GetSupplierQuery
import com.example.inception.GetUserByUsernameQuery
import com.example.inception.R
import com.example.inception.adaptor.SupplierRecycleViewAdaptor
import com.example.inception.api.apolloClient
import kotlinx.android.synthetic.main.fragment_commodity_detail.view.*
import kotlinx.android.synthetic.main.fragment_supplier.view.*


class SupplierFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var objectView = inflater.inflate(R.layout.fragment_supplier, container, false)
        return objectView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = CustomAutoScrollCenterZoomLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        lifecycleScope.launchWhenResumed {

            val response = try {
                apolloClient(requireContext()).query(GetSupplierQuery(role = "Supplier")).await()
            }catch (e: ApolloException){
                Log.d("SupplierList", "Failure", e)
                null
            }


            val suppliers = response?.data?.users_by_role?.filterNotNull()
            if(suppliers != null && !response.hasErrors()) {
                view.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.GONE


                val supplierRv = view.findViewById<RecyclerView>(R.id.rv_supplier)
                val adapter = SupplierRecycleViewAdaptor(suppliers)

                val snapHelper = LinearSnapHelper()
                snapHelper.attachToRecyclerView(supplierRv)

                supplierRv.layoutManager = layoutManager
                supplierRv.adapter = adapter
            }
        }
    }
}