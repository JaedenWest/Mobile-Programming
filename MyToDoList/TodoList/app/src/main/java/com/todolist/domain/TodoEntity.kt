package com.todolist.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_items")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val quantity: Int = 1,
    val isCompleted: Boolean = false,
    val notes: String = ""
)
