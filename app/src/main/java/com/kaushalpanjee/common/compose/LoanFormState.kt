package com.kaushalpanjee.common.compose

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class LoanFormState {


    // EMPLOYMENT
    var employmentType by mutableStateOf("Salaried")

    var monthlySalary by mutableStateOf("")
    var employerName by mutableStateOf("")
    var workExperience by mutableStateOf("")

    var profession by mutableStateOf("")
    var annualIncome by mutableStateOf("")
    var experience by mutableStateOf("")

    // BUSINESS
    var businessName by mutableStateOf("")
    var natureOfBusiness by mutableStateOf("")
    var businessAddress by mutableStateOf("")
    var entityType by mutableStateOf("")
    var projectCost by mutableStateOf("")
    var marginMoney by mutableStateOf("")

    // LOAN
    var loanAmount by mutableStateOf("")
    var purpose by mutableStateOf("")
    var tenure by mutableStateOf("")

    // BANK
    var accountNo by mutableStateOf("")
    var bankName by mutableStateOf("")
    var accountType by mutableStateOf("")

    // PERSONAL
    var panNo by mutableStateOf("")
    var durationAtCurrentAddress by mutableStateOf("")

    // DOCUMENTS
    var uploadPhotograph by mutableStateOf("")
    var uploadAadhaar by mutableStateOf("")
    var uploadPan by mutableStateOf("")

}