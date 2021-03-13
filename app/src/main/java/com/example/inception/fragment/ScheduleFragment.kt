package com.example.inception.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
//import com.example.inception.GetScheduleQuery
import com.example.inception.R
import com.example.inception.adaptor.ScheduleRecycleViewAdaptor
import com.example.inception.api.apolloClient
import kotlinx.android.synthetic.main.fragment_register.view.*
import kotlinx.android.synthetic.main.fragment_schedule.view.*

class ScheduleFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
    }


    companion object {

        @JvmStatic
        fun newInstance() =
            ScheduleFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}