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
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import androidx.compose.ui.zIndex
import com.kaushalpanjee.bhashini.helper.BhashiniHelper
import com.kaushalpanjee.common.model.UidaiKycRequest
import com.kaushalpanjee.core.util.AppConstant
import kotlinx.coroutines.runBlocking


@Composable
fun NotificationItemCard(
    item: NotificationUiModel,
    onApprove: () -> Unit,
    onDisapprove: () -> Unit,
    commonViewModel: CommonViewModel
) {

    val context = LocalContext.current
    val uiState by commonViewModel.checkcandidateRequestList.collectAsState()
    val markUnhappyState by commonViewModel.markunhappy.collectAsState()

    // 🔥 LOADER (ADDED - GLOBAL OVERLAY)
    if (uiState.isLoading || markUnhappyState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
                .zIndex(1f),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(8.dp))
                Text(stringResource(R.string.please_wait), color = Color.White)
            }
        }
    }

    // ✅ EXISTING CODE (NO CHANGE)
    LaunchedEffect(markUnhappyState.status) {
        if (markUnhappyState.status == "SUCCESS") {

            Toast.makeText(
                context,
                markUnhappyState.message ?: "Success",
                Toast.LENGTH_SHORT
            ).show()

            commonViewModel.resetDialog()
        }

        if (markUnhappyState.status == "ERROR") {

            Toast.makeText(
                context,
                markUnhappyState.message ?: context.getString(R.string.something_went_wrong),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    if (uiState.isDialogVisible == true) {

        if (uiState.status == "SUCCESS") {

            var isUnhappySelected by remember { mutableStateOf(false) }
            var remark by remember { mutableStateOf("") }
            var showError by remember { mutableStateOf(false) }

            AlertDialog(
                onDismissRequest = {},

                title = null,

                text = {
                    Column {
                        val translatedmessage = runBlocking {
                            BhashiniHelper.translate(uiState.message)
                        }
                        Text(text = translatedmessage)

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Button(
                                onClick = {
                                    commonViewModel.resetDialog()
                                },
                                colors = ButtonDefaults.buttonColors(Color.Red)
                            ) {
                                Text(stringResource(R.string.happy), color = Color.White)
                            }

                            Button(
                                onClick = {
                                    isUnhappySelected = true
                                },
                                colors = ButtonDefaults.buttonColors(Color(0xFF2E7D32))
                            ) {
                                Text(stringResource(R.string.unhappy), color = Color.White)
                            }
                        }

                        if (isUnhappySelected) {

                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedTextField(
                                value = remark,
                                onValueChange = {
                                    remark = it
                                    showError = false
                                },
                                label = { Text(stringResource(R.string.enter_remark)) },
                                isError = showError,
                                modifier = Modifier.fillMaxWidth()
                            )

                            if (showError) {
                                Text(
                                    text = stringResource(R.string.please_write_remark),
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

                                        // 🔥 ONLY THIS FIX (removed resetDialog here)
                                        commonViewModel.markunhappy(
                                            item.candidateId.toString(),
                                            item.instituteId.toString(),
                                            remark
                                        )
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(stringResource(R.string.submit))
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

        }


        else {

            Toast.makeText(
                context,
                stringResource(R.string.no_data_found),
                Toast.LENGTH_SHORT
            ).show()

            commonViewModel.resetDialog()
        }
    }

    // 🔥 CARD (NO CHANGE)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .then(
                if (item.invitationStatus == "A") {
                    Modifier.clickable {
                        commonViewModel.checkcandidate(
                            item.candidateId.toString(),
                            item.instituteId.toString()
                        )
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
                    text = TimeUtils.getRelativeTime(item.createdAt.toMillis(), context),
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
                    OutlinedButton(onClick = onDisapprove) {
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
