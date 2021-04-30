package com.example.inception.fragment

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
//import com.example.inception.GetScheduleQuery
import com.example.inception.R
import com.example.inception.adaptor.ScheduleRecycleViewAdaptor
import com.example.inception.api.apolloClient
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_register.view.*
import kotlinx.android.synthetic.main.fragment_schedule.view.*

class ScheduleFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Picasso.get().load("http://128.199.174.165:8081/photo/DSCF2979-067277033.jpg").into(view.temp)

//        lifecycleScope.launchWhenResumed {
//            val response = try {
//                apolloClient(requireContext()).query(GetScheduleQuery()).await()
//            }catch (e : ApolloException) {
//                Log.d("Schedule List", "Failure", e)
//                null
//            }
//
//            val Schedules = response?.data?.schedule_by_user?.filterNotNull()
//
//            if (Schedules != null && !response.hasErrors()) {
//                view.progress_bar_schedule.visibility = View.GONE
//
//                val scheduleRv = view.rv_schedule
//                val adapter = ScheduleRecycleViewAdaptor(Schedules)
//
//                scheduleRv.adapter = adapter
//                scheduleRv.layoutManager = LinearLayoutManager(requireContext())
//            }else {
//                Log.i("error schedule", response?.errors?.get(0)?.message.toString())
//            }
//        }

        //job Scheduler
    }
}