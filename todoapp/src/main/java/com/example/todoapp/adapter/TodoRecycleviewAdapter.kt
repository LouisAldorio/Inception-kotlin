package com.example.todoapp.adapter

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.data.Todo

class TodoRecycleviewAdapter(val context : Context, var todos: MutableList<Todo>,val callbackDelete : (Int) -> Unit, val callBackUpdate : (Int,Int) -> Unit) : RecyclerView.Adapter<TodoHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoHolder {
        return TodoHolder(
            LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_item_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return todos.size
    }

    override fun onBindViewHolder(holder: TodoHolder, position: Int) {
        holder.content.text = todos[position].content
        if(todos[position].status == 1){
            holder.check_box.isChecked = true
            holder.content.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }else {
            holder.check_box.isChecked = false
        }


        holder.check_box.setOnClickListener {
            //lakukan pengecekan apakah checkbox tercentang atau tidak
            if(holder.check_box.isChecked == true) {
                callBackUpdate(todos[position].id, 1)
                holder.content.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }else {
                callBackUpdate(todos[position].id, 0)
                holder.content.paintFlags = 0
            }
        }

        holder.delete_button.setOnClickListener {
            callbackDelete(todos[position].id)
        }
    }
}

class TodoHolder(view: View) : RecyclerView.ViewHolder(view){
    var content : TextView
    var check_box : CheckBox
    var delete_button : ImageButton

    init {
        content = view.findViewById(R.id.todo_content)
        check_box = view.findViewById(R.id.is_done)
        delete_button = view.findViewById(R.id.delete_todo)
    }
}