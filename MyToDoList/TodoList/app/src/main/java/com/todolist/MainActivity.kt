package com.todolist

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.todolist.ui.todoitemscreen.TodoDetailScreenRoot
import com.todolist.ui.todolistscreen.ToDoListScreenViewModel
import com.todolist.ui.todolistscreen.TodoListScreenRoot
import com.todolist.util.NotificationUtils
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main Activity for the single-activity architecture.
 * Manages notification initialization, runtime permissions, and Jetpack Compose navigation.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "OnCreate")

        // Initialize notification channel
        NotificationUtils.createNotificationChannel(this)

        // Request POST_NOTIFICATIONS permission on Android 13+ (API 33+)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            androidx.core.app.ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                101
            )
        }

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TodoListAppNavigation()
                }
            }
        }
    }
}

/**
 * Single-Activity Jetpack Compose Navigation Host managing screen routes.
 */
@Composable
fun TodoListAppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "todo_list"
    ) {
        // Destination 1: Task List Screen
        composable("todo_list") {
            val viewModel: ToDoListScreenViewModel = hiltViewModel()
            TodoListScreenRoot(
                viewModel = viewModel,
                onItemClick = { itemId ->
                    navController.navigate("todo_detail/$itemId")
                }
            )
        }

        // Destination 2: Task Detail Screen
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
