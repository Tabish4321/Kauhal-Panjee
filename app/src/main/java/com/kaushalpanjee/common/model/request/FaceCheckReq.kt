package com.kaushalpanjee.common.model.request

data class FaceCheckReq(
    val appVersion: String,
    val isFaceRegistered: String,
    val loginId: String
)
