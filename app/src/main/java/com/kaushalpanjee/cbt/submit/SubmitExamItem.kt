package com.kaushalpanjee.CBT.submit

import com.google.gson.annotations.SerializedName


data class SubmitExamItem(

    @SerializedName("question_id")
    var question_id: String,

    @SerializedName("answer_given")
    var answer_given: String,

    @SerializedName("category")
      var category: String,

    @SerializedName("marks_per_qs")
      var marks_per_qs: Double
)
//data class SubmitExamItem(
//    val question_id: String,
//    val answer_given: String,
//    val category: String,
//    val marks_per_qs: Double   // 👈 change here
//)
//data class SubmitExamItem(
//    val question_id: String,
//    val answer_given: String,
//    val category: String,
//    val marks_per_qs: Int
//)
