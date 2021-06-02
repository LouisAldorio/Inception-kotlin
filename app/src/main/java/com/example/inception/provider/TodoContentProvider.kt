package com.example.inception.provider

import DB.Todo
import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import com.example.inception.sql.DBHelper


//karena kita akan membagikan data ke aplikasi lain, maka kita implementasi
// ContentProvider() sbg penyedia data atau penyedia akses ke database.
//ContentProvier ini akan merangkum dan menyediakan data ke aplikasi melalui ContentResolver.
class TodoContentProvider : ContentProvider() {

    private var dbHelper : DBHelper? = null

    //digunakan utk menangani perintah INSERT
    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        var id : Long = -1
        //lakukan pengecekan apakah uri sudah sesuai
        if(uri == CONTENT_URI){
            //gunakan writeableDatabase untuk menginsert baris baru ke database
            val sqlDB = dbHelper!!.writableDatabase
            //kemudian masukkan data ke table dgn perintah insert yg akan mengembalikan nilai Long
            //jika insert berhasil dilakukan, maka akan mengembalikan id dari baris yg diinsert
            //jika insert gagal, maka akan mengembalikan -1
            id = sqlDB.insert(DB.Todo.TodoTable.TABLE_TODO,null,values)
        }
        //notify bahwa baris telah diperbarui kemudian sinkronkan perubahan ke jaringan.
        context!!.contentResolver.notifyChange(uri, null)
        //lalu kembalikan nilai uri dari data baru yang diinsert
        return Uri.parse("${DB.Todo.TodoTable.TABLE_TODO}/${id}")
    }

    //menangani perintah query dari user
    override fun query(
        uri: Uri,
        projection: Array<out String>?, //daftar kolom yg akan dipilih dan disimpan ke cursor
        selection: String?,    //kriteria atau filter baris yg akan dipilih
        selectionArgs: Array<out String>?,  //argumen atau nilai dri kriteria nya
        sortOrder: String?  //urutan bagaiaman data akan disusun di cursor
    ): Cursor? {
        //kita akan menggunakan SQLiteQueryBuilder untuk membuat query SQL untuk dikirim ke database
        var queryBuilder = SQLiteQueryBuilder()
        //tentukan table mana yg mau digunakan
        queryBuilder.tables = DB.Todo.TodoTable.TABLE_TODO
        //lakukan query dgn mengirimkan semua parameternya
        var cursor = queryBuilder.query(dbHelper?.readableDatabase,
            projection, selection, selectionArgs,null, null,sortOrder)
        //panggil cursor dan notifikasi Uri bahwa sudah terjadi perubahan.
        cursor.setNotificationUri(context?.contentResolver ,uri)
        //hasil return ini nanti akan ditangkap oleh app2 melalui contentResolver.
        return cursor
    }

    //menginisialisasi ContentProvider
    override fun onCreate(): Boolean {
        dbHelper = DBHelper(context!!)
        //karena ada menggunakan onCreate, maka return true
        return true
    }

    //digunakan utk membentuk query UPDATE
    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        //inisialisasi varibale untuk menampung jumlah baris yang berhasil di updated
        var rowsUpdated : Int = 0
        val sqlDB = dbHelper!!.writableDatabase
        if(uri == CONTENT_URI){
            //perintah update ini akan mengembalikan jumlah baris yg terupdate
            rowsUpdated = sqlDB.update(Todo.TodoTable.TABLE_TODO,
                values,
                selection,
                selectionArgs)
        }
        context!!.contentResolver.notifyChange(uri, null)
        //kembalikn jumlah baris yg terupdate
        return rowsUpdated
    }

    //digunakan utk melakukan query DELETE
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        //inisialisasi varibale untuk menampung jumlah baris yang berhasil di hapus
        var rowsDeleted : Int = 0
        val sqlDB = dbHelper!!.writableDatabase
        if(uri == CONTENT_URI){
            //panggil perintah delete dan kirimkan table, kriteria, serta
            // argumen untuk menghapus baris yg diinginkan
            rowsDeleted = sqlDB.delete(Todo.TodoTable.TABLE_TODO,
                selection,
                selectionArgs)
        }
        context!!.contentResolver.notifyChange(uri, null)
        //kembalikan jumla baris yg terhapus
        return rowsDeleted
    }

    //digunakan utk mengambil type data Uri yg dibaca
    override fun getType(uri: Uri): String? {
        TODO("Not yet implemented")
    }

    companion object{
        //tentukan authoritynya
        val AUTHORITY = "com.example.inception.provider.provider.TodoContentProvider"
        //tentukan batasan untuk contentprovider agar hanya dapat mengakses table tertentu
        private val TODO_TABLE = DB.Todo.TodoTable.TABLE_TODO
        //bentuk content uri yg akan digunakan utk menghubungkan antara app dan todoapp
        val CONTENT_URI : Uri = Uri.parse("content://$AUTHORITY/$TODO_TABLE")
    }
}