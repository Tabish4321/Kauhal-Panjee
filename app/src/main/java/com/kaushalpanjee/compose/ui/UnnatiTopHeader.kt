package com.kaushalpanjee.compose.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaushalpanjee.R

/**
 * Created by Rishi Porwal
 */

@Composable
fun UnnatiTopHeader(
    onBack: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val halfScreenHeight = configuration.screenHeightDp.dp / 3

    Box(
        modifier = Modifier.
            padding(vertical = 16.dp)
            .fillMaxWidth()
            .height(halfScreenHeight) // 🔥 HALF SCREEN
            .paint(
                painter = painterResource(id = R.drawable.ic_top_round),
                contentScale = ContentScale.Crop // cover-style
            )
    ) {


        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_back_ios_new),
            contentDescription = "Back",
            modifier = Modifier
                .padding(30.dp)
                .size(24.dp)
                .align(Alignment.TopStart)
                .clickable { onBack() }
        )


        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.ic_ddgky),
                contentDescription = null,
                modifier = Modifier.size(width = 100.dp, height = 70.dp)
            )

            Spacer(Modifier.width(20.dp))

            Image(
                painter = painterResource(R.drawable.ic_rseti),
                contentDescription = null,
                modifier = Modifier.size(width = 100.dp, height = 70.dp)
            )
        }
    }
}

