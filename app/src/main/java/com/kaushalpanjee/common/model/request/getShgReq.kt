package com.kaushalpanjee.common.model.request

data class getShgReq(
    val appVersion: String,
    val stateShortName: String,
    val blockCode: String,
    val gpCode: String
)
