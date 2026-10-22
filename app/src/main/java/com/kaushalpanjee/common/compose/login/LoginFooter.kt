package com.kaushalpanjee.common.compose.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaushalpanjee.R

@Composable
fun LoginFooter(
    version: String,
    secureTitle: String = stringResource(R.string.login_secure)
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = CircleShape,
            color = LoginColors.Surface,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier.padding(
                    horizontal = 13.dp,
                    vertical = 7.dp
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(LoginColors.LightGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.Security,
                        contentDescription = null,
                        tint = LoginColors.DarkGreen
                    )
                }

                Spacer(Modifier.width(9.dp))

                Text(
                    text = secureTitle,
                    modifier = Modifier.weight(1f),
                    color = LoginColors.DarkGreen,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )
            }
        }

        Text(
            text = stringResource(R.string.common_version, version),
            modifier = Modifier.padding(top = 8.dp),
            color = LoginColors.DarkGreen,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun LoginRuralBackground(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.rural_footer),
        contentDescription = null,
        modifier = modifier
            .fillMaxWidth()
            .height(138.dp),
        contentScale = ContentScale.FillBounds
    )
}