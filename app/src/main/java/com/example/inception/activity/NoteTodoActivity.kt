package com.example.inception.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inception.R
import com.example.inception.adaptor.TodolistAdapter
import com.example.inception.data.Todo
import com.example.inception.sql.DBHelper
import kotlinx.android.synthetic.main.activity_note_todo.*

class NoteTodoActivity : AppCompatActivity() {

    var dbHelper : DBHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Private Todo Notes"

        setContentView(R.layout.activity_note_todo)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        dbHelper = DBHelper(this)

        add_todo.setOnClickListener {
            var newTodo = Todo()

            if(new_todo.text.toString().trim() != "") {
                newTodo.content = new_todo.text.toString()
                newTodo.status = 0

                val result = dbHelper!!.CreateNewTodo(newTodo)
                if(result !=- 1L){
                    Toast.makeText(this, "New Todo Added", Toast.LENGTH_SHORT).show()
                    GetTodoList()
                    it.hideKeyboard()
                } else{
                    Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show()
                    it.hideKeyboard()
                }
                new_todo.text!!.clear()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        GetTodoList()
    }

    fun DeleteItemFromList(id: Int){
        dbHelper!!.deleteTodo(id)
    }

    fun UpdateTodoStatus(id : Int, status: Int){
        dbHelper!!.UpdateTodoStatus(id, status)
    }

    fun GetTodoList(){
        lifecycleScope.launchWhenResumed {
            val todoList = dbHelper!!.GetTodoList()
            val todo_adapter = TodolistAdapter(this@NoteTodoActivity, todoList, { GetTodoList() },{ id, status ->
                UpdateTodoStatus(id,status)
            },{ id ->
                DeleteItemFromList(id)
            })

            todo_rv.apply {
                layoutManager = LinearLayoutManager(this@NoteTodoActivity)
                adapter = todo_adapter
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