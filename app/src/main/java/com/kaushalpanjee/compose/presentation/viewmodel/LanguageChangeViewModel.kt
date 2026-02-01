package com.kaushalpanjee.compose.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaushalpanjee.compose.presentation.contract.LanguageChangeEffect
import com.kaushalpanjee.compose.presentation.contract.LanguageChangeEvent
import com.kaushalpanjee.compose.presentation.contract.LanguageChangeState
import com.kaushalpanjee.core.util.AppUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Rishi Porwal
 */
@HiltViewModel
class LanguageChangeViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(LanguageChangeState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<LanguageChangeEffect>()
    val effect = _effect.asSharedFlow()

    fun process(event: LanguageChangeEvent) {
        when (event) {

            is LanguageChangeEvent.SelectLanguage -> {
                if (event.code == _state.value.selectedLanguage) {
                    return
                }

                _state.update {
                    it.copy(
                        pendingLanguage = event.code,
                        showConfirmDialog = true
                    )
                }
            }

            LanguageChangeEvent.ConfirmChange -> {
                val lang = _state.value.pendingLanguage ?: return
                viewModelScope.launch {
                    _effect.emit(LanguageChangeEffect.ApplyLanguage(lang))
                }
                _state.update {
                    it.copy(
                        selectedLanguage = lang,
                        pendingLanguage = null,
                        showConfirmDialog = false
                    )
                }
            }

            LanguageChangeEvent.DismissDialog -> {
                _state.update {
                    it.copy(
                        showConfirmDialog = false,
                        pendingLanguage = null
                    )
                }
            }
        }
    }

    fun setInitialLanguage(code: String) {
        _state.update { it.copy(selectedLanguage = code) }
    }
}
