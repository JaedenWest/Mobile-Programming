package com.todolist.util

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

object NotificationUtils {

    const val CHANNEL_ID = "updates_channel"
    private const val CHANNEL_NAME = "App Updates"
    private const val CHANNEL_DESC = "Notifications regarding system updates and events."

    fun createNotificationChannel(context: Context) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
            description = CHANNEL_DESC
        }
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    /**
     * Builds and posts a simple notification immediately.
     */
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun showSimpleNotification(
        context: Context,
        notificationId: Int,
        title: String,
        message: String,
        targetActivity: Class<*>,
        @DrawableRes smallIconRes: Int = context.applicationInfo.icon
    ) {
        val notificationManager = NotificationManagerCompat.from(context)

        // 1. Explicit check for POST_NOTIFICATIONS on Android 13+ (API 33+) to satisfy Lint
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasPermission) {
                return
            }
        } else if (!notificationManager.areNotificationsEnabled()) {
            return
        }

        // 2. Create intent for target Activity
        val intent = Intent(context, targetActivity).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        // 3. Wrap in PendingIntent (API 31+ requires explicit mutability flags)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 4. Build notification
        val builder = NotificationCompat.Builder(context,
            com.todolist.util.NotificationUtils.CHANNEL_ID
        )
            .setSmallIcon(smallIconRes)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // 5. Issue notification
        try {
            notificationManager.notify(notificationId, builder.build())
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}