package DB

import android.provider.BaseColumns

//buat sebuah object untuk menampung nama tabel & kolom dri DB.
object Todo {
    //lalu buat sebuah class yang akan merepresentasikan sebuah tabel dari DataBase dengan mengimplementasikan BaseColumns.
    //BaseColumns sendiri berfungsi untuk membentuk konstanta penamaan
    //sehingga ketika kita membuat kolom utk tabel, maka semua nama sudah tersimpan ke dlm object Todoo
    //selain itu, dengan menerapkan BaseColumns, kita dapat meng-inherit primary key kepada CursorAdapter.
    class TodoTable: BaseColumns {
        companion object {
            val TABLE_TODO = "todos"
            val COLUMN_ID: String = "_id"
            val COLUMN_CONTENT: String = "content"
            val COLUMN_TODO_STATUS: String = "status"
        }
    }
}