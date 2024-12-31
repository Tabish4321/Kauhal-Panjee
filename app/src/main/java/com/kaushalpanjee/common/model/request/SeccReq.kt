package com.kaushalpanjee.common.model.request

data class SeccReq(
    val appVersion: String,
    val lgdVillCode: String,
    val seccName: String,
    val loginId: String,
    val imeiNo: String
)
