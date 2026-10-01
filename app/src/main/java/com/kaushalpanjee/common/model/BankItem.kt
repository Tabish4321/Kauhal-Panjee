package com.kaushalpanjee.common.model


data class BankItem(
    val bankCode: Int,
    val bankName: String,
    val accountNumber: String,
    val ifscCode: String,
    val panNo: String,
    val consentId: String,
    val consentStatus: String,
    val aggregator: String,
    val accountVerified: String,
    val accountMatch: String,
    val fipId: String
)
