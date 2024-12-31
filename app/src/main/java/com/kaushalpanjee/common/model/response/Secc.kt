package com.kaushalpanjee.common.model.response


data class WrappedListItem(
    val fatherName: String,
    val ahltin: String,
    val seccName: String
)

data class SeccDetailsRes(
    val wrappedList: List<WrappedListItem>,
    val responseCode: Int,
    val responseDesc: String,
    val responseMsg: String
)
