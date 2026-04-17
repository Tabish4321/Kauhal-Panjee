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
fun SubmitBankConsentScreen() {

    val context = LocalContext.current

    // 🔹 State
    var mobileNo by remember { mutableStateOf("9769652415") }
    var userId by remember { mutableStateOf("9769652415UID") }
    var fipId by remember { mutableStateOf("BOB") }
    var email by remember { mutableStateOf("abc@gamil.com") }
    var pan by remember { mutableStateOf("ABCDE1111F") }
    var candidateId by remember { mutableStateOf("9769652415ID") }
    var aadharNo by remember { mutableStateOf("111111111111") }

    var isLoading by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    // 🔹 SDK INIT (only once)
/*    LaunchedEffect(Unit) {
        SamikshaSdk.init(
            context,
            baseUrl = "https://samikshaapi.d2kindia.com/",
            apiKey = "624f2281-b0f1-44e3-9d3e-24826a53e7a6",
            calledFrom = "KAUSHAL PANJEE",
            apiVersion = "2",
            onFailure = {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            },
            onSuccess = {
                Toast.makeText(context, "SDK Initialized", Toast.LENGTH_SHORT).show()
            }
        )
    }*/

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Text("Submit Consent", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(mobileNo, { mobileNo = it }, label = { Text("Mobile No") })
            OutlinedTextField(fipId, { fipId = it }, label = { Text("FIP ID") })
            OutlinedTextField(email, { email = it }, label = { Text("Email") })
            OutlinedTextField(pan, { pan = it }, label = { Text("PAN") })
            OutlinedTextField(userId, { userId = it }, label = { Text("User ID") })
            OutlinedTextField(candidateId, { candidateId = it }, label = { Text("Candidate ID") })
            OutlinedTextField(aadharNo, { aadharNo = it }, label = { Text("Aadhar No") })

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {

                    isLoading = true

                    val request = ConsentRequest(
                        mobileNo = mobileNo,
                        userId = userId,
                        fipId = fipId,
                        email = email,
                        pan = pan,
                        candidateId = candidateId,
                        aadharNo = aadharNo
                    )

                    SamikshaSdk.submitConsent(
                        context = context,
                        request = request,

                        onSuccess = { response ->
                            isLoading = false
                            dialogMessage = "Consent submitted successfully\n$response"
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

        // 🔹 Dialog
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