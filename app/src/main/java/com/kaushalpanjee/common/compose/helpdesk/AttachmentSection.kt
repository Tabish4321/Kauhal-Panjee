package com.kaushalpanjee.common.compose.helpdesk

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AttachmentSection(

    fileName: String,

    onChooseClick: () -> Unit,

    onRemoveClick: () -> Unit = {}

) {

    Column {

        Text(

            text = "Attachment",

            style = MaterialTheme.typography.titleMedium,

            fontWeight = FontWeight.SemiBold

        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(

            onClick = onChooseClick,

            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),

            border = BorderStroke(
                1.dp,
                Color(0xFFD8D8D8)
            ),

            shape = RoundedCornerShape(14.dp)

        ) {

            Icon(

                Icons.Default.AttachFile,

                contentDescription = null,

                tint = Color(0xFF173430)

            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(

                "Choose Attachment",

                color = Color(0xFF173430)

            )

        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(

            text = "Supported : JPG, PNG, PDF",

            style = MaterialTheme.typography.bodySmall,

            color = Color.Gray

        )

        if (fileName.isNotEmpty()) {

            Spacer(modifier = Modifier.height(18.dp))

            Card(

                modifier = Modifier.fillMaxWidth(),

                colors = CardDefaults.cardColors(

                    containerColor = Color(0xFFF8F8F8)

                ),

                shape = RoundedCornerShape(12.dp)

            ) {

                Row(

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),

                    verticalAlignment = Alignment.CenterVertically

                ) {

                    Icon(

                        Icons.Default.InsertDriveFile,

                        contentDescription = null,

                        tint = Color(0xFF1976D2)

                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(

                        text = fileName,

                        modifier = Modifier.weight(1f)

                    )

                    IconButton(

                        onClick = onRemoveClick

                    ) {

                        Icon(

                            Icons.Default.DeleteOutline,

                            contentDescription = null,

                            tint = Color.Red

                        )

                    }

                }

            }

        }

    }

}