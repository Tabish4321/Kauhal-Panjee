package com.kaushalpanjee.common.model.request

data class EducationalInsertReq(
    val appVersion: String,
    val loginId: String,
    val imeiNo: String,
    val sectionCount: String, // Static and mandatory
    val monthYearOfPassing: String,
    val languageKnown: String,
    val techQualification: String,
    val techEducationDomain: String,
    val highestClass: String
)
