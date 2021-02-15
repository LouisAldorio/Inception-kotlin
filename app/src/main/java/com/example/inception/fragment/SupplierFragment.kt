package com.example.inception.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.inception.R


class SupplierFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var objectView = inflater.inflate(R.layout.fragment_supplier, container, false)



        return objectView
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