package com.kaushalpanjee.common.model.request

data class InsertTrainingCenterReq(
    val appVersion: String,
    val loginId: String,
    val trainingCenterId: Int,
    val schemeType: String,
    val districtCode: String,
    val piaId: String,
    val orgId: String,
    val instituteId: String,
    val courseId: Int
)

