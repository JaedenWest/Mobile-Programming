package com.todolist

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import com.todolist.ui.todolistscreen.TodoListScreenRoot
import com.todolist.ui.todoitemscreen.TodoDetailScreenRoot
import com.todolist.ui.todolistscreen.ToDoListScreenViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "OnCreate")

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TodoListAppNavigation()
                }
            }
        }
    }
}

@Composable
fun TodoListAppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "todo_list"
    ) {
        composable("todo_list") {
            val viewModel: ToDoListScreenViewModel = hiltViewModel()
            TodoListScreenRoot(
                viewModel = viewModel,
                onItemClick = { itemId ->
                    navController.navigate("todo_detail/$itemId")
                }
            )
        }

        composable(
            route = "todo_detail/{itemId}",
            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
        ) {
            TodoDetailScreenRoot(
                viewModel = hiltViewModel(),
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
