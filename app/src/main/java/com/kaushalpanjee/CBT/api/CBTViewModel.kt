package com.example.myapplication.CBT.api

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import com.google.gson.Gson
//import com.kaushalpanjee.CBT.database.AppDatabase
import com.kaushalpanjee.core.data.local.database.AppDatabase
import com.kaushalpanjee.core.util.AppUtil
import java.io.File








//class CBTViewModel : ViewModel() {
//
//    private val _questionList = MutableLiveData<List<Question>>()
//    val questionList: LiveData<List<Question>> = _questionList
//
//    private val _examId = MutableLiveData<String>()
//    val examId: LiveData<String> = _examId
//
//    private val _questionSetId = MutableLiveData<String>()
//    val questionSetId: LiveData<String> = _questionSetId
//
//    private val _batchId = MutableLiveData<String>()
//    val batchId: LiveData<String> = _batchId
//
//    private val _candidateId = MutableLiveData<String>()
//    val candidateId: LiveData<String> = _candidateId
//
//    private val _message = MutableLiveData<String>()
//    val message: LiveData<String> = _message
//
//    fun fetchExam(context: Context, loginId: String) {
//
//        viewModelScope.launch(Dispatchers.IO) {
//
//            try {
//
//                val client = OkHttpClient()
//                val mediaType = "application/json".toMediaType()
//
//                val language = AppUtil.getSavedLanguagePreference(context)
//
//                val body = """
//                {
//                  "candidateId": "$loginId",
//                  "languageId": "$language"
//                }
//                """.trimIndent().toRequestBody(mediaType)
//
//                val request = Request.Builder()
//                    .url("https://kaushal.dord.gov.in/demobackend/ddugkyapp/cbtApi/getQS")
//                    .post(body)
//                    .addHeader("Content-Type", "application/json")
//                    .build()
//
//                val response = client.newCall(request).execute()
//                val responseBody = response.body?.string()
//
//                if (responseBody != null) {
//
//                    val gson = Gson()
//                    val apiResponse =
//                        gson.fromJson(responseBody, ApiResponse::class.java)
//
//                    withContext(Dispatchers.Main) {
//
//                        val qs = apiResponse.questionset
//
//                        _questionList.value = qs.question
//                        _examId.value = qs.exam_id
//                        _questionSetId.value = qs.question_set_id
//                        _batchId.value = qs.batch_id
//                        _candidateId.value = qs.candidate_id
//
//                        _message.value =
//                            "Total Questions: ${qs.question.size}"
//                    }
//                }
//
//            } catch (e: Exception) {
//
//                withContext(Dispatchers.Main) {
//                    _message.value = "Error: ${e.message}"
//                }
//            }
//        }
//    }
//}










//class CBTViewModel : ViewModel() {
//
//    private val _questionList = MutableLiveData<List<Question>>()
//    val questionList: LiveData<List<Question>> = _questionList
//
//    private val _examId = MutableLiveData<String>()
//    val examId: LiveData<String> = _examId
//
//    private val _questionSetId = MutableLiveData<String>()
//    val questionSetId: LiveData<String> = _questionSetId
//
//    private val _batchId = MutableLiveData<String>()
//    val batchId: LiveData<String> = _batchId
//
//    private val _candidateId = MutableLiveData<String>()
//    val candidateId: LiveData<String> = _candidateId
//
//    private val _message = MutableLiveData<String>()
//    val message: LiveData<String> = _message
//
//    private val fileName = "cbt_cache.json"
//
//    fun fetchExam(context: Context, loginId: String) {
//
//        viewModelScope.launch(Dispatchers.IO) {
//
//            try {
//
//                val client = OkHttpClient()
//                val mediaType = "application/json".toMediaType()
//
//                val language = AppUtil.getSavedLanguagePreference(context)
//
//                val body = """
//                {
//                  "candidateId": "$loginId",
//                  "languageId": "$language"
//                }
//                """.trimIndent().toRequestBody(mediaType)
//
//                val request = Request.Builder()
//                    .url("https://kaushal.dord.gov.in/demobackend/ddugkyapp/cbtApi/getQS")
//                    .post(body)
//                    .addHeader("Content-Type", "application/json")
//                    .build()
//
//                val response = client.newCall(request).execute()
//                val responseBody = response.body?.string()
//
//                if (!responseBody.isNullOrEmpty()) {
//
//                    // ✅ SAVE CACHE
//                    saveCache(context, responseBody)
//
//                    // ✅ USE DATA
//                    parseAndSet(responseBody)
//
//                } else {
//                    loadFromCache(context)
//                }
//
//            } catch (e: Exception) {
//
//                // ✅ INTERNET FAIL → LOAD CACHE
//                loadFromCache(context)
//
//                withContext(Dispatchers.Main) {
//                    _message.value = "Offline mode"
//                }
//            }
//        }
//    }
//
//    // ✅ SAVE CACHE
//    private fun saveCache(context: Context, json: String) {
//        try {
//            File(context.filesDir, fileName).writeText(json)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//    // ✅ LOAD CACHE
//    private suspend fun loadFromCache(context: Context) {
//        try {
//            val file = File(context.filesDir, fileName)
//            if (file.exists()) {
//                val json = file.readText()
//                parseAndSet(json)
//            } else {
//                withContext(Dispatchers.Main) {
//                    _message.value = "No offline data"
//                }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//    // ✅ COMMON PARSER
//    private suspend fun parseAndSet(json: String) {
//
//        val apiResponse = Gson().fromJson(json, ApiResponse::class.java)
//        val qs = apiResponse.questionset
//
//        withContext(Dispatchers.Main) {
//            _questionList.value = qs.question
//            _examId.value = qs.exam_id
//            _questionSetId.value = qs.question_set_id
//            _batchId.value = qs.batch_id
//            _candidateId.value = qs.candidate_id
//
//            _message.value = "Total Questions: ${qs.question.size}"
//        }
//    }
//}

















