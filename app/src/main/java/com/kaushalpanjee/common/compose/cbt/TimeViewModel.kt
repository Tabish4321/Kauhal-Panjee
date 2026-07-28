package com.kaushalpanjee.common.compose.cbt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for CBT Exam Screen
 * Manages all exam state, timer, and exam submission
 */

class TimeViewModel : ViewModel() {

    private val _examStarted = MutableStateFlow(false)
    val examStarted: StateFlow<Boolean> = _examStarted
    private val _timeLeft = MutableStateFlow(1800)
    val timeLeft: StateFlow<Int> = _timeLeft

    private val _examFinished = MutableStateFlow(false)
    val examFinished: StateFlow<Boolean> = _examFinished

    private var timerJob: Job? = null

    fun startExam() {
        _examStarted.value = true
    }

    fun startTimer() {
        if (timerJob?.isActive == true) return

        timerJob = viewModelScope.launch {

            while (_timeLeft.value > 0 && !_examFinished.value) {
                delay(1000)
                _timeLeft.value--
            }

            if (_timeLeft.value <= 0) {
                _examFinished.value = true
            }
        }
    }

}

