package com.kaushalpanjee.common.model.response

data class PiaTrainingCenterRes(
    val wrappedList: List<TrainingCenter>,
    val responseCode: Int,
    val responseDesc: String
)

data class TrainingCenter(
    val trainingCenterId: Int,
    val trainingCenterName: String
)

