package com.kaushalpanjee.compose.ui.commonComponent

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.kaushalpanjee.R
import com.kaushalpanjee.common.showError
import com.kaushalpanjee.core.util.AppUtil

@Composable
fun PasswordTextField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = { Text(label) },
        singleLine = true,
        visualTransformation = if (isVisible)
            VisualTransformation.None
        else
            PasswordVisualTransformation(),

        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = colorResource(R.color.color_dark_green),
            unfocusedTextColor = colorResource(R.color.black),
            focusedLabelColor = colorResource(R.color.color_dark_green),
            unfocusedLabelColor = colorResource(R.color.black),
            focusedBorderColor = colorResource(R.color.color_dark_green),
            unfocusedBorderColor = colorResource(R.color.black),
            cursorColor = colorResource(R.color.black)
        ),
        shape = RoundedCornerShape(12.dp),
        trailingIcon = {
                IconButton(onClick = { isVisible = !isVisible }) {
                    Icon(
                        painter = painterResource(
                            id = if (isVisible)
                                R.drawable.ic_open_eye
                            else
                                R.drawable.close_eye
                        ),
                        contentDescription = null,
                        tint = colorResource(if(isVisible) R.color.color_green else R.color.color_dark_green ),

                    )

                }

        }

    )

}
