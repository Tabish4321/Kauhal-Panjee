package com.kaushalpanjee.common.compose.cbt

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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

    val targetProgress = (timeLeft.toFloat() / totalTime.toFloat())
        .coerceIn(0f, 1f)

    val progress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(800),
        label = ""
    )

    val minutes = timeLeft / 60
    val seconds = timeLeft % 60

    Box(
        modifier = Modifier.size(48.dp),
        contentAlignment = Alignment.Center
    ) {

        CircularProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxSize(),
            strokeWidth = 4.dp,
            color = Color.White,
            trackColor = Color.White.copy(alpha = 0.2f)
        )

        Text(
            text = String.format("%02d:%02d", minutes, seconds),
            color = Color.White,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


