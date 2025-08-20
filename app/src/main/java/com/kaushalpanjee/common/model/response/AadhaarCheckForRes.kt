package com.kaushalpanjee.common.model.response

data class AadhaarCheckForRes(
    val responseCode: Int,
    val responseDesc: String,
    val candidateId: String
)
