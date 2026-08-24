package com.kaushalpanjee.common.compose.helpdesk

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CreateTicketContent(
    onSubmit: (
        ticketType: String,
        scheme: String,
        title: String,
        description: String,
        attachment: Uri?
    ) -> Unit
) {

    val context = LocalContext.current

    var selectedTicket by remember { mutableStateOf("") }
    var selectedScheme by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var ticketError by remember { mutableStateOf(false) }
    var schemeError by remember { mutableStateOf(false) }
    var titleError by remember { mutableStateOf(false) }
    var descriptionError by remember { mutableStateOf(false) }

    var attachment by remember { mutableStateOf<Uri?>(null) }
    var attachmentName by remember { mutableStateOf("") }

    val ticketTypes = listOf(
        "Technical",
        "Functional",
        "Grievance",
        "Feedback"
    )

    val schemeTypes = listOf(
        "DDUGKY",
        "RSETI"
    )

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->

        attachment = uri
        attachmentName = uri?.lastPathSegment ?: ""
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Card(

            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 10.dp
                ),

            shape = RoundedCornerShape(22.dp),

            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),

            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            )

        ) {

            Column(

                modifier = Modifier
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(20.dp)

            ) {

                Text(
                    text = "Ticket Details",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF173430)
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = "Provide complete issue details",
                    color = Color.Gray
                )

                Spacer(
                    modifier = Modifier.height(22.dp)
                )

                TicketTypeDropDown(
                    selectedTicket,
                    ticketTypes,
                    ticketError
                ) {

                    selectedTicket = it
                    ticketError = false
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                SchemeTypeDropDown(
                    selectedScheme,
                    schemeTypes,
                    schemeError
                ) {

                    selectedScheme = it
                    schemeError = false
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                CommonTextField(
                    value = title,
                    label = "Issue Title",
                    error = titleError
                ) {

                    title = it
                    titleError = false
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                OutlinedTextField(

                    value = description,

                    onValueChange = {

                        description = it
                        descriptionError = false
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),

                    label = {
                        Text("Description")
                    },

                    minLines = 5,

                    isError = descriptionError

                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                AttachmentSection(

                    fileName = attachmentName,

                    onChooseClick = {

                        launcher.launch("*/*")
                    },

                    onRemoveClick = {

                        attachment = null
                        attachmentName = ""
                    }
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )
            }
        }

        CommonButton(

            text = "CREATE TICKET",

            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = 12.dp
                )

        ) {

            ticketError = selectedTicket.isBlank()
            schemeError = selectedScheme.isBlank()
            titleError = title.isBlank()
            descriptionError = description.isBlank()

            if (
                !ticketError &&
                !schemeError &&
                !titleError &&
                !descriptionError
            ) {

                onSubmit(
                    selectedTicket,
                    selectedScheme,
                    title,
                    description,
                    attachment
                )

            } else {

                Toast.makeText(
                    context,
                    "Missing mandatory fields",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}