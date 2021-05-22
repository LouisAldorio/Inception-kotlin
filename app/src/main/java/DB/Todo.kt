package DB

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