package com.kaushalpanjee.common.compose

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.d2k.samiksha.SamikshaSdk
import com.d2k.samiksha.model.ConsentRequest

@Composable
fun SubmitBankConsentScreen(
    userId: String?,
    mobile: String?,
    email: String,
) {

    val context = LocalContext.current

    //  State
   // var mobileNo by remember { mutableStateOf(mobile) }
    var mobileNo by remember { mutableStateOf("7763027544") }
    var userId by remember { mutableStateOf(userId) }
    var email by remember { mutableStateOf(email) }
    var candidateId by remember { mutableStateOf(userId) }

    var isLoading by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }


    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Text("Submit Consent", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(mobileNo.toString(), { mobileNo = it }, label = { Text("Mobile No") })

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {

                    isLoading = true

                    val request = ConsentRequest(
                        mobileNo = mobileNo.toString(),
                        userId = userId.toString(),
                        fipId = "",
                        email = email,
                        pan = "",
                        candidateId = candidateId.toString(),
                        aadharNo = ""
                    )

                    SamikshaSdk.submitConsent(
                        context = context,
                        request = request,

                        onSuccess = { response ->
                            isLoading = false
                            showDialog = true
                        },

                        onError = { error ->
                            isLoading = false
                            Toast.makeText(
                                context,
                                error.message ?: "Error",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                Text("Submit Consent")
            }
        }

        // 🔹 Loader
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(0.3f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        // Dialog
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Response") },
                text = { Text(dialogMessage) },
                confirmButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}