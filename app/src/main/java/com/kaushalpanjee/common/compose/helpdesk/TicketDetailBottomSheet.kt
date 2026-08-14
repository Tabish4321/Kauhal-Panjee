package com.kaushalpanjee.common.compose.helpdesk


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaushalpanjee.common.model.response.RequesterTicket

@Composable
fun TicketDetailBottomSheet(
    ticket: RequesterTicket,
    onClose: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.90f)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Ticket Details",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, null)
            }
        }

        HorizontalDivider()

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            item { DetailItem("Ticket ID", ticket.ticketId) }
            item { DetailItem("Requester Name", ticket.requesterName) }
            item { DetailItem("Raised Date", ticket.raisedDate) }
            item { DetailItem("Scheme", ticket.schemeType) }
            item { DetailItem("Issue Title", ticket.issueTitle) }
            item { DetailItem("Assigned Date", ticket.assignedDate) }
            item { DetailItem("Status", ticket.status) }
            item { DetailItem("Ticket Type", ticket.ticketType) }
            item { DetailItem("Description", ticket.description) }
            item { DetailItem("Assigned To", ticket.assignedTo) }
            item { DetailItem("Priority", ticket.priority) }
            item { DetailItem("Comment", ticket.assigneeComment) }










            item {

                Text(
                    text = "Assignee Comment",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF5F5F5)
                    )
                ) {

                    Text(
                        text = "Issue has been resolved successfully. Kindly verify from your side.",
                        modifier = Modifier.padding(12.dp)
                    )

                }

            }

        }

        Button(
            onClick = onClose,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF173430)
            )
        ) {
            Text("Close")
        }

    }
}

@Composable
fun DetailItem(
    title: String,
    value: String
) {

    Column {

        Text(
            text = title,
            fontSize = 12.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        HorizontalDivider()

    }
}