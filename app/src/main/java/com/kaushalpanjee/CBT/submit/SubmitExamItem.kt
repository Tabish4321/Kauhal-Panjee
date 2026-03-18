package com.kaushalpanjee.CBT.submit


data class SubmitExamItem(
    val question_id: String,
    val answer_given: String,
    val category: String,
    val marks_per_qs: Double   // 👈 change here
)
//data class SubmitExamItem(
//    val question_id: String,
//    val answer_given: String,
//    val category: String,
//    val marks_per_qs: Int
//)
