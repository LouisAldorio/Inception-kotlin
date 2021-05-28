package com.example.inception.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.inception.R
import com.example.inception.ROOM.DBHelper.TodoHelper
import com.example.inception.ROOM.entities.TodoRoom
import com.example.inception.adaptor.TodolistAdapter
import com.example.inception.data.Todo
import kotlinx.android.synthetic.main.activity_note_todo.*
import kotlinx.android.synthetic.main.activity_note_todo_r_o_o_m.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class NoteTodoROOMActivity : AppCompatActivity() {

    var dbROOMHelper : TodoHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Private Todo Notes In ROOM"

        setContentView(R.layout.activity_note_todo_r_o_o_m)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        dbROOMHelper  = Room.databaseBuilder(
            this,
            TodoHelper::class.java,
            "TodoROOM.db"
        ).build()

        add_todo_room.setOnClickListener {
            InsertItemToList(
                TodoRoom(
                content = new_todo_room.text.toString(),
                    status = 0
            ))
            GetTodoList()
            new_todo_room.text?.clear()
        }
    }

    override fun onStart() {
        super.onStart()
        GetTodoList()
    }

    fun DeleteItemFromList(id: Int){
        doAsync {
            dbROOMHelper!!.todoDao().DeleteById(id)
            uiThread {
                GetTodoList()
            }
        }
    }

    fun UpdateTodoStatus(id : Int, status: Int){
        doAsync {
            dbROOMHelper!!.todoDao().UpdateTodoStatus(id, status)
        }
    }

    fun InsertItemToList(newItem: TodoRoom){
        doAsync {
            dbROOMHelper!!.todoDao().insertAll(newItem)
            uiThread {
                GetTodoList()
                Toast.makeText(this@NoteTodoROOMActivity, "Data Added", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun GetTodoList(){

        doAsync {
            val todoList = dbROOMHelper!!.todoDao().getAllData()
            uiThread {
                //convert to List<Todo>
                var tempList = mutableListOf<Todo>()
                for(item in todoList){
                    val temp = Todo()
                    temp.id = item.id
                    temp.content = item.content
                    temp.status = item.status
                    tempList.add(temp)
                }

                val todo_adapter = TodolistAdapter(this@NoteTodoROOMActivity, tempList, { GetTodoList() },{ id, status ->
                    UpdateTodoStatus(id,status)
                },{ id ->
                    DeleteItemFromList(id)
                })

                todo_room_rv.apply {
                    layoutManager = LinearLayoutManager(this@NoteTodoROOMActivity)
                    adapter = todo_adapter
                }
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                onBackPressed()
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}