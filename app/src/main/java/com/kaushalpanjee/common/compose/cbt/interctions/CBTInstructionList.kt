package com.kaushalpanjee.common.compose.cbt.interctions



import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@Composable
fun CBTInstructionList(
    instructions: List<String>,
    isLargeScreen: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        instructions.forEach { item ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "▪",
                    fontSize = if (isLargeScreen) 18.sp else 15.sp,
                    modifier = Modifier.padding(top = 2.dp, end = 8.dp),
                    color = Color.Black
                )

                Text(
                    text = item,
                    modifier = Modifier.weight(1f),
                    fontSize = if (isLargeScreen) 18.sp else 14.sp,
                    lineHeight = if (isLargeScreen) 27.sp else 21.sp,
                    color = Color(0xFF222222)
                )
            }
        }
    }
}