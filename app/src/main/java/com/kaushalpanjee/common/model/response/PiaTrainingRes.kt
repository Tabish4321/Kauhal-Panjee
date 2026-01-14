package com.kaushalpanjee.common.model.response

data class PiaTrainingRes(
    val wrappedList: List<Trades>,
    val responseCode: Int,
    val responseDesc: String
)

data class Trades(
    val courseName: String,
    val courseId: Int
)


