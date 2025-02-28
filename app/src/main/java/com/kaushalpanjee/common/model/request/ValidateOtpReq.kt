package com.kaushalpanjee.common.model.request

data class ValidateOtpReq(
    val appVersion :String,
    val email :String,
    val mobileNo :String,
    val imeiNo :String,val otp :String
)
