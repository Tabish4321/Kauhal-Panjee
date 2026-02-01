package com.kaushalpanjee.compose.presentation.contract

/**
 * Created by Rishi Porwal
 */

sealed interface LanguageChangeEvent {
    data class SelectLanguage(val code: String) : LanguageChangeEvent
    object ConfirmChange : LanguageChangeEvent
    object DismissDialog : LanguageChangeEvent
}

data class LanguageChangeState(
    val selectedLanguage: String = "en",
    val showConfirmDialog: Boolean = false,
    val pendingLanguage: String? = null
)

sealed interface LanguageChangeEffect {
 //   object NavigateBack : LanguageChangeEffect
    data class ApplyLanguage(val code: String) : LanguageChangeEffect

}