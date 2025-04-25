package com.kaushalpanjee.common.model.response

data class OtpValidateResponse(
    val responseCode: Int,
    val responseDesc: String,
    val responseFlag: String,
    val wrappedList: List<UserIdName>
)

data class UserIdName(
    val loginId: String
)