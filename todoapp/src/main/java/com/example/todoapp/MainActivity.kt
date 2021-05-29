package com.example.todoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.ContentProviderTransaction.TodoTransaction
import com.example.todoapp.adapter.TodoRecycleviewAdapter
import com.example.todoapp.data.Todo
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var todoTrans : TodoTransaction? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        todoTrans = TodoTransaction(this)

        add_todo.setOnClickListener {
            todoTrans!!.insert(new_todo.text.toString())
            FetchTodo()
        }
    }

    override fun onResume() {
        super.onResume()
        FetchTodo()
    }

    fun FetchTodo(){
        var TodoList= mutableListOf<Todo>()
        for(item in todoTrans!!.GetAllTodo()){
            var todo = Todo()
            todo.status = item.status
            todo.id = item.id
            todo.content = item.content
            TodoList.add(todo)
        }

        val adapterTodo = TodoRecycleviewAdapter(this, TodoList, {id -> deleteTodo(id)},{ id,status -> updateTodo(id,status)})
        todo_rv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = adapterTodo
        }
    }

    fun deleteTodo(id: Int){
        val res = todoTrans!!.delete(id)
        if(res == true){
            Toast.makeText(this, "Todo deleted", Toast.LENGTH_SHORT).show()
            FetchTodo()
        }else{
            Toast.makeText(this, "Fail to delete TODO", Toast.LENGTH_SHORT).show()
        }
    }

    fun updateTodo(id : Int , status : Int){
        val res = todoTrans!!.update(id,status)
        if(res == true){
            Toast.makeText(this, "Todo Updated", Toast.LENGTH_SHORT).show()
            FetchTodo()
        }else{
            Toast.makeText(this, "Fail to update TODO", Toast.LENGTH_SHORT).show()
        }
    }
}