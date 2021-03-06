package com.example.inception.adaptor

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.inception.fragment.login
import com.example.inception.fragment.register

class RegistrationPagerAdaptor(activity: AppCompatActivity): FragmentStateAdapter(activity) {

    var pages = listOf(register(), login())
    override fun getItemCount(): Int {
        return pages.size
    }

    override fun createFragment(position: Int): Fragment {
        return pages[position]
    }

    
}