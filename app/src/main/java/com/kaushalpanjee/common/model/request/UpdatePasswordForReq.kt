package com.kaushalpanjee.common.model.request

data class UpdatePasswordForReq(
    val newPassword: String,
    val loginId: String,
    val appVersion: String
)