class CBTViewModel : ViewModel() {

    private val _questionList = MutableLiveData<List<Question>>()
    val questionList: LiveData<List<Question>> = _questionList

    private val _examId = MutableLiveData<String>()
    val examId: LiveData<String> = _examId

    private val _questionSetId = MutableLiveData<String>()
    val questionSetId: LiveData<String> = _questionSetId

    private val _batchId = MutableLiveData<String>()
    val batchId: LiveData<String> = _batchId

    private val _candidateId = MutableLiveData<String>()
    val candidateId: LiveData<String> = _candidateId

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    // ✅ FILE NAME
    private val fileName = "questions.json"

    fun fetchExam(context: Context, loginId: String) {

        viewModelScope.launch(Dispatchers.IO) {

            try {

                val client = OkHttpClient()
                val mediaType = "application/json".toMediaType()

                val language = AppUtil.getSavedLanguagePreference(context)

                val body = """
                {
                  "candidateId": "2537374209",
                  "languageId": "$language"
                }
                """.trimIndent().toRequestBody(mediaType)

                val request = Request.Builder()
                    .url("https://kaushal.dord.gov.in/demobackend/ddugkyapp/cbtApi/getQS")
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .build()

                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if (!responseBody.isNullOrEmpty()) {

                    // ✅ 1. SAVE JSON OFFLINE
                    saveJsonToFile(context, responseBody)

                    // ✅ 2. PARSE
                    parseAndSetData(responseBody)

                } else {
                    loadFromOffline(context)
                }

            } catch (e: Exception)
            {

                // ✅ API FAIL → LOAD OFFLINE
                loadFromOffline(context)

                withContext(Dispatchers.Main) {
                    _message.value = "Offline Mode: ${e.message}"
                }
            }
        }
    }

    // ✅ SAVE JSON
    private fun saveJsonToFile(context: Context, json: String) {
        try {
            val file = File(context.filesDir, fileName)
            file.writeText(json)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }



    fun deleteOfflineJson(context: Context) {
        try {
            val file = File(context.filesDir, fileName)
            if (file.exists()) {
                file.delete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // ✅ LOAD OFFLINE JSON
    private suspend fun loadFromOffline(context: Context) {
        try {
            val file = File(context.filesDir, fileName)
            if (file.exists()) {
                val json = file.readText()
                parseAndSetData(json)
            } else {
                withContext(Dispatchers.Main) {
                    _message.value = "No offline data found"
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadOfflineFirst(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            loadFromOffline(context)
        }
    }

    // ✅ COMMON PARSER
    private suspend fun parseAndSetData(json: String) {

        val gson = Gson()
        val apiResponse = gson.fromJson(json, ApiResponse::class.java)
        val qs = apiResponse.questionset

        withContext(Dispatchers.Main) {
            _questionList.value = qs.question
            _examId.value = qs.exam_id
            _questionSetId.value = qs.question_set_id
            _batchId.value = qs.batch_id
            _candidateId.value = qs.candidate_id

            _message.value = "Total Questions: ${qs.question.size}"
        }
    }
}