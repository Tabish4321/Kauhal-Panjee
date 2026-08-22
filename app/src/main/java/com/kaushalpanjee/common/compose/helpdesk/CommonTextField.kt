package com.kaushalpanjee.common.compose.helpdesk

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CommonTextField(

    value: String,

    label: String,

    error: Boolean,

    onValueChange: (String) -> Unit

) {

    OutlinedTextField(

        value = value,

        onValueChange = onValueChange,

        modifier = Modifier.fillMaxWidth(),

        singleLine = true,

        isError = error,

        label = {

            Text(label)

        },

        leadingIcon = {

            Icon(

                imageVector = Icons.Default.Title,

                contentDescription = null

            )

        },

        supportingText = {

            if (error) {

                Text(

                    text = "This field is required"

                )

            }

        }

    )

}