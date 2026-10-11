package com.todolist

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.todolist.data.local.BroadcastReceiverEntryPoint
import com.todolist.util.NotificationUtils
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Custom BroadcastReceiver triggered by AlarmManager when a task due date arrives.
 */
class MyToDoListReceiver : BroadcastReceiver() {

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getIntExtra("id", 0)
        val name = intent.getStringExtra("item")

        if (id != 0) {
            Log.d("Broadcast Receiver", "Item $name notification fired")

            // Access Hilt SingletonComponent dependency graph from BroadcastReceiver
            val entryPoint = EntryPointAccessors.fromApplication(
                context.applicationContext,
                BroadcastReceiverEntryPoint::class.java
            )

            val repository = entryPoint.getMyRepository()

            // Fetch item from repository asynchronously in a SupervisorJob coroutine
            CoroutineScope(SupervisorJob()).launch {
                val item = repository.getItemById(id)
                Log.d("Broadcast Receiver", "Item ${item?.title} notification fired")

                // Ensure channel is initialized and post local notification banner
                NotificationUtils.createNotificationChannel(context)
                NotificationUtils.showSimpleNotification(
                    context = context,
                    notificationId = id,
                    title = item?.title ?: "Task Due!",
                    message = item?.content?.ifBlank { "You have a task due now" }
                        ?: "Your task is due!",
                    targetActivity = MainActivity::class.java
                )
            }
        } else {
            Log.d("BroadcastReceiver", "Invalid Task ID")
        }
    }
}
