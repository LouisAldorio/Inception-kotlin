package com.example.inception.sql

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.inception.data.Todo

class DBHelper(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME,
    null, DATABASE_VERSION
) {
    companion object {
        private val DATABASE_VERSION = 2
        private val DATABASE_NAME = "inception-todo.db"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        var CREATE_USER_TABLE = ("CREATE TABLE ${DB.Todo.TodoTable.TABLE_TODO} " +
                "(${DB.Todo.TodoTable.COLUMN_ID} INTEGER PRIMARY KEY," +
                "${DB.Todo.TodoTable.COLUMN_CONTENT} TEXT," +
                "${DB.Todo.TodoTable.COLUMN_TODO_STATUS} INTEGER)")
        db!!.execSQL(CREATE_USER_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(
            "DROP TABLE IF EXISTS " +
                    "${DB.Todo.TodoTable.TABLE_TODO}"
        )
        onCreate(db)
    }

    fun CreateNewTodo(todo : Todo) : Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(DB.Todo.TodoTable.COLUMN_CONTENT, todo.content)
            put(DB.Todo.TodoTable.COLUMN_TODO_STATUS, todo.status)
        }
        val success = db.insert(
            DB.Todo.TodoTable.TABLE_TODO,
            null, contentValues
        )
        db.close()
        return success
    }

    fun UpdateTodoStatus(todo_id: Int , status : Int){
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DB.Todo.TodoTable.COLUMN_TODO_STATUS, status)
        db.update(DB.Todo.TodoTable.TABLE_TODO, contentValues, "_id = ?", arrayOf(todo_id.toString()))
    }

    fun deleteTodo(todo_id: Int) : Int {
        val db = this.writableDatabase
        return db.delete(DB.Todo.TodoTable.TABLE_TODO,"_id = ?", arrayOf(todo_id.toString()))
    }

    fun GetTodoList() : MutableList<Todo> {
        val todoList = ArrayList<Todo>()

        val queryString = "SELECT * FROM ${DB.Todo.TodoTable.TABLE_TODO}"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(queryString, null)
        } catch (e: SQLException) {
            return ArrayList()
        }

        if (cursor.moveToFirst()) {
            do {
                var todo = Todo()
                todo.id = cursor.getInt(cursor.getColumnIndex(DB.Todo.TodoTable.COLUMN_ID))
                todo.content = cursor.getString(cursor.getColumnIndex(DB.Todo.TodoTable.COLUMN_CONTENT))
                todo.status = cursor.getInt(cursor.getColumnIndex(DB.Todo.TodoTable.COLUMN_TODO_STATUS))
                todoList.add(todo)
            } while (cursor.moveToNext())
        }

        return todoList
    }
}