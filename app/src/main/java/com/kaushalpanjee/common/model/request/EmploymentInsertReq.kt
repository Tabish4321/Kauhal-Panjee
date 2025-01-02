package com.kaushalpanjee.common.model.request

data class EmploymentInsertReq(
    val appVersion: String,
    val loginId: String,
    val imeiNo: String,
    val sectionCount: String, // Static and mandatory
    val isEmployeed: String,
    val employmentMode: String,
    val intrestedIn: String,
    val employmentPreference: String,
    val jobLocationPreference: String,
    val monthlyEarning: Int,
    val expectedMonthlySalary: Int
)
