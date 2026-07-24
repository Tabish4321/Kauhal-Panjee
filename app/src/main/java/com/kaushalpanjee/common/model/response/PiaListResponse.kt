package com.kaushalpanjee.common.model.response

data class PiaListResponse(
    val wrappedList: List<PiaOrg>,
    val responseCode: Int,
    val responseDesc: String
)

data class PiaOrg(
    val piaOrgCode: String,
    val piaOrgName: String
)

