package com.kaushalpanjee.common.model.request

data class TrainingInsertReq(
    val appVersion: String,
    val loginId: String,
    val imeiNo: String,
    val sectionCount: String, // Static and mandatory
    val isPreTraining: String,
    val preCompletedTraining: String,
    val compTrainingDuration: String,
    val hearedAboutScheme: String,
    val hearedFrom: String,
    val intrestedSector: String,
    val intrestedTrade: String,
    val tradeCode: String
)
