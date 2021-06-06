package com.example.inception.ROOM.dao

import androidx.room.*
import com.example.inception.ROOM.entities.TodoRoom

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg todo: TodoRoom)
//
//    @Query("DELETE FROM Todos_with_ROOM ")
//    fun DeleteAll()

    @Query("DELETE FROM Todos_with_ROOM WHERE id = :id")
    fun DeleteById(id : Int)

    @Query("SELECT * FROM Todos_with_ROOM")
    fun getAllData() : List<TodoRoom>

    @Query("UPDATE Todos_with_ROOM SET status = :status WHERE id = :id")
    fun UpdateTodoStatus(id: Int,status: Int)
}