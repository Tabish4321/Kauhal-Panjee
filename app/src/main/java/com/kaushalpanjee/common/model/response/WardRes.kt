package com.kaushalpanjee.common.model.response

data class WardRes(
    val wrappedList: List<WardItem>,
    val responseCode: Int,
    val responseDesc: String,
    val responseMsg: String,
    val appCode: Any?
)



data class WardItem(
    val wardCode : String,
    val wardName: String
)
