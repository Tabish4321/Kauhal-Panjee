package com.kaushalpanjee.common.model.request


data class GetLoginIdNdPassReq(
    val appVersion: String,
    val mobileNo: String,
    val email: String,
    val imeiNo: String
)
