package com.kaushalpanjee.common.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.kaushalpanjee.common.model.response.CandidateBankLoan
import com.kaushalpanjee.core.util.AppUtil.validatePAN


// ================= PERSONAL =================

@Composable
fun PersonalStep(
    candidate: CandidateBankLoan?,
    state: LoanFormState
) {

    var isPanValid by remember { mutableStateOf(false) }

    var showImage by remember { mutableStateOf(false) }

    val dummyBase64 = candidate?.imagePath ?: ""

    PremiumCard("Personal Information") {

        InfoRow(
            Icons.Default.Person,
            "Full Name",
            candidate?.candidateName ?: ""
        )

        InfoRow(
            Icons.Default.DateRange,
            "Date of Birth",
            candidate?.dateOfBirth ?: ""
        )

        InfoRow(
            Icons.Default.Person,
            "Gender",
            candidate?.gender ?: ""
        )

        InfoRow(
            Icons.Default.Person,
            "Father / Guardian",
            candidate?.guardianName ?: ""
        )

        InfoRow(
            Icons.Default.School,
            "Educational Qualification",
            candidate?.highestEducation ?: ""
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    "Category",
                    style = MaterialTheme.typography.labelSmall
                )

                Text(
                    candidate?.cast ?: "",
                    fontWeight = FontWeight.Bold
                )
            }

            if (dummyBase64.isNotEmpty()) {

                IconButton(
                    onClick = {
                        showImage = true
                    }
                ) {
                    Icon(
                        Icons.Default.Visibility,
                        contentDescription = null
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        PanInputField(
            pan = state.panNo,
            onChange = {

                state.panNo = it.uppercase()

                isPanValid = validatePAN(state.panNo)
            },
            isValid = isPanValid
        )
    }

    if (showImage) {

        ImagePreviewDialog(
            base64Image = dummyBase64,
            onDismiss = {
                showImage = false
            }
        )
    }
}


// ================= CONTACT =================

@Composable
fun ContactStep(
    candidate: CandidateBankLoan?,
    state: LoanFormState
) {


    PremiumCard("Contact Details") {

        InfoRow(
            Icons.Default.Home,
            "Permanent Address",
            candidate?.address ?: ""
        )

        InfoRow(
            Icons.Default.LocationOn,
            "Current Address",
            candidate?.address ?: ""
        )

        InfoRow(
            Icons.Default.Phone,
            "Mobile Number",
            candidate?.mobileNo ?: ""
        )

        InfoRow(
            Icons.Default.Email,
            "Email ID",
            candidate?.emailId ?: ""
        )

        CommonInput(
            "Duration at Current Address (In Year)",
            state.durationAtCurrentAddress,
            {
                state.durationAtCurrentAddress = it
            },
            KeyboardType.Number
        )
    }
}


// ================= EMPLOYMENT =================
@Composable
fun EmploymentStep(state: LoanFormState) {


    PremiumCard("Employment Details") {

        //  2 TYPE DROPDOWN
        CommonDropdown(
            label = "Occupation Type",
            options = listOf("Salaried", "Self Employed"),
            selected = state.employmentType,
            onSelect = { state.employmentType = it }
        )

        Spacer(Modifier.height(12.dp))

        // ================= SALARIED =================
        if (state.employmentType == "Salaried") {

            CommonInput(
                "Monthly Salary",
                state.monthlySalary,
                { state.monthlySalary = it },
                KeyboardType.Number
            )

            CommonInput(
                "Employer Name",
                state.employerName,
                { state.employerName = it }
            )

            CommonInput(
                "Work Experience (Years)",
                state.workExperience,
                { state.workExperience = it },
                KeyboardType.Number
            )
        }

        // ================= SELF EMPLOYED =================
        else if (state.employmentType == "Self Employed") {

            CommonInput(
                "Profession",
                state.profession,
                { state.profession = it }
            )

            CommonInput(
                "Annual Income",
                state.annualIncome,
                { state.annualIncome = it },
                KeyboardType.Number
            )

            CommonInput(
                "Experience (Years)",
                state.experience,
                { state.experience = it },
                KeyboardType.Number
            )
        }

    }
}

// ================= BUSINESS =================

@Composable
fun BusinessStep(
    state: LoanFormState
) {

    PremiumCard("Business Details") {

        CommonInput(
            "Business Name",
            state.businessName,
            { state.businessName = it }
        )

        CommonInput(
            "Nature of Business",
            state.natureOfBusiness,
            { state.natureOfBusiness = it }
        )

        CommonInput(
            "Business Address",
            state.businessAddress,
            { state.businessAddress = it }
        )

        CommonDropdown(
            label = "Entity Type",

            options = listOf(
                "Proprietorship",
                "Partnership",
                "Pvt Ltd"
            ),

            selected = state.entityType,

            onSelect = {
                state.entityType = it
            }
        )

        CommonInput(
            "Project Cost",

            state.projectCost,

            { state.projectCost = it },

            KeyboardType.Number
        )

        CommonInput(
            "Margin Money",

            state.marginMoney,

            { state.marginMoney = it },

            KeyboardType.Number
        )
    }
}

// ================= LOAN =================
@Composable
fun LoanStep(state: LoanFormState) {



    PremiumCard("Loan Details") {

        CommonInput("Loan Amount", state.loanAmount, {
            state.loanAmount = it
        }, KeyboardType.Number)

        CommonInput("Purpose", state.purpose, {
            state.purpose = it
        })
        CommonInput("Tenure", state.tenure, {
            state.tenure = it
        }, KeyboardType.Number)
    }
}

// ================= BANK =================

@Composable
fun BankStep(
    state: LoanFormState
) {

    PremiumCard("Bank Details") {

        CommonInput(
            "Account Number",

            state.accountNo,

            { state.accountNo = it },

            KeyboardType.Number,

            true
        )

        CommonInput(
            "Bank Name",

            state.bankName,

            { state.bankName = it }
        )

        CommonDropdown(

            label = "Account Type",

            options = listOf(
                "Savings",
                "Current"
            ),

            selected = state.accountType,

            onSelect = {
                state.accountType = it
            }
        )
    }
}

// ================= DOCUMENT =================

@Composable
fun DocumentStep(
    state: LoanFormState
) {

    PremiumCard("Upload Documents") {

        UploadItem(

            title = "Photograph",

            onFileSelected = {

                state.uploadPhotograph = it
            }
        )

        UploadItem(

            title = "PAN",

            onFileSelected = {

                state.uploadPan = it
            }
        )

        UploadItem(

            title = "Aadhaar",

            onFileSelected = {

                state.uploadAadhaar = it
            }
        )
    }
}

