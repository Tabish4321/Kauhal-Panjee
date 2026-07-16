package com.kaushalpanjee.cbt


data class ExamUiState(

    val examStarted: Boolean = false,
    val currentIndex: Int = 0,
    val timeLeft: Int = 1800,
    val examFinished: Boolean = false,

    val answers: MutableMap<String,String> = mutableMapOf(),
    val markedQuestions: MutableMap<String,Boolean> = mutableMapOf(),

    val showReviewDialog: Boolean = false,
    val showSuccessDialog: Boolean = false,

    val loading:Boolean=false
)

//data class ExamUiState(
//
//    val examStarted: Boolean = false,
//    val currentIndex: Int = 0,
//    val timeLeft: Int = 1800,
//    val examFinished: Boolean = false,
//
//    val answers: Map<String,String> = emptyMap(),
//    val markedQuestions: Map<String,Boolean> = emptyMap(),
//
//    val showReviewDialog: Boolean = false,
//    val showSuccessDialog: Boolean = false
//)
