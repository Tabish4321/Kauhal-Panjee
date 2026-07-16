package com.kaushalpanjee.CBT.interctions



import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
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