package com.kaushalpanjee.common.compose.helpdesk

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HelpDeskHeader(

    onClose: () -> Unit

) {

    Box(

        modifier = Modifier
            .fillMaxWidth()
            .background(

                Brush.verticalGradient(

                    colors = listOf(

                        Color(0xFF173430),   // Dark (same as button)

                        Color(0xFF215245),   // Medium Dark

                        Color(0xFF3E8B6C)    // Light Green

                    )

                )

            )

    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(
                    start = 22.dp,
                    end = 22.dp,
                    bottom = 24.dp
                )
        ) {

            Box(

                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(60.dp)
                    .height(5.dp)
                    .clip(RoundedCornerShape(100.dp))
                    .background(Color.White.copy(alpha = .8f))

            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(

                verticalAlignment = Alignment.CenterVertically

            ) {

                Surface(

                    shape = CircleShape,

                    color = Color.White.copy(alpha = .15f)

                ) {

                    Icon(

                        imageVector = Icons.Default.HeadsetMic,

                        contentDescription = null,

                        tint = Color.White,

                        modifier = Modifier
                            .padding(12.dp)
                            .size(26.dp)

                    )

                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(

                        text = "Raise Help Desk Ticket",

                        color = Color.White,

                        fontSize = 22.sp,

                        fontWeight = FontWeight.Bold

                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(

                        text = "Create and track your support request",

                        color = Color.White.copy(alpha = .92f),

                        fontSize = 14.sp

                    )

                }

            }

        }

        Surface(

            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(
                    top = 8.dp,
                    end = 16.dp
                )
                .size(32.dp),   // Smaller circle

            shape = CircleShape,

            color = Color.White.copy(alpha = 0.18f)

        ) {

            IconButton(

                onClick = onClose,

                modifier = Modifier.size(16.dp)

            ) {

                Icon(

                    imageVector = Icons.Default.Close,

                    contentDescription = "Close",

                    tint = Color.White,

                    modifier = Modifier.size(18.dp)   // Smaller icon

                )

            }

        }

    }

}