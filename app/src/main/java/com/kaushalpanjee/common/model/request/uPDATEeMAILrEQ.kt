package com.kaushalpanjee.common.model.request

data class UpdateEmailReq(
    val appVersion: String,
    val loginId: String,
    val imeiNo: String,
    val email: String,

    )
