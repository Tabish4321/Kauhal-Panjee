package com.kaushalpanjee.common.model.request

data  class InsertOjtReq (

    val appVersion: String,
    val candidateId: String,
    val batchId: Int,
    val workplaceId: Int,
    val employeersId: Int,
    val imeiNo: String,
    val checkIn: String,
    val checkOut: String,
    val attendanceDate: String,
    val totalHours: String,
    val latitute: String,
    val longitute: String,
    val address: String
)