package com.kaushalpanjee.bhashini.helper

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.kaushalpanjee.KaushalPanjeeApplication
import com.kaushalpanjee.bhashini.Reponse.TranslationResponse
import com.kaushalpanjee.bhashini.api.BhashiniApi
import com.kaushalpanjee.bhashini.models.Config
import com.kaushalpanjee.bhashini.models.InputData
import com.kaushalpanjee.bhashini.models.InputItem
import com.kaushalpanjee.bhashini.models.Language
import com.kaushalpanjee.bhashini.models.PipelineTask
import com.kaushalpanjee.bhashini.models.TranslationRequest
import com.kaushalpanjee.core.util.AppConstant
import com.kaushalpanjee.core.util.AppUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * Created by Ajit Ranjan
 */

object BhashiniHelper {

    private val context: Context
        get() = KaushalPanjeeApplication.Companion.getContext()

    suspend fun translate(text: String): String {

        if (text.isBlank()) return text

        return withContext(Dispatchers.IO) {
            try {

                val json = Gson().toJson(buildRequestModel(text))

                val body = json.toRequestBody("application/json".toMediaTypeOrNull())

                val (isSuccess, response) = BhashiniApi.post(
                    url = AppConstant.StaticURL.BHASHINI_URL,
                    body = body,
                    headers = mapOf(
                        "Content-Type" to "application/json",
                        "Authorization" to AppConstant.StaticURL.BHASHINI_AUTH
                    )
                )

                if (!isSuccess) return@withContext text

                parseResponse(response) ?: text

            } catch (e: Exception) {
                Log.e("BHASHINI_HELPER", "❌ ${e.message}")
                text
            }
        }
    }

    // 🔹 Model Builder
    private fun buildRequestModel(text: String) = TranslationRequest(
        pipelineTasks = listOf(
            PipelineTask(
                config = Config(
                    language = Language(
                        targetLanguage = AppUtil.getSavedLanguagePreference(context)
                    )
                )
            )
        ),
        inputData = InputData(
            input = listOf(InputItem(source = text))
        )
    )

    // 🔹 Response Parser
    private fun parseResponse(response: String): String? {
        return try {
            Gson().fromJson(response, TranslationResponse::class.java)
                .pipelineResponse
                ?.firstOrNull()
                ?.output
                ?.firstOrNull()
                ?.target
        } catch (e: Exception) {
            null
        }
    }
}