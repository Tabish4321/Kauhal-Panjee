package com.kaushalpanjee.common.model.response


data class GetDetailsBankLoanRes(
    val wrappedList: List<CandidateBankLoan>,
    val responseCode: Int,
    val responseDesc: String,
    val responseMsg: String
)

data class CandidateBankLoan(
    val cast: String,
    val address: String,
    val candidateName: String,
    val gender: String,
    val guardianName: String,
    val imagePath: String?,
    val dateOfBirth: String,
    val emailId: String,
    val highestEducation: String,
    val mobileNo: String,
    val candidateId: String
)