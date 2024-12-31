package com.kaushalpanjee.common.model.request

data class UserCreationReq(
val aadharNo: String,
val candidateName: String,
val gender: String,
val dateOfBirth: String,
val stateName: String,
val stateCode: String,
val districtName: String,
val blockName: String,
val postOffice: String,
val village: String,
val pinCode: String,
val mobileNo: String?,
val email: String?,
val careOf: String,
val street: String,
val appVersion: String,
val aadharImage: String,
val imeiNo: String
)

