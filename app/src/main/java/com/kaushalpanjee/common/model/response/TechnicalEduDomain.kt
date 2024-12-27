package com.kaushalpanjee.common.model.response

data class TechnicalEduDomain(
val domainList: List<Domain>,
val responseCode: Int,
val responseDesc: String,
val responseMsg: String?
)

data class Domain(
    val domainCode: String,
    val domainName: String
)