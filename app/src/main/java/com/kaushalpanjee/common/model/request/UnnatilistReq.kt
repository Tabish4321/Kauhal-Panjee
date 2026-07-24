package com.kaushalpanjee.common.model.request

data class UnnatilistReq(
    var appVersion :String,
    var loginId :String,
    val imeiNo: String,
    val adhaarHash: String,

    )
