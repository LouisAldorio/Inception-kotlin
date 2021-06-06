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
    //contentResolver ini nnatinya akan melakukan permintaan data ke provider
    private val contentResolver = context.contentResolver

    //menampilkan seluruh data
    fun GetAllTodo() : List<Todo>{
        //inisialisasi sebuah arrayList untuk menampung todoo nantinya
        var todoList = ArrayList<Todo>()
        //tentukan daftar kolom yg akan dipilih dan disimpan ke cursor
        var Projection = arrayOf(COLUMN_ID, COLUMN_CONTENT, COLUMN_TODO_STATUS)
        //jalankan query
        var cursor = contentResolver.query(TodoContentProviderURI().CONTENT_URI,
            Projection,null,null,null)
        //pastikan cursor tidak kosong atua null
        if(cursor != null){
            //psatikan cursor berada di posisi awal agar semua data dapat dibaca
            if (cursor.moveToFirst()) {
                do {
                    var todo = Todo()
                    todo.id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                    todo.content = cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT))
                    todo.status = cursor.getInt(cursor.getColumnIndex(COLUMN_TODO_STATUS))
                    //simpan data yang sudah ditampung tadi ke dalam todoList
                    todoList.add(todo)  //geser cursor ke baris selanjutnya

                } while (cursor.moveToNext())
            }

        }
        return todoList
    }

    fun insert(todo_content: String){
        //tentukan data yang akan diisi ke dalam table
        val values = ContentValues()
        values.put(COLUMN_CONTENT, todo_content)
        values.put(COLUMN_TODO_STATUS,0)
        //panggil perintah insert melaluoi contentResolver dan kirimkan data yg akan diinsert
        contentResolver.insert(TodoContentProviderURI().CONTENT_URI,values)
    }

    fun update(id : Int, status: Int) : Boolean{
        var result = false
        val values = ContentValues()
        values.put(COLUMN_ID, id)
        values.put(COLUMN_TODO_STATUS,status)
        //tentukan id sbg kriteria baris yg akan diupdate
        val selection = "_id = \"" + id.toString() + "\""
        //panggil perintah update melalui contentresolver dan simpan nilai dari
        // update ke dalam varibale
        val rowsUpdated = contentResolver.update(TodoContentProviderURI().CONTENT_URI,
            values,selection,null)
        //cek apakah ada baris yg terupdate
        if (rowsUpdated > 0)
            result = true

        return result
    }

    fun delete(id : Int) : Boolean {
        var result = false
        //tentukan id sbg kriteria baris yg akan dihapus
        val selection = "_id = \"" + id.toString() + "\""
        //panggil perintah delete melalui contentresolver dan simpan nilai dari
        // delete ke dalam varibale
        val rowsDeleted = contentResolver.delete(TodoContentProviderURI().CONTENT_URI,
            selection, null)
        //cek apakah ada baris yg terhapus
        if (rowsDeleted > 0)
            result = true

        return result
    }
}