package com.example.inception.provider

import DB.Todo
import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import com.example.inception.sql.DBHelper



class TodoContentProvider : ContentProvider() {

    private var dbHelper : DBHelper? = null

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        var id : Long = -1
        if(uri == CONTENT_URI){
            val sqlDB = dbHelper!!.writableDatabase
            id = sqlDB.insert(DB.Todo.TodoTable.TABLE_TODO,null,values)
        }

        context!!.contentResolver.notifyChange(uri, null)
        return Uri.parse("${DB.Todo.TodoTable.TABLE_TODO}/${id}")
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {

        var queryBuilder = SQLiteQueryBuilder()
        queryBuilder.tables = DB.Todo.TodoTable.TABLE_TODO

        var cursor = queryBuilder.query(dbHelper?.readableDatabase,
            projection, selection, selectionArgs,null, null,sortOrder)

        cursor.setNotificationUri(context?.contentResolver ,uri)
        return cursor
    }

    override fun onCreate(): Boolean {
        dbHelper = DBHelper(context!!)
        return true
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        var rowsUpdated : Int = 0
        val sqlDB = dbHelper!!.writableDatabase
        if(uri == CONTENT_URI){
            rowsUpdated = sqlDB.update(Todo.TodoTable.TABLE_TODO,
                values,
                selection,
                selectionArgs)
        }
        context!!.contentResolver.notifyChange(uri, null)
        return rowsUpdated
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        var rowsDeleted : Int = 0
        val sqlDB = dbHelper!!.writableDatabase

        if(uri == CONTENT_URI){
            rowsDeleted = sqlDB.delete(Todo.TodoTable.TABLE_TODO,
                selection,
                selectionArgs)
        }
        context!!.contentResolver.notifyChange(uri, null)
        return rowsDeleted
    }

    override fun getType(uri: Uri): String? {
        TODO("Not yet implemented")
    }

    companion object{
        val AUTHORITY = "com.example.inception.provider.provider.TodoContentProvider"
        private val TODO_TABLE = DB.Todo.TodoTable.TABLE_TODO
        val CONTENT_URI : Uri = Uri.parse("content://$AUTHORITY/$TODO_TABLE")
    }
}