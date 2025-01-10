package com.kaushalpanjee.common.model.response

data class ForgotIdOtpRes(
    val otp: String?,
    val responseCode: Int,
    val responseDesc: String,
    val password: String?
)

