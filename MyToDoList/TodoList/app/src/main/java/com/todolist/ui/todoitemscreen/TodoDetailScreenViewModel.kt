package com.todolist.ui.todoitemscreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.todolist.data.local.TodoListRepository
import com.todolist.domain.TodoEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoDetailScreenViewModel @Inject constructor(
    private val repository: TodoListRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val itemId: Int = checkNotNull(savedStateHandle["itemId"])

    private val _uiState = MutableStateFlow<TodoEntity?>(null)
    val uiState: StateFlow<TodoEntity?> = _uiState.asStateFlow()

    init {
        loadItem()
    }

    private fun loadItem() {
        viewModelScope.launch {
            _uiState.value = repository.getItemById(itemId)
        }
    }

    fun updateTodoItemInRepository(todoItem: TodoEntity, onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.updateTodoItem(todoItem)
            onComplete()
        }
    }

    fun deleteItem(id: Int, onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.deleteItem(id)
            onComplete()
        }
    }
}
