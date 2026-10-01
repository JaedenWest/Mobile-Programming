package com.todolist.ui.todolistscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.todolist.data.local.TodoListRepository
import com.todolist.domain.TodoEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToDoListScreenViewModel @Inject constructor(
    private val repository: TodoListRepository
) : ViewModel() {

    val todoListItems: StateFlow<List<TodoEntity>> = repository.todoList
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun insertTodoItemToRepository(itemName: String) {
        viewModelScope.launch {
            val todoItem = TodoEntity(
                id = 0,
                name = itemName,
                quantity = 1,
                isCompleted = false,
                notes = ""
            )
            Log.d("ListScreenVM", "Item Inserted ${todoItem.name}")
            repository.insertTodoItem(todoItem)
        }
    }
}
