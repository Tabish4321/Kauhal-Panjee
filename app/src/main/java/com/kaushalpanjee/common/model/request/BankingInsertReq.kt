package com.kaushalpanjee.common.model.request

data class BankingInsertReq(
    val appVersion: String,
    val loginId: String,
    val imeiNo: String,
    val sectionCount: String,
    val bankCode: String,
    val bankBranchCode: String,
    val bankAccountNo: String,
    val ifscCode: String,
    val panNo: String
)
