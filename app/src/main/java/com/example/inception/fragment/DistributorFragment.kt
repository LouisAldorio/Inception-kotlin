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
import com.example.inception.GetDistributorQuery
import com.example.inception.GetSupplierQuery
import com.example.inception.R
import com.example.inception.adaptor.DistributorRecycleViewAdaptor
import com.example.inception.adaptor.DistributorWantedItemsRecycleViewAdaptor
import com.example.inception.adaptor.SupplierRecycleViewAdaptor
import com.example.inception.api.apolloClient
import kotlinx.android.synthetic.main.distributor_item_layout.*

class DistributorFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_distributor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenResumed {

            val response = try {
                apolloClient(requireContext()).query(GetDistributorQuery(role = "Distributor")).await()
            }catch (e: ApolloException){
                Log.d("Distributor List", "Failure", e)
                null
            }

            val distributors = response?.data?.users_by_role?.filterNotNull()
            if(distributors != null && !response.hasErrors()) {
                view.findViewById<ProgressBar>(R.id.progress_bar_distributor).visibility = View.GONE

                //distributors
                val distributorRv = view.findViewById<RecyclerView>(R.id.rv_distributor)
                val adapter = DistributorRecycleViewAdaptor(requireContext(),distributors)

                distributorRv.layoutManager = LinearLayoutManager(requireContext())
                distributorRv.adapter = adapter


            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = DistributorFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}