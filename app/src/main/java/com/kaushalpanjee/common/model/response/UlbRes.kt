package com.kaushalpanjee.common.model.response

data class UlbRes(

    val wrappedList: List<UlbItem>,
    val responseCode: Int,
    val responseDesc: String,
    val responseMsg: String
)


data class UlbItem(
    val ulbCode: String,
    val ulbName: String
)
