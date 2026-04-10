package com.kaushalpanjee.common.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kaushalpanjee.model.uistate.LoanFormState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanStepperScreen(onBack: () -> Unit) {

    var step by remember { mutableStateOf(0) }
    val state = remember { LoanFormState() }

    val isBusinessUser = state.employmentType != "Salaried"

    // 🔥 dynamic step list (BEST PRACTICE)
    val steps = remember(isBusinessUser) {
        if (isBusinessUser) {
            listOf(
                "Personal",
                "Contact",
                "Employment",
                "Business",
                "Loan",
                "Bank",
                "Obligation",
                "Declaration",
                "Document"
            )
        } else {
            listOf(
                "Personal",
                "Contact",
                "Employment",
                "Loan",
                "Bank",
                "Obligation",
                "Declaration",
                "Document"
            )
        }
    }

    val totalSteps = steps.size - 1

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
                StepHeader(step)
            }
        },

        bottomBar = {
            BottomButtons(
                step = step,
                onNext = {
                    if (step < totalSteps) step++
                },
                onBack = {
                    if (step > 0) step--
                }
            )
        }

    ) { padding ->

        LazyColumn(
            Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            item {

                when (steps[step]) {

                    "Personal" -> PersonalStep()

                    "Contact" -> ContactStep()

                    "Employment" -> EmploymentStep(state)

                    "Business" -> BusinessStep()

                    "Loan" -> LoanStep(state)

                    "Bank" -> BankStep()

                    "Obligation" -> ObligationStep(state)

                    "Declaration" -> DeclarationStep()

                    "Document" -> DocumentStep()
                }
            }

            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}