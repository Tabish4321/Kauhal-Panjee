package com.kaushalpanjee.common.model.response


data class TrainingCenterRes(
    val centerList: List<Center>,
    val responseCode: Int,
    val responseDesc: String
)

data class Center(
    val address: String,
    val centerImage: String,
    val centerName: String,
    val contactNo: String
)
