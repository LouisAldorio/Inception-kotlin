package com.example.todoapp.ContentProviderTransaction

import android.content.ContentValues
import android.content.Context
import android.text.BoringLayout
import com.example.todoapp.DB.Todo.TodoTable.Companion.COLUMN_CONTENT
import com.example.todoapp.DB.Todo.TodoTable.Companion.COLUMN_ID
import com.example.todoapp.DB.Todo.TodoTable.Companion.COLUMN_TODO_STATUS
import com.example.todoapp.DB.TodoContentProviderURI
import com.example.todoapp.data.Todo
import kotlinx.android.synthetic.main.activity_main.*

class TodoTransaction(context: Context) {
    private val contentResolver = context.contentResolver

    fun GetAllTodo() : List<Todo>{
        var todoList = ArrayList<Todo>()
        var Projection = arrayOf(COLUMN_ID, COLUMN_CONTENT, COLUMN_TODO_STATUS)

        var cursor = contentResolver.query(TodoContentProviderURI().CONTENT_URI, Projection,null,null,null)

        if(cursor != null){

            if (cursor.moveToFirst()) {
                do {
                    var todo = Todo()
                    todo.id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                    todo.content = cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT))
                    todo.status = cursor.getInt(cursor.getColumnIndex(COLUMN_TODO_STATUS))

                    todoList.add(todo)

                } while (cursor.moveToNext())
            }

        }
        return todoList
    }

    fun insert(todo_content: String){
        val values = ContentValues()
        values.put(COLUMN_CONTENT, todo_content)
        values.put(COLUMN_TODO_STATUS,0)

        contentResolver.insert(TodoContentProviderURI().CONTENT_URI,values)
    }

    fun update(id : Int, status: Int) : Boolean{
        var result = false

        val values = ContentValues()
        values.put(COLUMN_ID, id)
        values.put(COLUMN_TODO_STATUS,status)

        val selection = "_id = \"" + id.toString() + "\""

        val rowsUpdated = contentResolver.update(TodoContentProviderURI().CONTENT_URI,
            values,selection,null)

        if (rowsUpdated > 0)
            result = true

        return result
    }

    fun delete(id : Int) : Boolean {
        var result = false

        val selection = "_id = \"" + id.toString() + "\""

        val rowsDeleted = contentResolver.delete(TodoContentProviderURI().CONTENT_URI,
            selection, null)

        if (rowsDeleted > 0)
            result = true

        return result
    }
}