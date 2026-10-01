package com.todolist.ui.todoitemscreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.todolist.domain.TodoEntity

@Composable
fun TodoDetailScreenRoot(
    viewModel: TodoDetailScreenViewModel,
    onNavigateBack: () -> Unit
) {
    val item by viewModel.uiState.collectAsState()

    val currentItem = item
    if (currentItem != null) {
        TodoDetailScreen(
            item = currentItem,
            onSaveItem = { updatedItem ->
                viewModel.updateTodoItemInRepository(updatedItem) {
                    onNavigateBack()
                }
            },
            onDelete = {
                viewModel.deleteItem(currentItem.id) {
                    onNavigateBack()
                }
            },
            onNavigateBack = onNavigateBack
        )
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoDetailScreen(
    item: TodoEntity,
    onSaveItem: (TodoEntity) -> Unit,
    onDelete: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember(item) { mutableStateOf(item.name) }
    var quantityText by remember(item) { mutableStateOf(item.quantity.toString()) }
    var notes by remember(item) { mutableStateOf(item.notes) }
    var isCompleted by remember(item) { mutableStateOf(item.isCompleted) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Task Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Edit Task",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Task Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = quantityText,
                onValueChange = { quantityText = it.filter { char -> char.isDigit() } },
                label = { Text("Quantity") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes") },
                minLines = 3,
                maxLines = 5,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Checkbox(
                    checked = isCompleted,
                    onCheckedChange = { isCompleted = it }
                )
                Text(text = "Mark as Completed")
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDelete,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Delete")
                }

                Button(
                    onClick = {
                        val parsedQuantity = quantityText.toIntOrNull() ?: 1
                        onSaveItem(
                            item.copy(
                                name = name,
                                quantity = parsedQuantity,
                                notes = notes,
                                isCompleted = isCompleted
                            )
                        )
                    },
                    enabled = name.isNotBlank(),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Save")
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TodoDetailScreenPreview() {
    MaterialTheme {
        TodoDetailScreen(
            item = TodoEntity(
                id = 1,
                name = "Buy groceries",
                quantity = 1,
                isCompleted = false,
                notes = "Get milk, eggs, bread"
            ),
            onSaveItem = {},
            onDelete = {},
            onNavigateBack = {}
        )
    }
}
