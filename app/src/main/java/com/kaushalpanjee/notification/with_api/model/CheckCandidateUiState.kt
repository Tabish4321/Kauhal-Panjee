package com.kaushalpanjee.notification.with_api.model

data class CheckCandidateUiState(
    val isLoading: Boolean = false,
    val isDialogVisible: Boolean = false,
    val message: String = "",
    val status: String = "",
    val showHappyUnhappy: Boolean = false   // 🔥 ADD THIS
)
//data class CheckCandidateUiState(
//    val isDialogVisible: Boolean = false,
//    val message: String = "",
//    val status: String = ""
//)
