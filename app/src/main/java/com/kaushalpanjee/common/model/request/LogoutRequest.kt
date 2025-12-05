package com.kaushalpanjee.common.model.request

data class LogoutRequest(
    val appVersion: String,
    val imeiNo: String,
    val loginId: String
)