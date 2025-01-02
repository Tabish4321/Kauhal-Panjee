package com.kaushalpanjee.common.model.response


data class InsertRes(
    val wrappedList: List<Any> = emptyList(),
    val responseCode: Int,
    val responseDesc: String,
    val responseMsg: String
)
