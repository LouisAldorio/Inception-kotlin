package com.example.todoapp.DB

import android.net.Uri
import android.provider.BaseColumns

object Todo {
    class TodoTable: BaseColumns {
        companion object {
            val TABLE_TODO = "todos"
            val COLUMN_ID: String = "_id"
            val COLUMN_CONTENT: String = "content"
            val COLUMN_TODO_STATUS: String = "status"
        }
    }
}

class TodoContentProviderURI {
    val AUTHORITY = "com.example.inception.provider.provider.TodoContentProvider"
    private val TODO_TABLE = Todo.TodoTable.TABLE_TODO
    val CONTENT_URI : Uri = Uri.parse("content://$AUTHORITY/$TODO_TABLE")
}