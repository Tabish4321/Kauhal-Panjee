package com.example.myapplication.CBT.api

data class Question(
    val question_id: String,
    val question_value: String,
    val option: List<Option>,
    val marks_per_qs: Double
)
