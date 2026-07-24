package com.kaushalpanjee.common.model.response

data class UnnatiListResponse(  val wrappedList: List<WrappedItem>,
                                val responseCode: Int,
                                val responseDesc: String,
                                val responseMsg: String,
                                val appCode: String?
)

data class WrappedItem(
    val scheme: List<String>,
    val status: String
)
