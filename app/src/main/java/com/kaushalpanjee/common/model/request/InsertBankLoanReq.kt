package com.kaushalpanjee.common.model.request

data class InsertBankLoanReq(

    val appVersion: String,
    val candidateId: String,
    val panNo: String,
    val durationAtCurrentAddress: String,

    val occupationType: String,
    val monthlySalary: Double,
    val employerName: String?,
    val workExperience: String?,

    val profession: String,
    val annualIncome: Int,
    val experience: String,

    val businessName: String,
    val natureOfBusiness: String,
    val businessAddress: String,
    val entityType: String,

    val projectCost: Int,
    val marginMoney: Int,
    val loanAmount: Int,

    val purpose: String,
    val tenure: String,

    val accountNo: String,
    val bankName: String,
    val accountType: String,

    val uploadPhotograph: String,
    val uploadAadhaar: String,
    val uploadPan: String
)
