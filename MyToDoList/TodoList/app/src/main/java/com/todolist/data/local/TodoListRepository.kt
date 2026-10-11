package com.todolist.data.local

import android.util.Log
import com.todolist.domain.TodoEntity
import kotlinx.coroutines.flow.Flow

class TodoListRepository(private val todoListDao: TodoListDao) {

    /** emits the full list of todo items from the database. */
    val todoList: Flow<List<TodoEntity>> = todoListDao.getAllTodoListItems()

    /** Fetches a single task item by its unique ID. */
    suspend fun getItemById(id: Int): TodoEntity? = todoListDao.getItemById(id)

    /** Inserts a new task item into the database. */
    suspend fun insertTodoItem(item: TodoEntity) {
        todoListDao.insertTodoItem(item)
        Log.d("Repository", "Item Inserted: ${item.title}")
    }

    /** Updates an existing task item in the database. */
    suspend fun updateTodoItem(item: TodoEntity) {
        todoListDao.updateTodoItem(item)
    }

    /** Deletes a task item by its ID. */
    suspend fun deleteItem(id: Int) {
        todoListDao.deleteTodoItem(id)
    }

    /** Deletes all completed tasks from the database. */
    suspend fun deleteCompletedItems() {
        todoListDao.deleteCompletedItems()
    }
}
