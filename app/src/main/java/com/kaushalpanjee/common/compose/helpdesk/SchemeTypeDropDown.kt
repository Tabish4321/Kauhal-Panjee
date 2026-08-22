package com.kaushalpanjee.common.compose.helpdesk

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
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
fun SchemeTypeDropDown(

    selectedScheme: String,

    schemeTypeList: List<String>,

    schemeError: Boolean,

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

            value = selectedScheme,

            onValueChange = {},

            readOnly = true,

            isError = schemeError,

            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),

            label = {

                Text("Scheme Type *")

            },

            placeholder = {

                Text("Select Scheme Type")

            },

            leadingIcon = {

                Icon(

                    imageVector = Icons.Default.AccountTree,

                    contentDescription = null

                )

            },

            trailingIcon = {

                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )

            },

            supportingText = {

                if (schemeError) {

                    Text("Please select scheme type")

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

            schemeTypeList.forEach { type ->

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