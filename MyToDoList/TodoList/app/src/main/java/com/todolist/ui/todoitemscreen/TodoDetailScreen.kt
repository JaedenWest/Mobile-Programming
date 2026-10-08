package com.todolist.ui.todoitemscreen

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.todolist.domain.TodoEntity
import com.todolist.util.DateTimeUtils
import java.util.Calendar

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
    val context = LocalContext.current

    var title by remember(item) { mutableStateOf(item.title) }
    var notes by remember(item) { mutableStateOf(item.content) }
    var dueDate by remember(item) { mutableLongStateOf(item.dueDate) }
    var isCompleted by remember(item) { mutableStateOf(item.isCompleted) }

    fun showDateTimePicker() {
        val calendar = Calendar.getInstance()
        if (dueDate > 0L) {
            calendar.timeInMillis = dueDate
        }

        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                TimePickerDialog(
                    context,
                    { _, hourOfDay, minute ->
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        calendar.set(Calendar.MINUTE, minute)
                        calendar.set(Calendar.SECOND, 0)
                        calendar.set(Calendar.MILLISECOND, 0)
                        dueDate = calendar.timeInMillis
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    false
                ).show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

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
                value = title,
                onValueChange = { title = it },
                label = { Text("Task Title") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDateTimePicker() }
            ) {
                OutlinedTextField(
                    value = DateTimeUtils.formatDateTime(dueDate),
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    label = { Text("Due Date & Time") },
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    trailingIcon = {
                        Row {
                            if (dueDate > 0L) {
                                IconButton(onClick = { dueDate = 0L }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear Due Date"
                                    )
                                }
                            }
                            IconButton(onClick = { showDateTimePicker() }) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Select Due Date"
                                )
                            }
                        }
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

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
                        onSaveItem(
                            item.copy(
                                title = title,
                                content = notes,
                                dueDate = dueDate,
                                isCompleted = isCompleted
                            )
                        )
                    },
                    enabled = title.isNotBlank(),
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
                title = "Buy groceries",
                content = "Get milk, eggs, bread",
                isCompleted = false,
                dueDate = System.currentTimeMillis()
            ),
            onSaveItem = {},
            onDelete = {},
            onNavigateBack = {}
        )
    }
}
