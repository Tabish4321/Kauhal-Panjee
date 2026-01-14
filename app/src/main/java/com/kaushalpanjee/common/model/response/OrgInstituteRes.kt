package com.kaushalpanjee.common.model.response


data class OrgInstituteRes(
    val wrappedList: List<Institute>,
    val responseCode: Int,
    val responseDesc: String
)

data class Institute(
    val instituteId: Int,
    val instituteName: String
)
