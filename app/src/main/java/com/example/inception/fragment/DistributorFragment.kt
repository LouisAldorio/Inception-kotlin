package com.example.inception.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.inception.R

class DistributorFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_distributor, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = DistributorFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}