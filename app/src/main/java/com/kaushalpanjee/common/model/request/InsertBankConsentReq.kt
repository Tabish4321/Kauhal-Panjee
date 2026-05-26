package com.kaushalpanjee.common.model.request

data class InsertBankConsentReq (
    val appVersion: String,
    val mobileNo: String,
    val consentId: String,
    val consentHandleId: String,
    val status: String,
    val candidateId: String
)