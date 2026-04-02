package com.kaushalpanjee.common.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanStepperScreen(onBack: () -> Unit) {

    var step by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Loan Application") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, null)
                        }
                    }
                )
                StepHeader(step) // 🔥 sticky
            }
        },

        bottomBar = {
            BottomButtons(
                step = step,
                onNext = { if (step < 5) step++ },
                onBack = { step-- }
            )
        }

    ) { padding ->

        LazyColumn(
            Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            item {
                when (step) {
                    0 -> PersonalStep()
                    1 -> AddressStep()
                    2 -> EmploymentStep()
                    3 -> LoanStep()
                    4 -> BankStep()
                    5 -> DocumentStep()
                }
            }

            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}