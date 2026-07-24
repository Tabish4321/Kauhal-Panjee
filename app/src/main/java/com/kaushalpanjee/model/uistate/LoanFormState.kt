package com.kaushalpanjee.model.uistate

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class LoanFormState {

    var employmentType by mutableStateOf("Salaried")
    var loanAmount by mutableStateOf("")
}