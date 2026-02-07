package com.kaushalpanjee.compose.ui.commonComponent

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Created by Rishi Porwal
 */

@Composable
fun KPSpacer(
    size: Dp = 7.dp,
    vertical: Boolean = false
) {
    if (vertical) {
        Spacer(modifier = Modifier.height(size))
    } else {
        Spacer(modifier = Modifier.width(size))
    }
}