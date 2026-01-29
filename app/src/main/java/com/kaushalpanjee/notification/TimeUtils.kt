package com.kaushalpanjee.notification

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

object TimeUtils {

    fun getRelativeTime(createdAt: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - createdAt

        val minute = 60 * 1000
        val hour = 60 * minute
        val day = 24 * hour

        return when {
            diff < minute ->
                "Just now"

            diff < hour ->
                "${diff / minute} min ago"

            diff < day && isToday(createdAt) ->
                "${diff / hour} hour ago"

            isYesterday(createdAt) ->
                "Yesterday"

            isToday(createdAt) ->
                "Today"

            else ->
                formatDate(createdAt)
        }
    }

    fun getRelativeTime(createdAt: String): String {
        val createdMillis = createdAt.toMillis()
        if (createdMillis == 0L) return ""

        val now = System.currentTimeMillis()
        val diff = now - createdMillis

        val minute = 60 * 1000
        val hour = 60 * minute
        val day = 24 * hour

        return when {
            diff < minute ->
                "Just now"

            diff < hour ->
                "${diff / minute} min ago"

            diff < day && isToday(createdMillis) ->
                "${diff / hour} hour ago"

            isYesterday(createdMillis) ->
                "Yesterday"

            isToday(createdMillis) ->
                "Today"

            else ->
                formatDate(createdMillis)
        }
    }


    fun String.toMillis(): Long {
        return try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
            LocalDateTime.parse(this, formatter)
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        } catch (e: Exception) {
            0L
        }
    }


    private fun isToday(time: Long): Boolean {
        val today = Calendar.getInstance()
        val target = Calendar.getInstance().apply { timeInMillis = time }
        return today.get(Calendar.YEAR) == target.get(Calendar.YEAR) &&
                today.get(Calendar.DAY_OF_YEAR) == target.get(Calendar.DAY_OF_YEAR)
    }

    private fun isYesterday(time: Long): Boolean {
        val yesterday = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -1)
        }
        val target = Calendar.getInstance().apply { timeInMillis = time }
        return yesterday.get(Calendar.YEAR) == target.get(Calendar.YEAR) &&
                yesterday.get(Calendar.DAY_OF_YEAR) == target.get(Calendar.DAY_OF_YEAR)
    }

    private fun formatDate(time: Long): String {
        val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return formatter.format(Date(time))
    }
}

//Text(
//text = TimeUtils.getRelativeTime(item.createdAt),
//style = MaterialTheme.typography.labelSmall,
//color = MaterialTheme.colorScheme.primary
//)

