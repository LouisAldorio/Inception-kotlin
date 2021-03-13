package com.example.inception.adaptor

import agency.tango.android.avatarview.views.AvatarView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
//import com.example.inception.GetScheduleQuery
import com.example.inception.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.schedule_item_layout.view.*

//param = val schedules: List<GetScheduleQuery.Schedule_by_user>
class ScheduleRecycleViewAdaptor(): RecyclerView.Adapter<ScheduleHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleHolder {
        return ScheduleHolder(LayoutInflater.from(parent.context).inflate(R.layout.schedule_item_layout,parent,false))
    }

    override fun getItemCount(): Int {
//        return schedules.size
        return 0
    }

    override fun onBindViewHolder(holder: ScheduleHolder, position: Int) {
//        Picasso.get().load(schedules[position].involved_users[0]?.profile_image).into(holder.involvedUser1)
//        Picasso.get().load(schedules[position].involved_users[1]?.profile_image).into(holder.involvedUser2)
//
//        holder.scheduleName.text = schedules[position].schedule_name
//        holder.commodityName.text = schedules[position].commodity_name
    }
}


class ScheduleHolder(view : View) : RecyclerView.ViewHolder(view){
    var scheduleName : TextView
    var involvedUser1 : AvatarView
    var involvedUser2 : AvatarView
    var commodityName : TextView

    init {
        scheduleName = view.schedule_name
        involvedUser1 = view.involved_users_1
        involvedUser2 = view.involved_users_2
        commodityName = view.commodity_name

    }


}