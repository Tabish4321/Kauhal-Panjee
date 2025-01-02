package com.kaushalpanjee.common.model.request

data class SeccInsertReq(
    val appVersion: String,
    val loginId: String,
    val imeiNo: String,
    val sectionCount: String, // Static and mandatory
    val seccStateCode: String,
    val seccDistrictCode: String,
    val seccBlockCode: String,
    val seccGpCode: String,
    val seccVillageCode: String,
    val seccCandidateName: String,
    val seccAhlTin: String
)
