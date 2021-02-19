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
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.example.inception.GetSupplierQuery
import com.example.inception.GetUserByUsernameQuery
import com.example.inception.R
import com.example.inception.adaptor.SupplierRecycleViewAdaptor
import com.example.inception.api.apolloClient


class SupplierFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var objectView = inflater.inflate(R.layout.fragment_supplier, container, false)
        return objectView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

                supplierRv.layoutManager = LinearLayoutManager(requireContext())
                supplierRv.adapter = adapter
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            SupplierFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}