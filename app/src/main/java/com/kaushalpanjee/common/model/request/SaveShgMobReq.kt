package com.kaushalpanjee.common.model.request

data class SaveShgMobReq(
    val appVersion: String,
    val candidateId: String,
    val stateShortName: String,
    val shgId: Long,
    val shgCode: String,
    val shgName: String,
    val shgNameLocal: String?,
    val nicShgCode: String?,
    val memberId: Long,
    val memberCode: String,
    val memberName: String,
    val aadharVerified: Boolean,
    val gender: String,
    val relationName: String?,
    val dob: String?,
    val designation: String?,
    val phone: String?,
    val rtoken: String?,
    val memberStateCode: String?,
    val districtCode: String?,
    val blockCode: String?,
    val panchayatCode: String?,
    val villageCode: String?
)