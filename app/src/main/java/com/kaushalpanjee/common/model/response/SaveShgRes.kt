package com.kaushalpanjee.common.model.response

data class SaveShgRes(
    val wrappedList: List<ShgMemberUpdate>? = emptyList(),
    val responseCode: Int? = null,
    val responseDesc: String? = null,
    val responseMsg: String? = null
)

data class ShgMemberUpdate(
    val member_name: String? = null,
    val action: String? = null
)