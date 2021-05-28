package com.example.todoapp

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.todoapp.ContentProviderTransaction.TodoTransaction
import com.example.todoapp.DB.Todo
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val todo = TodoTransaction(this)

        var result = ""
        for(str in todo.GetAllTodo()){
            result += str.content + " "
        }

        Log.i("Result",result)

        add_new_todo.setOnClickListener {
            todo.insert(todo_content.text.toString())
        }
    }
}