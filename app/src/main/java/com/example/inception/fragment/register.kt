package com.example.inception.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.Toast
import com.example.inception.R
import com.example.inception.activity.LandingPage
import kotlinx.android.synthetic.main.fragment_register.*


class register : Fragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val adapter = activity?.let { ArrayAdapter.createFromResource(it,R.array.spinner_role,android.R.layout.simple_spinner_dropdown_item) }
        role_spinner.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var objectView = inflater.inflate(R.layout.fragment_register, container, false)

        var imageButton = objectView.findViewById<RelativeLayout>(R.id.register)
        imageButton.setOnClickListener {
            var LandingPageIntent = Intent(activity,LandingPage::class.java)
            startActivity(LandingPageIntent)
        }


        return objectView
    }

}