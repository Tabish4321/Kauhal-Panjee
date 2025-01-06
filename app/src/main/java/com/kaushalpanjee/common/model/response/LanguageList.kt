package com.kaushalpanjee.common.model.response

data class LanguageList(
    val languageList: List<LanguageItem>,
    val responseCode: Int,
    val responseDesc: String,
    val responseMsg: String
)

data class LanguageItem(
    val languageCode: String,
    val languageName: String
)
