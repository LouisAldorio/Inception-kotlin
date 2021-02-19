package com.example.inception.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.inception.R
import com.example.inception.objectClass.User
import kotlinx.android.synthetic.main.fragment_profile.view.*


class ProfileFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var objectView = inflater.inflate(R.layout.fragment_profile, container, false)

        objectView.logout.setOnClickListener {
            User.removeToken(requireContext())
            requireActivity().finish()
        }

        return objectView
    }


    companion object {

        @JvmStatic
        fun newInstance() =
            ProfileFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}