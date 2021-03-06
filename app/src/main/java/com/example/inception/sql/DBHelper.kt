package com.example.inception.sql

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.inception.data.Todo

//implementasikan SQLiteOpenHelper untuk membantu mengelola database
class DBHelper(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME,
    null, DATABASE_VERSION
) {
    companion object {
        //definisikan versi dari database
        private val DATABASE_VERSION = 2
        //definisikan nama database
        private val DATABASE_NAME = "inception-todo.db"
    }

    //onCreate akan dijalankan ketika pertama kali kita memanggil DB
    override fun onCreate(db: SQLiteDatabase?) {
        //buat perintah untuk membuat table
        var CREATE_USER_TABLE = ("CREATE TABLE ${DB.Todo.TodoTable.TABLE_TODO} " +
                "(${DB.Todo.TodoTable.COLUMN_ID} INTEGER PRIMARY KEY," +
                "${DB.Todo.TodoTable.COLUMN_CONTENT} TEXT," +
                "${DB.Todo.TodoTable.COLUMN_TODO_STATUS} INTEGER)")
        //lalu jgn lupa untuk meng-execute atau menjalakan query SQl nya agar table terbentuk
        db!!.execSQL(CREATE_USER_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //untuk mengupdate, kita akan menghapus table yang ada
        db?.execSQL(
            "DROP TABLE IF EXISTS " +
                    "${DB.Todo.TodoTable.TABLE_TODO}"
        )
        //kemudian jalankan onCreate(db) untuk membuat table lagi
        onCreate(db)
    }

    //untuk menambahkan todoo baru, kita akan membuat sebuah method
    fun CreateNewTodo(todo : Todo) : Long {
        //untuk menambahkan data baru ke DB, kita akan menggunakan writeableDatabase
        val db = this.writableDatabase
        //tentukan data yang akan diisi ke dalam table
        val contentValues = ContentValues().apply {
            put(DB.Todo.TodoTable.COLUMN_CONTENT, todo.content)
            put(DB.Todo.TodoTable.COLUMN_TODO_STATUS, todo.status)
        }
        //kemudian kita akan memasukkan data ke table dgn perintah insert yg akan mengembalikan nilai Long
        val success = db.insert(
            DB.Todo.TodoTable.TABLE_TODO,
            null, contentValues
        )
        //jgn lupa untuk menutup DB setelah proses selesai
        db.close()
        //setelah itu, kita akan mengembalikan nilai dari success untuk dilakukan
        // pengecekan apakah proses insert data berhasil dilakukan atau tidak
        return success
    }
    //method untuk mengupdate todoo
    fun UpdateTodoStatus(todo_id: Int , status : Int){
        //untuk meng-edit atua mengubah data di DB, kita akan menggunakan writeableDatabase
        val db = this.writableDatabase
        val contentValues = ContentValues()
        //kemudian kita akan mengirimkan data status yang baru ke DB untuk diupdate pada kolom status
        contentValues.put(DB.Todo.TodoTable.COLUMN_TODO_STATUS, status)
        //lakukan update table_todo pada kolom status di baris yang ditentukan
        db.update(DB.Todo.TodoTable.TABLE_TODO, contentValues, "_id = ?", arrayOf(todo_id.toString()))
    }

    //method untuk menghapus todoo
    fun deleteTodo(todo_id: Int) : Int {
        val db = this.writableDatabase
        //lakukan penghapusan baris pada table_todo yang diinginkan berdasarkan id
        return db.delete(DB.Todo.TodoTable.TABLE_TODO,"_id = ?", arrayOf(todo_id.toString()))
    }

    //method untuk membaca smua todoo
    fun GetTodoList() : MutableList<Todo> {
        //inisialisasi sebuah arrayList untuk menampung todoo nantinya
        val todoList = ArrayList<Todo>()
        //buat query untuk membaca semua data yang ada pada table_todo
        val queryString = "SELECT * FROM ${DB.Todo.TodoTable.TABLE_TODO}"
        //untuk membaca DB, kita gunakan readableDatabase
        val db = this.readableDatabase
        //kita akan membuat Cursor dengan query untuk membaca data dari database
        var cursor: Cursor? = null
        //lakukan try catch untuk pembacaan data dari database
        //Jika berhasil, data akan ditangkap oleh cursor dan dimasukkan ke dalm arrayList
        try {
            cursor = db.rawQuery(queryString, null)
        } catch (e: SQLException) {
            return ArrayList()
        }
        //sebelum dibaca, pertama-tama kita akan meletakkan cursor di posisi awal agar semua data dapat dibaca
        if (cursor.moveToFirst()) {
            do {
                //inisialisasi todoo untuk menampung data
                var todo = Todo()
                //cursor akan membaca dan menangkap data id, content dan status dari table
                todo.id = cursor.getInt(cursor.getColumnIndex(DB.Todo.TodoTable.COLUMN_ID))
                todo.content = cursor.getString(cursor.getColumnIndex(DB.Todo.TodoTable.COLUMN_CONTENT))
                todo.status = cursor.getInt(cursor.getColumnIndex(DB.Todo.TodoTable.COLUMN_TODO_STATUS))
                //kemudian kita masukkan data yang sudah ditampung tadi ke dalam todoList
                todoList.add(todo)
            } while (cursor.moveToNext()) //geser cursor ke baris selanjutnya
        }
        //kembalikan todoList
        return todoList
    }


    //transaction model ini akan memungkinkan kita untuk menjalankan banyak perintah dalam 1x proses.
    //dengan ini, proses dapat berjalan lebih cepat karena kita akan mengumpulkan semua transaksi
    // yg dilakukan lalu setelah itu baru diupdate ke database
    //fungsi untuk mempersipakan penerimaan transaksi
    fun beginTransaction(){
        this.writableDatabase.beginTransaction()
    }
    //fungsi untuk menandakan bahwa proses transasksi berhasil dilakukan
    fun successTransaction(){
        this.writableDatabase.setTransactionSuccessful()
    }
    //fungsi untuk mengakhiri transaksi
    fun endTransaction(){
        this.writableDatabase.endTransaction()
    }

    //function untuk menambahkan todoo baru
    fun TransactionCreateNewTodo(todo : Todo) : Long {
        //berikut perintah insert ke database
        val sql = "INSERT INTO ${DB.Todo.TodoTable.TABLE_TODO} " +
                "(${DB.Todo.TodoTable.COLUMN_CONTENT}" +
                ",${DB.Todo.TodoTable.COLUMN_TODO_STATUS}) VALUES (?,?)"
        //compile perintah sql menjadi objek pernyataan pra-kompilasi yg
        // akan digunakan untuk menjalaankan perintah sql
        val stmt = this.writableDatabase.compileStatement(sql)
        //kemudian kita bind data yang akan diinsert ke stmt
        //parameter pertama yaitu content dan yg kedua adalah statusnya
        stmt.bindString(1,todo.content)
        stmt.bindString(2,todo.status.toString())

        //karena kita akan melalukan proses insert, maka kita akan menggunakan executeInsert()
        //yang akan mengembalikan ID dari baris yg diinsert
        val result = stmt.executeInsert()
        //hapus data bind sebelumnya
        stmt.clearBindings()

        return result
    }

    //function untuk melakukan update ke database
    fun TransactionUpdateTodoStatus(id : Int, status: Int){
        //tampung query update ke variable
        //kita akan melakukan update data pada kolom status berdasarkan ID nya
        val sql = "UPDATE ${DB.Todo.TodoTable.TABLE_TODO} " +
                "SET ${DB.Todo.TodoTable.COLUMN_TODO_STATUS} = ? "+
                "WHERE ${DB.Todo.TodoTable.COLUMN_ID} = ?"
        val stmt = this.writableDatabase.compileStatement(sql)
        //isikan data status baru yang akan diupdate
        stmt.bindString(1,status.toString())
        //isi juga id nya
        stmt.bindString(2,id.toString())
        //jalankan proses update database
        stmt.execute()
        //berishkan data bind sebelumnya
        stmt.clearBindings()
    }

    //function untuk menghapus data
    fun TransactionDeleteTodo(id : Int){
        //simpan query untuk menghapus baris data berdasarkan ID ke var sql
        val sql = "DELETE FROM ${DB.Todo.TodoTable.TABLE_TODO} " +
                "WHERE ${DB.Todo.TodoTable.COLUMN_ID} = ?"
        val stmt = this.writableDatabase.compileStatement(sql)
        stmt.bindString(1,id.toString())
        //jalankan proses delete nya
        stmt.execute()
        //jgn lupa bersihkan data bind sebelumnya.
        stmt.clearBindings()
    }
}