package com.kaushalpanjee.common.model.response

// Data class to represent each bank detail
data class BankDetail(
    val branchCode: String,
    val bankCode: String,
    val branchName: String,
    val bankName: String,
    val accLength: String

)

// Data class to represent the overall response
data class BankingRes(
    val bankDetailsList: List<BankDetail>,
    val responseCode: Int,
    val responseDesc: String,
    val responseMsg: String
)
