package com.kaushalpanjee.common.compose.helpdesk

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaushalpanjee.common.model.response.RequesterTicket

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequesterTicketScreen(
    onBack: () -> Unit
) {

    val ticketList = remember {

        mutableStateListOf(

            RequesterTicket(
                ticketId = "HD1001",
                requesterName = "Tabish Jamal",
                issueTitle = "Unable to Login",
                raisedDate = "11 Aug 2026",
                status = "Closed",
                ticketType = "Technical Issue",
                schemeType = "RSETI",
                assignedTo = "Admin",
                assignedDate = "12 Aug 2026",
                priority = "High",
                assigneeComment = "Issue resolved successfully.",
                description = "User is unable to login after password reset.",
                attachmentUrl = null
            ),

            RequesterTicket(
                ticketId = "HD1002",
                requesterName = "Walvinder",
                issueTitle = "Camera Not Working",
                raisedDate = "10 Aug 2026",
                status = "Additional Info",
                ticketType = "Functional Issue",
                schemeType = "DDUGKY",
                assignedTo = "Support Team",
                assignedDate = "10 Aug 2026",
                priority = "Medium",
                assigneeComment = "Please share device model and logs.",
                description = "Camera preview is not opening on Android 14.",
                attachmentUrl = null
            ),

            RequesterTicket(
                ticketId = "HD1003",
                requesterName = "Ajit",
                issueTitle = "App Crash on Dashboard",
                raisedDate = "09 Aug 2026",
                status = "Open",
                ticketType = "Technical Issue",
                schemeType = "RSETI",
                assignedTo = "Developer",
                assignedDate = "09 Aug 2026",
                priority = "Low",
                assigneeComment = "",
                description = "Application crashes while opening dashboard.",
                attachmentUrl = null
            )

        )
    }
    var selectedTicket by remember {
        mutableStateOf<RequesterTicket?>(null)
    }

    var showBottomSheet by remember {
        mutableStateOf(false)
    }

    Scaffold(

        containerColor = Color.White,

        topBar = {

            TopAppBar(

                title = {

                    Text(
                        text = "Requester Tickets",
                        fontWeight = FontWeight.Bold
                    )

                },

                navigationIcon = {

                    IconButton(
                        onClick = onBack
                    ) {

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )

                    }

                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )

            )

        }

    ) { padding ->

        LazyColumn(

            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding),

            contentPadding = PaddingValues(16.dp),

            verticalArrangement = Arrangement.spacedBy(12.dp)

        )
        {

            items(ticketList) { item ->

                RequesterTicketCard(

                    item = item,

                    onViewClick = {

                        selectedTicket = it
                        showBottomSheet = true

                    },

                    onActionClick = {

                    }

                )
            }

        }


        if (showBottomSheet && selectedTicket != null) {

            ModalBottomSheet(

                onDismissRequest = {

                    showBottomSheet = false

                },

                containerColor = Color.White

            ) {

                TicketDetailBottomSheet(

                    ticket = selectedTicket!!,

                    onClose = {

                        showBottomSheet = false

                    }

                )

            }

        }

    }

}

@Composable
fun RequesterTicketCard(
    item: RequesterTicket,
    onViewClick: (RequesterTicket) -> Unit = {},
    onActionClick: (RequesterTicket) -> Unit = {}
) {

    val statusColor = when (item.status) {
        "Closed" -> Color(0xFF2E7D32)
        "Additional Info" -> Color(0xFFF57C00)
        else -> Color(0xFF1976D2)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = item.ticketId,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF173430)
                )

                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = statusColor.copy(alpha = .15f)
                ) {

                    Text(
                        text = item.status,
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 6.dp
                        ),
                        color = statusColor,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    )

                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Requester
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.Gray
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = item.requesterName,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Issue
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.ReportProblem,
                    contentDescription = null,
                    tint = Color.Gray
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = item.issueTitle,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Raised Date
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = Color.Gray
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = item.raisedDate,
                    color = Color.DarkGray,
                    fontSize = 14.sp
                )

            }

            Spacer(modifier = Modifier.height(20.dp))

            // Bottom Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                OutlinedButton(
                    onClick = {
                        onViewClick(item)
                    },
                    modifier = Modifier
                        .width(120.dp)
                        .height(42.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(
                        1.dp,
                        Color(0xFF173430)
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF173430)
                    )
                ) {

                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = "View",
                        fontWeight = FontWeight.Medium
                    )

                }

                if (item.status == "Closed" || item.status == "Additional Info") {

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = {
                            onActionClick(item)
                        },
                        modifier = Modifier
                            .width(135.dp)
                            .height(42.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF173430)
                        )
                    ) {

                        Icon(
                            imageVector = if (item.status == "Closed")
                                Icons.Default.ChatBubble
                            else
                                Icons.AutoMirrored.Filled.Reply,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = if (item.status == "Closed")
                                "Re-Open"
                            else
                                "Response",
                            maxLines = 1,
                            fontWeight = FontWeight.Medium
                        )

                    }
                }
            }
        }
    }
}