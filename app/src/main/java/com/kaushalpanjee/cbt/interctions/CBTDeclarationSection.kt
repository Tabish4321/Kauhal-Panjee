package com.kaushalpanjee.cbt.interctions



import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@Composable
fun CBTDeclarationSection(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    text: String,
    isLargeScreen: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.padding(top = 2.dp)
        )

        Text(
            text = text,
            modifier = Modifier
                .padding(top = 12.dp)
                .weight(1f),
            fontSize = if (isLargeScreen) 18.sp else 14.sp,
            lineHeight = if (isLargeScreen) 27.sp else 21.sp,
            color = Color(0xFF222222)
        )
    }
}