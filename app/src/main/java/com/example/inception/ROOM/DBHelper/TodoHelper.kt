package com.example.inception.ROOM.DBHelper

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.inception.ROOM.dao.TodoDao
import com.example.inception.ROOM.entities.TodoRoom

@Database(entities = arrayOf(TodoRoom::class),version = 1)
abstract class TodoHelper : RoomDatabase(){
    abstract fun todoDao() : TodoDao
}