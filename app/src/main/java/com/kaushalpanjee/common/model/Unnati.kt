package com.kaushalpanjee.common.model

data class Unnati(
    val `data`: Data,
    val message: String,
    val responseCode: String
)

data class Data(
    val DDUGKY: String,
    val NRLM: String,
    val PMKVY: String,
    val PM_VISHWAKARMA: String,
    val RSETI: String
)