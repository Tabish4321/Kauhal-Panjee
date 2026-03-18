package com.example.myapplication.CBT.api

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
import com.kaushalpanjee.core.util.AppUtil
import com.kaushalpanjee.core.util.UserPreferences
import javax.inject.Inject





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

    fun fetchExam(context: Context, loginId: String) {

        viewModelScope.launch(Dispatchers.IO) {

            try {

                val client = OkHttpClient()
                val mediaType = "application/json".toMediaType()

                val language = AppUtil.getSavedLanguagePreference(context)

                val body = """
                {
                  "candidateId": "$loginId",
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

                if (responseBody != null) {

                    val gson = Gson()
                    val apiResponse =
                        gson.fromJson(responseBody, ApiResponse::class.java)

                    withContext(Dispatchers.Main) {

                        val qs = apiResponse.questionset

                        _questionList.value = qs.question
                        _examId.value = qs.exam_id
                        _questionSetId.value = qs.question_set_id
                        _batchId.value = qs.batch_id
                        _candidateId.value = qs.candidate_id

                        _message.value =
                            "Total Questions: ${qs.question.size}"
                    }
                }

            } catch (e: Exception) {

                withContext(Dispatchers.Main) {
                    _message.value = "Error: ${e.message}"
                }
            }
        }
    }
}

//class CBTViewModel : ViewModel() {
//
//    private val _questionList = MutableLiveData<List<Question>>()
//    val questionList: LiveData<List<Question>> = _questionList
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
//                // preference language
//                val language = AppUtil.getSavedLanguagePreference(context)
//                val body = """
//                    {
//
//                        "candidateId": "$loginId",
//                        "languageId": "$language"
//                    }
//                """.trimIndent().toRequestBody(mediaType)
//                val gson = Gson()
//
//                val jsonString = gson.toJson(body)
//
//                Log.d("JSON_DATA", jsonString)
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
//                        _questionList.value =
//                            apiResponse.questionset.question
//
//                        _message.value =
//                            "Total Questions: ${apiResponse.questionset.question.size}"
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
