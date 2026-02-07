package com.kaushalpanjee.compose.ui.commonComponent

import  androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.kaushalpanjee.R
import com.kaushalpanjee.compose.ui.theme.AvenirNextBold

/**
 * Created by Rishi Porwal
 */

@Composable
fun KPText(
    text: String = "",
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 12.sp,
    fontFamily: FontFamily = AvenirNextBold,
    fontWeight: FontWeight = FontWeight.Bold,
    color: Color = Color.Unspecified,
    lineHeight: TextUnit = 16.sp,

) {
    Text(
        text = text,
        modifier = modifier,
        fontSize = fontSize,
        fontFamily = fontFamily,
        fontWeight = fontWeight,
        color = if (color == Color.Unspecified)
            colorResource(id = R.color.color_dark_light_green)
        else color,
        lineHeight = lineHeight
    )
}




