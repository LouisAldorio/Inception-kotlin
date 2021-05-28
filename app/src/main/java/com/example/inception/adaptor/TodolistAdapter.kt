package com.example.inception.adaptor

import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.inception.R
import com.example.inception.data.Todo
import com.example.inception.sql.DBHelper


//berikut recycleview adapter yang akan membantu kita dalam merender todolist
//recycleview adapter akan menerima context dan MutableList yang akan di render
class TodolistAdapter(val context : Context,var todos: MutableList<Todo>,
                      val callbackRefresh : () -> Unit,
                      val callBackUpdate : (id: Int, status: Int) -> Unit,
                      val callBackDelete: (id : Int) -> Unit) : RecyclerView.Adapter<TodoHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoHolder {
        return TodoHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_item_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return todos.size
    }

    override fun onBindViewHolder(holder: TodoHolder, position: Int) {
        //seperti biasa kita bind data kedalam view nya
        holder.content.text = todos[position].content
        //lakukan pengecekan apakah status dari todoo = 1
        if(todos[position].status == 1){
            //jika iya, maka kita akan mencentang checkbox dan kita tambahkan garis pada text yang menandakan
            // bahwa todoo telah selesai dikerjakan
            holder.check_box.isChecked = true
            holder.content.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }else {
            //jika belum, maka checkbox tidak akn dicentang
            holder.check_box.isChecked = false
        }

        //kita akan mengubah data status pada table_todo ketika user mengklik checkbox
        holder.check_box.setOnClickListener {
            //lakukan pengecekan apakah checkbox tercentang atau tidak
            if(holder.check_box.isChecked == true) {
                callBackUpdate(todos[position].id, 1)
                holder.content.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }else {
                callBackUpdate(todos[position].id, 0)
                //jika ya, maka panggil UpdateTodoStatus lalu kirimkan kolom dan data yang akan diupdate berdasarkan id
                //tambahkan garis untuk mencoret teks todoo yang telah selesai dikerjakan
                holder.content.paintFlags = 0
            }
        }

        //ketika button delete dihapus, maka data pada DB juga akan dihapus
        holder.delete_button.setOnClickListener {

            callBackDelete(todos[position].id)
            callbackRefresh()
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