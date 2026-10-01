package com.todolist.ui.todolistscreen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.todolist.domain.TodoEntity

@Composable
fun TodoListScreenRoot(
    viewModel: ToDoListScreenViewModel,
    onItemClick: (Int) -> Unit
) {
    val itemList by viewModel.todoListItems.collectAsState()

    TodoListScreen(
        itemList = itemList,
        onAddItem = { item ->
            viewModel.insertTodoItemToRepository(item)
            Log.d("TodoListScreen", "Item Inserted: $item")
        },
        onToggleCompleted = { },
        onClearCompleted = { },
        onItemClick = onItemClick
    )
}

@Composable
fun TodoListScreen(
    itemList: List<TodoEntity>,
    onAddItem: (String) -> Unit,
    onToggleCompleted: (TodoEntity) -> Unit,
    onClearCompleted: () -> Unit,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var textInput by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "To Do List",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = textInput,
                onValueChange = { textInput = it },
                label = { Text("Add Task") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = {
                    if (textInput.isNotBlank()) {
                        onAddItem(textInput)
                        textInput = ""
                    }
                }
            ) {
                Text("Add")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("To Do Items", style = MaterialTheme.typography.titleMedium)
            if (itemList.any { it.isCompleted }) {
                TextButton(onClick = onClearCompleted) {
                    Text("Clear Completed")
                }
            }
        }

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(itemList, key = { it.id }) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onItemClick(item.id) }
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Checkbox(
                            checked = item.isCompleted,
                            onCheckedChange = { onToggleCompleted(item) }
                        )
                        Text(
                            text = item.name,
                            textDecoration = if (item.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                            fontWeight = if (item.isCompleted) FontWeight.Normal else FontWeight.Medium
                        )
                    }

                    Text(
                        text = "Qty: ${item.quantity}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TodoListScreenPreview() {
    MaterialTheme {
        TodoListScreen(
            itemList = listOf(
                TodoEntity(id = 1, name = "Finish Android project", quantity = 1, isCompleted = false),
                TodoEntity(id = 2, name = "Buy groceries", quantity = 1, isCompleted = false),
                TodoEntity(id = 3, name = "Read 20 pages", quantity = 1, isCompleted = true)
            ),
            onAddItem = {},
            onToggleCompleted = {},
            onClearCompleted = {},
            onItemClick = {}
        )
    }
}
