package com.example.myapplication.CBT

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@Composable
fun CircularTimer(
    timeLeft: Int,
    totalTime: Int
) {
    val minutes = timeLeft / 60
    val seconds = timeLeft % 60
    val progress = timeLeft.toFloat() / totalTime.toFloat()

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(45.dp)
    ) {
        CircularProgressIndicator(
            progress = { progress },
            strokeWidth = 4.dp,
            color = Color.White,
            trackColor = Color.White.copy(alpha = 0.2f),
            modifier = Modifier.fillMaxSize()
        )

        Text(
            text = String.format("%02d:%02d", minutes, seconds),
            color = Color.White,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

