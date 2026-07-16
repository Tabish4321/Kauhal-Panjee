package com.kaushalpanjee.cbt

// ✅ IMPORTS
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


// ✅ ACTION BUTTON (RowScope)
@Composable
fun RowScope.ActionButton(
    text: String,
    color: Color,
    dimens: AppDimens,
    onClick: () -> Unit
) {
    Text(
        text = text,
        color = Color.White,
        fontSize = dimens.textSmall,
        textAlign = TextAlign.Center,
        modifier = androidx.compose.ui.Modifier
            .weight(1f)   // ✅ NOW WORKS
            .background(
                color = color,
                shape = RoundedCornerShape(dimens.radiusSmall)
            )
            .clickable { onClick() }
            .padding(vertical = dimens.paddingSmall)
    )
}

@Composable
fun OutlineBtn(
    text: String,
    dimens: AppDimens,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(dimens.radiusMedium))
            .border(2.dp, MaterialTheme.colorScheme.primary)
            .clickable { onClick() }
            .padding(
                horizontal = dimens.paddingMedium,
                vertical = dimens.paddingSmall
            )
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.primary
        )
    }
}