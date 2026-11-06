package com.kaushalpanjee.common.model.response

data class getShgRes(
    val wrappedList: List<ShgItem>? = emptyList(),
                       val responseCode: Int? = null,
                       val responseDesc: String? = null
)

data class ShgItem(
    val code: String? = null,
    val name: String? = null,
    val shgNameLocal: String? = null,
    val formationDate: String? = null,
    val shgType: String? = null
)