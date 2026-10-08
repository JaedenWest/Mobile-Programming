package com.todolist.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateTimeUtils {

    fun formatDateTime(timestamp: Long): String {
        if (timestamp <= 0L) return "No due date"
        val sdf = SimpleDateFormat("MMM dd, yyyy h:mm a", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    fun formatDate(timestamp: Long): String {
        if (timestamp <= 0L) return "No Date Made"
        val sdf = SimpleDateFormat("mmm, dd, yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    fun formatTime(timestamp: Long): String {
        if(timestamp <= 0L) return "No Time Made"
        val sdf = SimpleDateFormat("h:m:s", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}