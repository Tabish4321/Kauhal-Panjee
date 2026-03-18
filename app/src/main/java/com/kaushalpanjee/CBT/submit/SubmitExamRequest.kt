package com.kaushalpanjee.CBT.submit

data class SubmitExamRequest(
    val cand_id: String,
    val batch_id: String,
    val exam_id: String,
    val question_set_id: String,
    val Ques_and_ans: List<SubmitExamItem>
)
