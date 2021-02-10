package com.example.inception.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import com.example.inception.R
import com.example.inception.activity.LandingPage


class login : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val binding = DataBindingUtil.inflate<FragmentLoginBinding>(
//            inflater, R.layout.fragment_login, container, false)
        // Inflate the layout for this fragment

        var objectView = inflater.inflate(R.layout.fragment_login, container, false)

        var imageButton = objectView.findViewById<RelativeLayout>(R.id.login)
        imageButton.setOnClickListener {
            var LandingPageIntent = Intent(activity, LandingPage::class.java)
            startActivity(LandingPageIntent)
        }
        return objectView
    }


}