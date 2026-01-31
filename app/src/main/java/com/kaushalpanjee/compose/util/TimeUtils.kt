package com.kaushalpanjee.compose.util

import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import java.util.Locale
/**
 * Created by Rishi Porwal
 */



object TimeUtils {

    private val dateFormatter =
        DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault())

    private val apiFormatter: DateTimeFormatter =
        DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
            .optionalStart()
            .appendFraction(ChronoField.MICRO_OF_SECOND, 1, 6, true)
            .optionalEnd()
            .toFormatter()

    fun getRelativeTime(createdAt: String?): String {
        val millis = createdAt?.toMillis() ?: return ""
        return getRelativeTime(millis)
    }

    fun getRelativeTime(createdAtMillis: Long): String {
        if (createdAtMillis <= 0L) return ""

        val now = Instant.now()
        val time = Instant.ofEpochMilli(createdAtMillis)

        if (time.isAfter(now)) return "Just now"

        val duration = Duration.between(time, now)
        val minutes = duration.toMinutes()
        val hours = duration.toHours()

        val zone = ZoneId.systemDefault()
        val today = LocalDate.now(zone)
        val date = LocalDateTime.ofInstant(time, zone).toLocalDate()

        return when {
            minutes < 1 -> "Just now"
            minutes < 60 -> "$minutes min ago"
            date == today -> "$hours hour${if (hours > 1) "s" else ""} ago"
            date == today.minusDays(1) -> "Yesterday"
            else -> date.format(dateFormatter)
        }
    }

    private fun String.toMillis(): Long {
        return try {
            LocalDateTime.parse(this, apiFormatter)
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        } catch (e: Exception) {
            0L
        }
    }
}
