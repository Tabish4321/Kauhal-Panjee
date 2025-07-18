package com.kaushalpanjee.common.model.request


data class AadhaarRekycReq(
    val aadharImage: String,
    val aadharNo: String,
    val appVersion: String,
    val blockName: String,
    val candidateName: String,
    val careOf: String,
    val dateOfBirth: String,
    val districtName: String,
    val gender: String,
    val pinCode: String,
    val postOffice: String,
    val stateName: String,
    val street: String,
    val village: String,
    val imeiNo: String,
    val loginId: String
)
