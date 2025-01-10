package com.kaushalpanjee.common.model.request



data class ChangePassReq(
    val appVersion: String,
    val loginId: String,
    val oldPassword: String,
    val newPassword: String
)
