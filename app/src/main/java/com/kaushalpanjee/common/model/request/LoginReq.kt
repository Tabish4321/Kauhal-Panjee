package com.kaushalpanjee.common.model.request


data class LoginReq(
    val loginId: String,
    val password: String,
    val imeiNo: String,
    val appVersion: String,
    val deviceName: String
)

