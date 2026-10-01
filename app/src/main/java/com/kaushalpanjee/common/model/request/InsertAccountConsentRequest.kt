package com.kaushalpanjee.common.model.request

data class InsertAccountConsentRequest(
    val appVersion: String,
    val loginId: String,
    val accountNo: String,
    val consentId: String,
    val consentStatus: String,
    val accountMatch: String,
    val accountVerified: String,
    val aggregator: String
)