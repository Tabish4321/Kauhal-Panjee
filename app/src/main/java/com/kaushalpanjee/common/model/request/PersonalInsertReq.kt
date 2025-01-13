package com.kaushalpanjee.common.model.request

data class PersonalInsertReq(
val appVersion: String,
val loginId: String,
val imeiNo: String,
val sectionCount: String,
val guardianName: String,
val motherName: String,
val guardianMobileNo: String,
val annualFamilyIncome: String,
val voterId: String,
val voterIdCard: String,
val dlNo: String,
val dlCard: String,
val castCategory: String,
val categoryCertificate: String,
val maritalStatus: String,
val isMinority: String,
val minorityCert: String,
val isDisability: String,
val disablityCert: String,
val isNrega: String,
val nregaCard: String,
val nregaJobCard: String,
val isShg: String,
val shgNo: String,
val antyodaya: String,
val rationCard: String,
val isRsby: String,
val rsbyCard: String,
val isPip: String

)