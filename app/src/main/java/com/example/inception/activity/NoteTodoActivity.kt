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
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class NoteTodoActivity : AppCompatActivity() {

    //inisialisasi DBHelper
    var dbHelper : DBHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Private Todo Notes"

        setContentView(R.layout.activity_note_todo)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        //kirimkan this yg berarti DB akan dibentuk dri MainActivity
        dbHelper = DBHelper(this)

        add_todo.setOnClickListener {
            //inisialisasi newTodo untuk menampung data baru
            var newTodo = Todo()
            //lakukan pengeceekan apakah textview kosong atau tidak
            if(new_todo.text.toString().trim() != "") {
                //jika tidak kosong, makan kita simpan datanya ke dalam newTodo
                newTodo.content = new_todo.text.toString()
                newTodo.status = 0

                //setelah itu, kita panggil CreateNewTodo yang sudah dibuat sebelumnya untuk menginsert data ke DB
//                val result = dbHelper!!.CreateNewTodo(newTodo)
                //lakukan pengecekan apakah data proses insert berhasil dilakukan atau tidak
                //jika tidak berhasil, insert akan mengembalikan nilai -1
//                if(result !=- 1L){
//                    Toast.makeText(this, "New Todo Added", Toast.LENGTH_SHORT).show()
//                    //tampilkan data
//                    GetTodoList()
//                    it.hideKeyboard()
//                } else{
//                    Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show()
//                    it.hideKeyboard()
//                }
//                new_todo.text!!.clear()

                //with transaction optimzation
                doAsync {
                    dbHelper!!.beginTransaction()
                    val success = dbHelper!!.TransactionCreateNewTodo(newTodo)
                    dbHelper!!.successTransaction()
                    dbHelper!!.endTransaction()

                    uiThread {
                        if(success !=- 1L){
                            Toast.makeText(this@NoteTodoActivity, "New Todo Added", Toast.LENGTH_SHORT).show()
                            //tampilkan data
                            GetTodoList()
                            add_todo.hideKeyboard()
                        } else{
                            Toast.makeText(this@NoteTodoActivity, "Fail To Add Todo", Toast.LENGTH_SHORT).show()
                            add_todo.hideKeyboard()
                        }
                        new_todo.text!!.clear()
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        //pada lifecycle onStart, kita panggil fun GetTodoList untuk menampilkan todoList
        GetTodoList()
    }

    fun DeleteItemFromList(id: Int){
//        dbHelper!!.deleteTodo(id)

        //with transaction optimization
        doAsync {
            dbHelper!!.beginTransaction()
            dbHelper!!.TransactionDeleteTodo(id)
            dbHelper!!.successTransaction()
            dbHelper!!.endTransaction()
        }
    }

    fun UpdateTodoStatus(id : Int, status: Int){
//        dbHelper!!.UpdateTodoStatus(id, status)

        //with transaction optimaztion
        doAsync {
            dbHelper!!.beginTransaction()
            dbHelper!!.TransactionUpdateTodoStatus(id,status)
            dbHelper!!.successTransaction()
            dbHelper!!.endTransaction()
        }
    }

    //menampilkan semua data dari table_todo
    fun GetTodoList(){
//        lifecycleScope.launchWhenResumed {
//            //baca dan tampung data dari DB ke todoList
//            val todoList = dbHelper!!.GetTodoList()
//
//            val todo_adapter = TodolistAdapter(this@NoteTodoActivity, todoList, { GetTodoList() },{ id, status ->
//                UpdateTodoStatus(id,status)
//            },{ id ->
//                DeleteItemFromList(id)
//            })
//
//            todo_rv.apply {
//                layoutManager = LinearLayoutManager(this@NoteTodoActivity)
//                adapter = todo_adapter
//            }
//        }

        //with transaction optimation
        lifecycleScope.launchWhenResumed {
            //baca dan tampung data dari DB ke todoList
            dbHelper!!.beginTransaction()
            val todoList = dbHelper!!.GetTodoList()
            dbHelper!!.successTransaction()
            dbHelper!!.endTransaction()

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