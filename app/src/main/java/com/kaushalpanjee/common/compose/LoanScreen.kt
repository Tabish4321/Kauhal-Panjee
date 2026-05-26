package com.kaushalpanjee.common.compose

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kaushalpanjee.common.CommonViewModel
import com.kaushalpanjee.core.util.AppUtil
import com.kaushalpanjee.core.util.Resource

// ================= LOAN STEPPER SCREEN =================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanStepperScreen(
    onBack: () -> Unit,
    viewModel: CommonViewModel,
    context: Context
) {

    var step by remember { mutableStateOf(0) }

    val apiState by viewModel.getBankLoanDetails.collectAsState()

    // ================= API HIT =================

    LaunchedEffect(Unit) {

        viewModel.getBankLoanDetails(
            header = AppUtil.getSavedTokenPreference(context)
        )
    }

    // ================= API DATA =================

    val candidateData =
        (apiState as? Resource.Success)
            ?.data
            ?.wrappedList
            ?.firstOrNull()

    // ================= FORM STATE =================

    val state = remember { LoanFormState() }

    // ================= DYNAMIC STEPS =================

    val isBusinessUser =
        state.employmentType != "Salaried"

    val steps = remember(isBusinessUser) {

        if (isBusinessUser) {

            listOf(
                "Personal",
                "Contact",
                "Employment",
                "Business",
                "Loan",
                "Bank",
                "Document"
            )

        } else {

            listOf(
                "Personal",
                "Contact",
                "Employment",
                "Loan",
                "Bank",
                "Document"
            )
        }
    }

    val totalSteps = steps.size - 1

    // ================= UI =================

    Scaffold(

        topBar = {

            Column {

                TopAppBar(

                    title = {
                        Text("Loan Application")
                    },

                    navigationIcon = {

                        IconButton(
                            onClick = onBack
                        ) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = null
                            )
                        }
                    }
                )

                StepHeader(
                    step = step,
                    totalSteps = totalSteps
                )
            }
        },

        bottomBar = {

            BottomButtons(

                step = step,
                totalSteps = totalSteps,
                state = state,
                candidateData = candidateData,
                viewModel = viewModel,
                context = context,

                onNextStep = {
                    step++
                },

                onBack = {

                    if (step > 0) {
                        step--
                    }
                }
            )
        }

    ) { padding ->

        when (apiState) {

            is Resource.Loading -> {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is Resource.Error -> {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        (apiState as Resource.Error)
                            .data?.responseDesc ?: "Error"
                    )
                }
            }

            else -> {

                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .padding(16.dp)
                ) {

                    item {

                        when (steps[step]) {

                            "Personal" -> {

                                PersonalStep(
                                    candidate = candidateData,
                                    state = state
                                )
                            }

                            "Contact" -> {

                                ContactStep(
                                    candidate = candidateData,
                                    state = state
                                )
                            }

                            "Employment" -> {

                                EmploymentStep(
                                    state = state
                                )
                            }

                            "Business" -> {

                                BusinessStep(
                                    state = state
                                )
                            }

                            "Loan" -> {

                                LoanStep(
                                    state = state
                                )
                            }

                            "Bank" -> {

                                BankStep(
                                    state = state
                                )
                            }

                            "Document" -> {

                                DocumentStep(
                                    state = state
                                )
                            }
                        }
                    }

                    item {
                        Spacer(
                            modifier = Modifier.height(80.dp)
                        )
                    }
                }
            }
        }
    }
}