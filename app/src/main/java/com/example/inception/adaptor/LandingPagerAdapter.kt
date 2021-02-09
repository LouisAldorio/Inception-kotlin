package com.example.inception.adaptor

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.inception.fragment.ProfileFragment
import com.example.inception.fragment.ScheduleFragment
import com.example.inception.fragment.register


class LandingPagerAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {

    var pages = listOf(register(),ScheduleFragment(),ProfileFragment())
    override fun getItemCount(): Int {
        return pages.size
    }

    override fun createFragment(position: Int): Fragment {
        return pages[position]
    }
}