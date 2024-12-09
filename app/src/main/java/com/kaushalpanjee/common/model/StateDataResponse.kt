package com.kaushalpanjee.common.model


data class StateDataResponse(
    val responseCode: Int,
    val responseDesc: String,
    val wrappedList: MutableList<WrappedList>
)

data class WrappedList(
    val state_name: String,
    val state_code: String,
    var isSelected: Boolean = false
)