package com.kaushalpanjee.common.compose.helpdesk


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketTypeDropDown(

    selectedTicket: String,

    ticketTypeList: List<String>,

    ticketTypeError: Boolean,

    onSelected: (String) -> Unit

) {

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(

        expanded = expanded,

        onExpandedChange = {

            expanded = !expanded

        }

    ) {

        OutlinedTextField(

            value = selectedTicket,

            onValueChange = {},

            readOnly = true,

            isError = ticketTypeError,

            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),

            label = {

                Text("Ticket Type *")

            },

            placeholder = {

                Text("Select Ticket Type")

            },

            leadingIcon = {

                Icon(

                    imageVector = Icons.Default.ConfirmationNumber,

                    contentDescription = null

                )

            },

            trailingIcon = {

                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )

            },

            supportingText = {

                if (ticketTypeError) {

                    Text("Please select ticket type")

                }

            }

        )

        ExposedDropdownMenu(

            expanded = expanded,

            onDismissRequest = {

                expanded = false

            },

            modifier = Modifier.background(Color.White)

        ) {

            ticketTypeList.forEach { type ->

                DropdownMenuItem(

                    text = {

                        Text(
                            text = type,
                            color = Color.Black
                        )

                    },

                    colors = MenuDefaults.itemColors(
                        textColor = Color.Black
                    ),

                    onClick = {

                        onSelected(type)

                        expanded = false

                    }

                )

            }

        }

    }

}