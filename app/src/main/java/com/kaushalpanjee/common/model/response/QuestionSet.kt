package com.example.myapplication.CBT.api

import com.google.gson.annotations.SerializedName



data class CbtQuestionsResponse(
    @SerializedName("questionset")
    val questionset: QuestionSet? = null
)

data class QuestionSet(
    @SerializedName("exam_id")
    val exam_id: String? = null,

    @SerializedName("candidate_id")
    val candidate_id: String? = null,

    @SerializedName("question_set_id")
    val question_set_id: String? = null,

    @SerializedName("batch_id")
    val batch_id: String? = null,

    @SerializedName("exam_date_time")
    val exam_date_time: String? = null,

    @SerializedName("question")
    val question: List<Question>? = null
)

data class Question(
    @SerializedName("question_id")
    val question_id: String? = null,

    @SerializedName("question_value")
    val question_value: String? = null,

    @SerializedName("option")
    val option: List<Option>? = null,

    @SerializedName("marks_per_qs")
    val marks_per_qs: Double? = null
)

data class Option(
    @SerializedName("option_key")
    val option_key: String? = null,

    @SerializedName("option_value")
    val option_value: String? = null
)

//data class QuestionSet(
//    val exam_id: String,
//    val candidate_id: String,
//    val question_set_id: String,
//    val batch_id: String,
//    val exam_date_time: String,
//    val question: List<Question>
//)
//data class QuestionSet(
//    @SerializedName("exam_id")
//    val exam_id: String,
//    @SerializedName("candidate_id")
//    val candidate_id: String,
//    @SerializedName("question_set_id")
//    val question_set_id: String,
//    @SerializedName("batch_id")
//    val batch_id: String,
//    @SerializedName("exam_date_time")
//    val exam_date_time: String,
//    @SerializedName("question")
//    val question: List<Question>
//)
//
//data class Question(
//    @SerializedName("question_id")
//    val question_id: String,
//    @SerializedName("question_value")
//    val question_value: String,
//    @SerializedName("option")
//    val option: List<Option>,
//    @SerializedName("marks_per_qs")
//    val marks_per_qs: Double
//)
//
//data class Option(
//    @SerializedName("option_key")
//    val option_key: String,
//    @SerializedName("option_value")
//    val option_value: String
//)
//data class QuestionSet(
//    @SerializedName("exam_id")
//    val exam_id: String? = null,
//
//    @SerializedName("candidate_id")
//    val candidate_id: String? = null,
//
//    @SerializedName("question_set_id")
//    val question_set_id: String? = null,
//
//    @SerializedName("batch_id")
//    val batch_id: String? = null,
//
//    @SerializedName("exam_date_time")
//    val exam_date_time: String? = null,
//
//    @SerializedName("question")
//    val question: List<Question>? = null
//)



