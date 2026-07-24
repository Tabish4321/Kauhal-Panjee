package com.kaushalpanjee.common.model.response


data class AebasRes(
    val wrappedList: List<AttendanceDetails>,
    val responseCode: Int,
    val responseDesc: String,
    val responseMsg: String
)

data class AttendanceDetails(
    val empStatus: String,
    val empLocation: String,
    val regDate: String,
    val attendanceId: Long,
    val previousEmpCode: String
)