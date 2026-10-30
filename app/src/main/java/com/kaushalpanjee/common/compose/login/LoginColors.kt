package com.kaushalpanjee.common.compose.login

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object LoginColors {
    val Primary = Color(0xFF087A56)
    val DarkGreen = Color(0xFF064F40)
    val Background = Color(0xFFF7FAF3)
    val Surface = Color(0xFFFFFFFF)
    val LightGreen = Color(0xFFE8F5D9)
    val Border = Color(0xFFD3E0D7)
    val TextPrimary = Color(0xFF173E32)
    val TextSecondary = Color(0xFF6F7972)
    val Placeholder = Color(0xFFAAB2AC)
    val Error = Color(0xFFB3261E)
    val Divider = Color(0xFFD8E2DB)

}

object LoginDimensions {
    val ScreenPadding = 22.dp
    val CardRadius = 26.dp
    val FieldRadius = 17.dp
    val ButtonRadius = 16.dp
    val FieldHeight = 54.dp
    val ButtonHeight = 50.dp
}

object LoginShapes {
    val Card = RoundedCornerShape(LoginDimensions.CardRadius)
    val Field = RoundedCornerShape(LoginDimensions.FieldRadius)
    val Button = RoundedCornerShape(LoginDimensions.ButtonRadius)
}
