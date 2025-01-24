package com.kaushalpanjee.common.model.response


data class AadhaarDetailRes(
    val wrappedList: List<UserDetails>,
    val responseCode: Int,
    val responseDesc: String,
    val responseMsg: String
)

data class UserDetails(
    val appVersion: String,
    val loginId: String,
    val districtName: String,
    val gender: String,
    val blockName: String,
    val imagePath: String,
    val dateOfBirth: String,
    val emailId: String,
    val mobileNo: String,
    val statename: String,
    val regStateCode: String,
    val regState: String,
    val aadharNo: String,
    val userName: String,
    val deviceDate: String,
    val deviceName: String,
    val comAddress: String,
    val careOf: String,
    val imeiNo: String,
    val street: String,
    val pinCode: String,
    val village: String,
    val postOffice: String
)
