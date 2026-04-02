package com.kaushalpanjee.common.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

// ================= PERSONAL =================
@Composable
fun PersonalStep() {

    PremiumCard("Personal Details") {

        InfoRow(Icons.Default.Person, "Full Name", "Tabish Jamal")
        InfoRow(Icons.Default.Badge, "Father / Mother Name", "XYZ Jamal")
        InfoRow(Icons.Default.DateRange, "Date of Birth", "01-01-1995")
        InfoRow(Icons.Default.Person, "Gender", "Male")
        InfoRow(Icons.Default.Favorite, "Marital Status", "Single")
        InfoRow(Icons.Default.Flag, "Nationality", "Indian")
        InfoRow(Icons.Default.CreditCard, "Aadhaar Number", "XXXX-XXXX-1234")
        InfoRow(Icons.Default.Phone, "Mobile Number", "123456789")
        InfoRow(Icons.Default.Email, "Email ID", "abc@orkut.com")
        InputField("PAN Number")

    }
}

// ================= ADDRESS =================
@Composable
fun AddressStep() {

    var residence by remember { mutableStateOf("") }
    var years by remember { mutableStateOf("") }

    PremiumCard("Address Details") {

        InfoRow(Icons.Default.Person, "Full Name", "Tabish Jamal")
        InfoRow(Icons.Default.Badge, "Father / Mother Name", "XYZ Jamal")
        InfoRow(Icons.Default.DateRange, "Date of Birth", "01-01-1995")
        InfoRow(Icons.Default.Person, "Gender", "Male")
        InfoRow(Icons.Default.Favorite, "Marital Status", "Single")
        InfoRow(Icons.Default.Flag, "Nationality", "Indian")
        InfoRow(Icons.Default.CreditCard, "Aadhaar Number", "XXXX-XXXX-1234")
        InfoRow(Icons.Default.Phone, "Mobile Number", "123456789")
        InfoRow(Icons.Default.Email, "Email ID", "abc@orkut.com")

        // ✅ DROPDOWN
        CommonDropdown(
            label = "Residence Type",
            options = listOf("Owned", "Rented"),
            selected = residence,
            onSelect = { residence = it }
        )

        // ✅ INPUT (same style)
        CommonInput(
            label = "Years at Address",
            value = years,
            onChange = { years = it },
            keyboard = KeyboardType.Number
        )
    }
}

// ================= EMPLOYMENT =================
@Composable
fun EmploymentStep() {

    var type by remember { mutableStateOf("Salaried") }

    PremiumCard("Employment Details") {

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

            FilterChip(
                selected = type == "Salaried",
                onClick = { type = "Salaried" },
                label = { Text("Salaried") }
            )

            FilterChip(
                selected = type == "Self",
                onClick = { type = "Self" },
                label = { Text("Self Employed") }
            )
        }

        Spacer(Modifier.height(12.dp))

        if (type == "Salaried") {

            InputField("Employer Name")
            InputField("Designation")
            InputField("Monthly Salary")
            InputField("Work Experience")
            InputField("Office Address")

        } else {

            InputField("Business Name")
            InputField("Nature of Business")
            InputField("Annual Income")
            InputField("Business Address")
        }
    }
}

// ================= LOAN =================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanStep() {

    val loanTypes = listOf("Home", "Personal", "Education", "Car")

    var selectedLoanType by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var tenure by remember { mutableStateOf("") }
    var purpose by remember { mutableStateOf("") }
    var emi by remember { mutableStateOf("") }

    PremiumCard("Loan Details") {

        DropdownField(
            label = "Loan Type",
            options = loanTypes,
            selected = selectedLoanType,
            onSelected = { selectedLoanType = it }
        )

        Spacer(Modifier.height(8.dp))

        InputFieldWithKeyboard("Loan Amount", amount, { amount = it }, KeyboardType.Number)
        InputFieldWithKeyboard("Loan Tenure", tenure, { tenure = it }, KeyboardType.Number)
        InputFieldWithKeyboard("Purpose", purpose, { purpose = it }, KeyboardType.Text)
        InputFieldWithKeyboard("Preferred EMI", emi, { emi = it }, KeyboardType.Number)
    }
}

// ================= BANK =================

@Composable
fun BankStep() {

    PremiumCard("Bank Details") {

        InputField("Bank Name")
        InputField("Account Number")
        InputField("IFSC Code")
        InputField("Branch Name")
    }
}

// ================= DOCUMENT =================
@Composable
fun DocumentStep() {

    PremiumCard("Upload Documents") {

        UploadItem("Aadhaar Card")
        UploadItem("PAN Card")
        UploadItem("Salary Slip / Income Proof")
        UploadItem("Bank Statement")
        UploadItem("Photograph")
    }
}