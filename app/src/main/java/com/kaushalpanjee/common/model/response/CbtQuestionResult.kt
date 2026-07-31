package com.kaushalpanjee.common.model.response

import com.example.myapplication.CBT.api.QuestionSet


sealed class CbtQuestionResult {
    object Loading : CbtQuestionResult()

    data class Success(
        val data: QuestionSet
    ) : CbtQuestionResult()

    data class Error(
        val statusCode: Int? = null,
        val message: String
    ) : CbtQuestionResult()
}