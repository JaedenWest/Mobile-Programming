package com.todolist.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateTimeUtils {

    private val DATE_TIME_FORMATTER: DateTimeFormatter =
        DateTimeFormatter.ofPattern("MMM dd, yyyy h:mm a")

    private val DATE_FORMATTER: DateTimeFormatter =
        DateTimeFormatter.ofPattern("MMM dd, yyyy")

    private val TIME_FORMATTER: DateTimeFormatter =
        DateTimeFormatter.ofPattern("HH:mm:ss")

    fun formatDateTime(timestamp: Long): String {
        if (timestamp <= 0L) return "No due date"
        return Instant.ofEpochMilli(timestamp)
            .atZone(ZoneId.systemDefault())
            .format(DATE_TIME_FORMATTER.withLocale(Locale.getDefault()))
    }

    fun formatDate(timestamp: Long): String {
        if (timestamp <= 0L) return "No Date Made"
        return Instant.ofEpochMilli(timestamp)
            .atZone(ZoneId.systemDefault())
            .format(DATE_FORMATTER.withLocale(Locale.getDefault()))
    }

    fun formatTime(timestamp: Long): String {
        if (timestamp <= 0L) return "No Time Made"
        return Instant.ofEpochMilli(timestamp)
            .atZone(ZoneId.systemDefault())
            .format(TIME_FORMATTER.withLocale(Locale.getDefault()))
    }
}