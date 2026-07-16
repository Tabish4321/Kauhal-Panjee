package com.kaushalpanjee.CBT.submit

import com.google.gson.annotations.SerializedName


data class SubmitExamRequest(
    @SerializedName("appVersion")
    val appVersion: String,

    @SerializedName("cand_id")
    val cand_id: String,

    @SerializedName("batch_id")
    val batch_id: String,

    @SerializedName("exam_id")
    val exam_id: String,

    @SerializedName("question_set_id")
    val question_set_id: String,

    @SerializedName("Ques_and_ans")
    val Ques_and_ans: List<SubmitExamItem>
)
//data class SubmitExamRequest(
//    val appVersion: String,
//    val cand_id: String,
//    val batch_id: String,
//    val exam_id: String,
//    val question_set_id: String,
//    val Ques_and_ans: List<SubmitExamItem>
//)
