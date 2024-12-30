package com.kaushalpanjee.common.model.response

data class WhereHaveYouHeardRes(
    val surveyList: List<SurveyItem>,
    val responseCode: Int,
    val responseDesc: String,
    val responseMsg: String?
)

data class SurveyItem(
    val mediumName: String,
    val surveyCode: String
)


