package com.example.inception.ROOM.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Todos_with_ROOM")
data class TodoRoom (
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "content") var content: String = "",
    @ColumnInfo(name = "status") var status: Int = 0
)