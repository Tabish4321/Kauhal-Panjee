package com.kaushalpanjee.compose.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Created by Rishi Porwal
 */

@Composable
fun NotificationStatusBadge(status: String) {
    val (text, color, bgColor) = when (status) {
        "APPROVED", "A" -> Triple("Accepted", Color(0xFF2E7D32), Color(0xFFE8F5E9))
        "REJECTED", "R" -> Triple("Rejected", Color.Red, Color(0xFFFDECEA))
        else -> return
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(8.dp),
        tonalElevation = 1.dp
    ) {
        Text(
            text = text,
            color = color,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}