package com.kaushalpanjee.common.model.response

data class LogoutResponse(
    val responseCode: Int,
    val responseDesc: String,
    val responseMsg: String
)