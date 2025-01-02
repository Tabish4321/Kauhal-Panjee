package com.kaushalpanjee.common.model.request

data class EducationalInsertReq(
    val appVersion: String,
    val loginId: String,
    val imeiNo: String,
    val sectionCount: String, // Static and mandatory
    val highestEducation: String,
    val monthYearOfPassing: String,
    val languageKnown: String,
    val isTechEducate: String,
    val techQualification: String,
    val monthYearOfPassingTechEdu: String,
    val techEducationDomain: String
)
