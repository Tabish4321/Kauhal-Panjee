package com.kaushalpanjee.cbt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

/**
 * ViewModel for CBT Exam Screen
 * Manages all exam state, timer, and exam submission
 */
class TimeViewModel : ViewModel() {

    // UI State
    private val _examStarted = MutableStateFlow(false)
    val examStarted: StateFlow<Boolean> = _examStarted

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex

    private val _timeLeft = MutableStateFlow(1800) // 30 minutes in seconds
//     private val _timeLeft = MutableStateFlow(60)
//      private val _timeLeft = MutableStateFlow(15)
    val timeLeft: StateFlow<Int> = _timeLeft

    private val _examFinished = MutableStateFlow(false)
    val examFinished: StateFlow<Boolean> = _examFinished

    private val _showReviewDialog = MutableStateFlow(false)

    val showReviewDialog: StateFlow<Boolean> = _showReviewDialog
//    private val _reviewQuestions = MutableStateFlow(false)
//    val reviewQuestions: StateFlow<Boolean> = _reviewQuestions

    private val _reviewQuestions = MutableStateFlow<Set<String>>(emptySet())

    val reviewQuestions: StateFlow<Set<String>> = _reviewQuestions

    private val _showSuccessDialog = MutableStateFlow(false)
    val showSuccessDialog: StateFlow<Boolean> = _showSuccessDialog





    // Timer Management

    fun startTimer() {
        viewModelScope.launch(Dispatchers.Default) {
            while (!_examFinished.value && _timeLeft.value > 0) {
                kotlinx.coroutines.delay(1000)
                _timeLeft.value = _timeLeft.value - 1
            }

            if (_timeLeft.value == 0) {
                _examFinished.value = true
                _showSuccessDialog.value = true
            }
        }
    }



}

