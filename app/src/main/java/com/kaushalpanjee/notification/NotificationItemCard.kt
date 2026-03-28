package com.kaushalpanjee.notification

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.kaushalpanjee.notification.TimeUtils.toMillis
import com.kaushalpanjee.notification.with_api.NotificationStatus
import com.kaushalpanjee.notification.with_api.model.NotificationUiModel


import com.kaushalpanjee.R
import com.kaushalpanjee.common.CommonViewModel
import com.kaushalpanjee.common.model.UidaiKycRequest
import com.kaushalpanjee.core.util.AppConstant



@Composable
fun NotificationItemCard(
    item: NotificationUiModel,
    onApprove: () -> Unit,
    onDisapprove: () -> Unit,
    commonViewModel: CommonViewModel
) {

    val context = LocalContext.current
    val uiState by commonViewModel.checkcandidateRequestList.collectAsState()

    if (uiState.isDialogVisible == true) {

        if (uiState.status == "SUCCESS") {

            // 🔥 Local state
            var isUnhappySelected by remember { mutableStateOf(false) }
            var remark by remember { mutableStateOf("") }
            var showError by remember { mutableStateOf(false) }

            AlertDialog(
                onDismissRequest = {
                    // ❌ disable outside dismiss
                },

                // ❌ TITLE REMOVED
                title = null,

                text = {
                    Column {

                        // ✅ MESSAGE
                        Text(text = uiState.message)

                        Spacer(modifier = Modifier.height(16.dp))

                        // ✅ HAPPY / UNHAPPY BUTTONS
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            // 🔴 Happy (LEFT)
                            Button(
                                onClick = {
                                    commonViewModel.resetDialog()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Red
                                )
                            ) {
                                Text("😊 Happy", color = Color.White)
                            }

                            // 🟢 Unhappy (RIGHT)
                            Button(
                                onClick = {
                                    isUnhappySelected = true
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF2E7D32)
                                )
                            ) {
                                Text("😞 Unhappy", color = Color.White)
                            }
                        }

                        // ✅ SHOW ONLY WHEN UNHAPPY CLICKED
                        if (isUnhappySelected) {

                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedTextField(
                                value = remark,
                                onValueChange = {
                                    remark = it
                                    showError = false
                                },
                                label = { Text("Enter Remark") },
                                isError = showError,
                                modifier = Modifier.fillMaxWidth()
                            )

                            if (showError) {
                                Text(
                                    text = "Please write remark",
                                    color = Color.Red,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    if (remark.isBlank()) {
                                        showError = true
                                    } else {
                                        // ✅ VALID SUBMIT
                                        Toast.makeText(
                                            context,
                                            "Submitted: $remark",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        commonViewModel.resetDialog()
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Submit")
                            }
                        }
                    }
                },

                confirmButton = {},

                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                )
            )

        } else {

            Toast.makeText(
                context,
                "No Data Found",
                Toast.LENGTH_SHORT
            ).show()

            commonViewModel.resetDialog()
        }
    }

//    if (uiState.isDialogVisible) {
//        AlertDialog(
//            onDismissRequest = {
//                commonViewModel.resetDialog()
//            },
//            title = {
//                Text(text = uiState.status)
//            },
//            text = {
//                Column {
//                    Text(text = uiState.message)
//
//                    if (uiState.showHappyUnhappy) {
//                        Spacer(modifier = Modifier.height(16.dp))
//
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            horizontalArrangement = Arrangement.SpaceEvenly
//                        ) {
//                            Button(onClick = {
//                                commonViewModel.resetDialog()
//                            }) {
//                                Text("😊 Happy")
//                            }
//
//                            Button(onClick = {
//                                commonViewModel.resetDialog()
//                            }) {
//                                Text("😞 Unhappy")
//                            }
//                        }
//                    }
//                }
//            },
//            confirmButton = {}
//        )
//    }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .then(
                if (item.invitationStatus == "A") {
                    Modifier.clickable {
                        commonViewModel.checkcandidate(item.candidateId.toString(),item.instituteId.toString())
                    }
                } else Modifier
            ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = TimeUtils.getRelativeTime(item.createdAt.toMillis(),context),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                StatusBadge(status = item.invitationStatus)
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = item.title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = item.message,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (item.invitationStatus == "P") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = onDisapprove
                    ) {
                        Text(stringResource(R.string.reject))
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    Button(onClick = onApprove) {
                        Text(stringResource(R.string.accept))
                    }
                }
            }
        }
    }
}
@Composable
fun StatusBadge(status: String) {
    val (text, color, bgColor) = when (status) {
        "APPROVED","A" ->
//            Ajit Ranjan use Resource.string
            Triple(stringResource(R.string.accepted),Color(0xFF2E7D32), Color(0xFFE8F5E9))
//            Triple("Accepted", Color(0xFF2E7D32), Color(0xFFE8F5E9))

       "REJECTED","R" ->
           //            Ajit Ranjan use Resource.string
            Triple(stringResource(R.string.rejected), Color.Red, Color(0xFFFDECEA))

        else -> return
    }
    Box(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = color,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}







//@Composable
//fun NotificationItemCard(
//    item: NotificationUiModel,
//    onApprove: () -> Unit,
//    onDisapprove: () -> Unit,
//    commonViewModel: CommonViewModel
//) {
//
//    val context = LocalContext.current
//
//    // ✅ YOUR EXISTING STATE (FIX)
//    val uiState = commonViewModel.checkcandidateRequestList.collectAsState().value
//
//    // ✅ DIALOG SHOW
//    if (uiState.isDialogVisible) {
//        AlertDialog(
//            onDismissRequest = {
//                // 🔥 IMPORTANT: dismiss manually reset karo
//                commonViewModel.resetDialog()
//            },
//            title = {
//                Text(text = uiState.status)
//            },
//            text = {
//                Column {
//                    Text(text = uiState.message)
//
//                    if (uiState.showHappyUnhappy) {
//                        Spacer(modifier = Modifier.height(16.dp))
//
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            horizontalArrangement = Arrangement.SpaceEvenly
//                        ) {
//
//                            Button(onClick = {
//                                commonViewModel.resetDialog()
//                            }) {
//                                Text("😊 Happy")
//                            }
//
//                            Button(onClick = {
//                                commonViewModel.resetDialog()
//                            }) {
//                                Text("😞 Unhappy")
//                            }
//                        }
//                    }
//                }
//            },
//            confirmButton = {}
//        )
//    }
//
//    // ✅ YOUR CARD (UNCHANGED LOGIC)
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp, vertical = 8.dp)
//            .then(
//                if (item.invitationStatus == "A") {
//                    Modifier.clickable {
//                        commonViewModel.checkcandidate("2523464946", "2")
//                    }
//                } else Modifier
//            ),
//        shape = RoundedCornerShape(16.dp),
//        elevation = CardDefaults.cardElevation(12.dp),
//        colors = CardDefaults.cardColors(containerColor = Color.White)
//    ) {
//
//        Column(modifier = Modifier.padding(16.dp)) {
//
//            Text(text = item.title)
//
//            Text(text = item.message)
//
//            if (item.invitationStatus == "P") {
//                Row {
//                    Button(onClick = onApprove) {
//                        Text("Accept")
//                    }
//
//                    Button(onClick = onDisapprove) {
//                        Text("Reject")
//                    }
//                }
//            }
//        }
//    }
//}