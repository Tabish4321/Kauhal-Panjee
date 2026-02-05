package com.kaushalpanjee.compose.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaushalpanjee.R
import com.kaushalpanjee.compose.presentation.contract.ChangePasswordContract
import com.kaushalpanjee.compose.ui.commonComponent.PasswordTextField
import com.kaushalpanjee.compose.ui.commonComponent.PrimaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    uiState: ChangePasswordContract.State,
    onOldPassChange: (String) -> Unit,
    onNewPassChange: (String) -> Unit,
    onConfirmPassChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onBack:()->Unit

) {
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                colorResource(id = R.color.color_background_light),
                                colorResource(id = R.color.color_background),
                            )
                        ),
                        shape = RoundedCornerShape(bottomStart = 60.dp, bottomEnd = 60.dp)
                    )
            ) {

                Spacer(modifier = Modifier.height(20.dp))
                IconButton(
                    onClick = { onBack() },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 16.dp, top = 25.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_arrow_back_ios_new),
                        contentDescription = null,
                        modifier = Modifier.height(60.dp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_ddgky),
                        contentDescription = null,
                        modifier = Modifier.height(60.dp)
                    )

                    Spacer(modifier = Modifier.width(20.dp))

                    Image(
                        painter = painterResource(id = R.drawable.ic_rseti),
                        contentDescription = null,
                        modifier = Modifier.height(60.dp)
                    )
                }
            }
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 20.dp)
                .fillMaxSize()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Change Password",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.black)

            )

            Spacer(modifier = Modifier.height(24.dp))

            PasswordTextField(
                value = uiState.oldPassword,
                label = "Old Password",
                onValueChange = onOldPassChange
            )

            Spacer(modifier = Modifier.height(16.dp))

            PasswordTextField(
                value = uiState.newPassword,
                label = "New Password",
                onValueChange = onNewPassChange
            )

            Spacer(modifier = Modifier.height(16.dp))

            PasswordTextField(
                value = uiState.confirmPassword,
                label = "Confirm Password",
                onValueChange = onConfirmPassChange
            )

            Spacer(modifier = Modifier.height(28.dp))

            Spacer(modifier = Modifier.weight(1f))

            PrimaryButton(
                text = "Submit",
                loading = uiState.isLoading,
                onClick = onSubmit
            )

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}


