package com.kaushalpanjee.common.compose.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Login
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaushalpanjee.R

@Composable
fun LoginInputField(
    title: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(LoginDimensions.FieldHeight)
            .clip(LoginShapes.Field)
            .background(LoginColors.Surface)
            .border(1.dp, LoginColors.Border, LoginShapes.Field)
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(11.dp))
                .background(LoginColors.LightGreen),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isPassword) Icons.Outlined.Lock else Icons.Outlined.Person,
                contentDescription = null,
                tint = LoginColors.DarkGreen,
                modifier = Modifier.size(21.dp)
            )
        }

        Spacer(Modifier.width(10.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(title, color = LoginColors.TextSecondary, fontSize = 11.sp)
            Spacer(Modifier.height(2.dp))
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                textStyle = TextStyle(
                    color = LoginColors.TextPrimary,
                    fontSize = 14.sp
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = if (isPassword && passwordVisible) KeyboardType.Text else keyboardType
                ),
                visualTransformation = if (isPassword && !passwordVisible) {
                    PasswordVisualTransformation()
                } else {
                    VisualTransformation.None
                },
                decorationBox = { inner ->
                    Box {
                        if (value.isEmpty()) {
                            Text(
                                placeholder,
                                color = LoginColors.Placeholder,
                                fontSize = 14.sp
                            )
                        }
                        inner()
                    }
                }
            )
        }

        if (isPassword) {
            IconButton(
                onClick = { passwordVisible = !passwordVisible },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = if (passwordVisible) {
                        Icons.Outlined.VisibilityOff
                    } else {
                        Icons.Outlined.Visibility
                    },
                    contentDescription = stringResource(
                        if (passwordVisible) {
                            R.string.login_hide_password
                        } else {
                            R.string.login_show_password
                        }
                    ),
                    tint = LoginColors.DarkGreen,
                    modifier = Modifier.size(21.dp)
                )
            }
        }
    }
}

@Composable
fun LoginPrimaryButton(
    title: String,
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = !isLoading,
        modifier = modifier
            .fillMaxWidth()
            .height(LoginDimensions.ButtonHeight),
        shape = LoginShapes.Button,
        contentPadding = PaddingValues(horizontal = 18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = LoginColors.Primary,
            disabledContainerColor = LoginColors.Primary
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(21.dp),
                color = Color.White,
                strokeWidth = 2.dp
            )
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Outlined.Login, contentDescription = null)
                Spacer(Modifier.width(12.dp))
                Text(title, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.weight(1f))
                Icon(Icons.Outlined.ChevronRight, contentDescription = null)
            }
        }
    }
}

@Composable
fun LoginOutlinedButton(
    title: String,
    register: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(50.dp),
        shape = RoundedCornerShape(13.dp),
        border = BorderStroke(1.dp, LoginColors.Border),
        contentPadding = PaddingValues(horizontal = 5.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = LoginColors.DarkGreen,
            containerColor = LoginColors.Surface
        )
    ) {
        Icon(
            imageVector = if (register) Icons.Outlined.PersonAdd else Icons.Outlined.Info,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(6.dp))
        Text(title, fontSize = 12.sp, maxLines = 1)
    }
}

@Composable
fun LoginLockBadge(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.size(58.dp),
        shape = CircleShape,
        color = LoginColors.Surface,
        shadowElevation = 5.dp
    ) {
        Box(Modifier.padding(7.dp), contentAlignment = Alignment.Center) {
            Box(
                Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(LoginColors.LightGreen),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.Security,
                    contentDescription = "Secure login",
                    tint = LoginColors.DarkGreen,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun LoginErrorMessage(message: String, modifier: Modifier = Modifier) {
    if (message.isNotBlank()) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(LoginColors.Surface)
                .border(1.dp, LoginColors.Error.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Outlined.Warning, contentDescription = null, tint = LoginColors.Error)
            Spacer(Modifier.width(8.dp))
            Text(message, color = LoginColors.Error, fontSize = 12.sp)
        }
    }
}
