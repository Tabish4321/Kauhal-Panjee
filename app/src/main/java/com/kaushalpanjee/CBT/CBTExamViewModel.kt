package com.kaushalpanjee.CBT

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.example.myapplication.CBT.api.Question
import com.google.gson.Gson
import com.kaushalpanjee.BuildConfig
import com.kaushalpanjee.CBT.submit.RetrofitClient
import com.kaushalpanjee.CBT.submit.SubmitExamItem
import com.kaushalpanjee.CBT.submit.SubmitExamRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

/**
 * ViewModel for CBT Exam Screen
 * Manages all exam state, timer, and exam submission
 */
class CBTExamViewModel : ViewModel() {

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
    private val _reviewQuestions = MutableStateFlow(false)
    val reviewQuestions: StateFlow<Boolean> = _reviewQuestions

    private val _showSuccessDialog = MutableStateFlow(false)
    val showSuccessDialog: StateFlow<Boolean> = _showSuccessDialog

    private val _editMode = MutableStateFlow(false)
    val editMode: StateFlow<Boolean> = _editMode

    private val _submissionLoading = MutableStateFlow(false)
    val submissionLoading: StateFlow<Boolean> = _submissionLoading

    private val _submissionError = MutableStateFlow<String?>(null)
    val submissionError: StateFlow<String?> = _submissionError

    // Answer tracking
    private val _answers = MutableStateFlow<Map<String, String>>(emptyMap())
    val answers: StateFlow<Map<String, String>> = _answers

    private val _markedQuestions = MutableStateFlow<Set<String>>(emptySet())
    val markedQuestions: StateFlow<Set<String>> = _markedQuestions

    private val _questionStatus = MutableStateFlow<Map<String, String>>(emptyMap())
    val questionStatus: StateFlow<Map<String, String>> = _questionStatus

    // UI Event Methods
    fun startExam() {
        _examStarted.value = true
    }

    fun nextQuestion(maxQuestions: Int) {
        if (_currentIndex.value < maxQuestions - 1) {
            _currentIndex.value += 1
        }
    }

    fun previousQuestion() {
        if (_currentIndex.value > 0) {
            _currentIndex.value -= 1
        }
    }

    fun goToQuestion(index: Int) {
        _currentIndex.value = index
    }
    fun clearSubmissionError() {
        _submissionError.value = null
    }
    fun toggleReviewDialog() {
        _showReviewDialog.value = !_showReviewDialog.value
    }

    fun closeReviewDialog() {
        _showReviewDialog.value = false
    }

    fun closeSuccessDialog() {
        _showSuccessDialog.value = false
    }

    // Answer Management
    fun selectAnswer(questionId: String, optionKey: String) {
        val currentAnswers = _answers.value.toMutableMap()
        currentAnswers[questionId] = optionKey
        _answers.value = currentAnswers
    }

    fun clearAnswer(questionId: String) {
        val currentAnswers = _answers.value.toMutableMap()
        currentAnswers.remove(questionId)
        _answers.value = currentAnswers

        val currentStatus = _questionStatus.value.toMutableMap()
        currentStatus.remove(questionId)
        _questionStatus.value = currentStatus
    }

    fun markQuestion(questionId: String) {
        val currentMarked = _markedQuestions.value.toMutableSet()
        if (currentMarked.contains(questionId)) {
            currentMarked.remove(questionId)
        } else {
            currentMarked.add(questionId)
        }
        _markedQuestions.value = currentMarked
    }

    fun saveAndNext(questionId: String, actionText: String, maxQuestions: Int) {
        val currentStatus = _questionStatus.value.toMutableMap()
        currentStatus[questionId] = actionText
        _questionStatus.value = currentStatus

        if (_currentIndex.value < maxQuestions - 1) {
            _currentIndex.value += 1
        }
    }

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
//    fun startTimer() {
//        viewModelScope.launch(Dispatchers.Default) {
//            while (!_examFinished.value && _timeLeft.value > 0) {
//                kotlinx.coroutines.delay(1000)
//                _timeLeft.value = _timeLeft.value - 1
//            }
//
//            if (_timeLeft.value == 0) {
//                _examFinished.value = true
//                _showSuccessDialog.value = true
//            }
//        }
//    }

    // Exam Submission
    fun submitExam(
        questionList: List<Question>,
        candidateId: String,
        batchId: String,
        examId: String,
        questionSetId: String,
        context: Context,

    ) {
        viewModelScope.launch {
            _submissionLoading.value = true
            _submissionError.value = null

            try {
                val submitList = questionList.map { question ->
                    val answerGiven = _answers.value[question.question_id] ?: ""

                    val category = when {
                        _markedQuestions.value.contains(question.question_id) -> "Mark & Review"
                        answerGiven.isNotEmpty() -> "Save & Next"
                        else -> "Save & Next"
                    }

                    question.question_id?.let {
                        question.marks_per_qs?.let { marks_per_qs ->
                            SubmitExamItem(
                                question_id = it,
                                answer_given = answerGiven,
                                category = category,
                                marks_per_qs = marks_per_qs
                            )
                        }
                    }
                }

                val request = SubmitExamRequest(
                    appVersion = BuildConfig.VERSION_NAME,
                    cand_id = candidateId,
                    batch_id = batchId,
                    exam_id = examId,
                    question_set_id = questionSetId,
                    Ques_and_ans = submitList as List<SubmitExamItem>
                )

                val gson = Gson()
                Log.d("EXAM_SUBMISSION", gson.toJson(request))

                val response = RetrofitClient.api.submitExam(request)

                if (response.isSuccessful) {
                    _examFinished.value = true
                    _showSuccessDialog.value = true

                    // Schedule background sync with WorkManager
                    scheduleExamSyncWork(candidateId, examId, context)
                } else {
                    _submissionError.value = "Submission Failed: ${response.code()}"
                    Log.e("EXAM_SUBMISSION", "Error: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                _submissionError.value = "Network Error: ${e.message}"
                Log.e("EXAM_SUBMISSION", "Exception: ${e.message}", e)
            } finally {
                _submissionLoading.value = false
            }
        }
    }

    /**
     * Schedule background exam sync work using WorkManager
     */
    private fun scheduleExamSyncWork(
        candidateId: String,
        examId: String,
        context: Context
    ) {
        try {
            val examSyncData = workDataOf(
                "candidateId" to candidateId,
                "candidateId" to candidateId,
                "examId" to examId
            )

            val examSyncWork = OneTimeWorkRequestBuilder<ExamSyncWorker>()
                .setInputData(examSyncData)
                .setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL,
                    15,
                    TimeUnit.MINUTES
                )
                .addTag("exam_sync_$examId")
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                "exam_sync_$examId",
                ExistingWorkPolicy.KEEP,
                examSyncWork
            )

            Log.d("WORKMANAGER", "Scheduled exam sync for candidate: $candidateId, exam: $examId")
        } catch (e: Exception) {
            Log.e("WORKMANAGER", "Error scheduling work: ${e.message}", e)
        }
    }

    // Reset ViewModel
    fun reset() {
        _examStarted.value = false
        _currentIndex.value = 0
        _timeLeft.value = 1800
        _examFinished.value = false
        _showReviewDialog.value = false
        _showSuccessDialog.value = false
        _editMode.value = false
        _submissionLoading.value = false
        _submissionError.value = null
        _answers.value = emptyMap()
        _markedQuestions.value = emptySet()
        _questionStatus.value = emptyMap()
    }
}

