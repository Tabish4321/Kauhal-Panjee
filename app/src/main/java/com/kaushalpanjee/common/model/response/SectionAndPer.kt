package com.kaushalpanjee.common.model.response

data class SectionAndPer(
    val wrappedList: List<StatusItem>,
    val responseCode: Int,
    val responseDesc: String,
    val responseMsg: String
)

data class StatusItem(
    val personalStatus: Int,
    val imagePath: String,
    val educationalStatus: Int,
    val trainingStatus: Int,
    val seccStatus: Int,
    val addressStatus: Int,
    val employmentStatus: Int,
    val bankingStatus: Int,
    val totalPercentage: Float,
    val isFaceRegistred: String,
    val candidateName: String
)
