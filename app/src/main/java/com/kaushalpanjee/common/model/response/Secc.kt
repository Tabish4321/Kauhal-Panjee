package com.kaushalpanjee.common.model.response


data class WrappedListItem(
    val fathername: String,
    val ahltin: String,
    val seccname: String
)

data class SeccDetailsRes(
    val wrappedList: List<WrappedListItem>,
    val responseCode: Int,
    val responseDesc: String,
    val responseMsg: String
)
