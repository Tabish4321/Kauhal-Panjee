package com.example.myapplication.CBT.api

data class QuestionSet(
    val exam_id: String,
    val candidate_id: String,
    val question_set_id: String,
    val batch_id: String,
    val exam_date_time: String,
    val question: List<Question>
)
