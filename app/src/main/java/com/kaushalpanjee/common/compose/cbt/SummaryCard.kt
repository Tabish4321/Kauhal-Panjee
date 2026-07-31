package com.example.esop.quetions_esop

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SummaryCard(
    title: String,
    count: String,
    color: Color
) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.15f)
        )
    ) {

        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Text(
                text = title,
                fontSize = 12.sp
            )

            Text(
                text = count,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}