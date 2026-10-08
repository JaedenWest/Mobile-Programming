package com.todolist.data.local

import android.util.Log
import com.todolist.domain.TodoEntity
import kotlinx.coroutines.flow.Flow

class TodoListRepository(private val todoListDao: TodoListDao) {

    val todoList: Flow<List<TodoEntity>> = todoListDao.getAllTodoListItems()

    suspend fun getItemById(id: Int): TodoEntity? = todoListDao.getItemById(id)

    suspend fun insertTodoItem(item: TodoEntity){
        todoListDao.insertTodoItem(item)
        Log.d("Repository","Item Inserted: ${item.title}")
    }

    suspend fun updateTodoItem(item: TodoEntity){
        todoListDao.updateTodoItem(item)
    }

    suspend fun deleteItem(id: Int) {
        todoListDao.deleteTodoItem(id)
    }

    suspend fun deleteCompletedItems() {
        todoListDao.deleteCompletedItems()
    }
}
