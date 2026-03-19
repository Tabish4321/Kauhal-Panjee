package com.kaushalpanjee.bhahni




import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import com.kaushalpanjee.KaushalPanjeeApplication
import com.kaushalpanjee.core.util.AppConstant.StaticURL.BHASHINI_AUTH
import com.kaushalpanjee.core.util.AppConstant.StaticURL.BHASHINI_SERVICE_ID
import com.kaushalpanjee.core.util.AppConstant.StaticURL.BHASHINI_URL
import com.kaushalpanjee.core.util.AppUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

////mac commit in use 20/03/2026 kaushal panjee
object BhashiniHelper {
    private val context: Context
        get() = KaushalPanjeeApplication.getContext()

    private val client = OkHttpClient()




    suspend fun translate(text: String): String {

        if (text.isBlank()) return text

        return withContext(Dispatchers.IO) {
            try {
                val body = createRequestBody(text)
                val request = createRequest(body)

                val response = client.newCall(request).execute()

                if (!response.isSuccessful) return@withContext text

                val responseBody = response.body?.string().orEmpty()

                parseResponse(responseBody) ?: text

            } catch (e: Exception) {
                Log.e("BHASHINI_ERROR", e.message.toString())
                text
            }
        }
    }

    // 🔹 Request Body Builder
    private fun createRequestBody(text: String): RequestBody {

        val json = JSONObject().apply {

            put("pipelineTasks", JSONArray().apply {
                put(JSONObject().apply {
                    put("taskType", "translation")
                    put("config", JSONObject().apply {
                        put("language", JSONObject().apply {
                            put("sourceLanguage", "en")
                            put("targetLanguage",  AppUtil.getSavedLanguagePreference(context))
                        })
                        put("serviceId", BHASHINI_SERVICE_ID)
                    })
                })
            })

            put("inputData", JSONObject().apply {
                put("input", JSONArray().apply {
                    put(JSONObject().apply {
                        put("source", text)
                    })
                })
            })
        }

        return json.toString()
            .toRequestBody("application/json".toMediaTypeOrNull())
    }

    // 🔹 Request Builder
    private fun createRequest(body: RequestBody): Request {
        return Request.Builder()
            .url(BHASHINI_URL)
            .post(body)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", BHASHINI_AUTH)
            .build()
    }

    // 🔹 Response Parser
    private fun parseResponse(response: String): String? {
        return try {
            JSONObject(response)
                .optJSONArray("pipelineResponse")
                ?.optJSONObject(0)
                ?.optJSONArray("output")
                ?.optJSONObject(0)
                ?.optString("target")
        } catch (e: Exception) {
            null
        }
    }
}





//code are working here

//object BhashiniHelper {
//
//    private val client = OkHttpClient()
//
//    suspend fun translate(text: String): String {
//
//        if (text.isBlank()) return text
//
//        return withContext(Dispatchers.IO) {
//
//            try {
//
//                val jsonObject = JSONObject().apply {
//
//                    put("pipelineTasks", JSONArray().apply {
//                        put(JSONObject().apply {
//                            put("taskType", "translation")
//                            put("config", JSONObject().apply {
//                                put("language", JSONObject().apply {
//                                    put("sourceLanguage", "en")
//                                    put("targetLanguage", "hi")
//                                })
//                                put("serviceId", "ai4bharat/indictrans-v2-all-gpu--t4")
//                            })
//                        })
//                    })
//
//                    put("inputData", JSONObject().apply {
//                        put("input", JSONArray().apply {
//                            put(JSONObject().apply {
//                                put("source", text)
//                            })
//                        })
//                    })
//                }
//
//                val body = jsonObject.toString()
//                    .toRequestBody("application/json".toMediaTypeOrNull())
//
//                val request = Request.Builder()
//                    .url("https://dhruva-api.bhashini.gov.in/services/inference/pipeline")
//                    .post(body)
//                    .addHeader("Content-Type", "application/json")
//                    .addHeader(
//                        "Authorization",
//                        "5QqK4amQLCINDosdd41kAYmdTKxeQ73js0O8xXtSz_q-T1pmZdKmVZ5ikXOCycLI"
//                    ) // ✅ NO Bearer
//                    .build()
//
//                val response = client.newCall(request).execute()
//
//                val responseBody = response.body?.string().orEmpty()
//
//                Log.d("BHASHINI_RES", responseBody)
//
//                if (!response.isSuccessful) return@withContext text
//
//                val json = JSONObject(responseBody)
//
//                val translated = json
//                    .optJSONArray("pipelineResponse")
//                    ?.optJSONObject(0)
//                    ?.optJSONArray("output")
//                    ?.optJSONObject(0)
//                    ?.optString("target")
//
//                translated ?: text
//
//            } catch (e: Exception) {
//
//                Log.e("BHASHINI_ERROR", e.message.toString())
//                text
//            }
//        }
//    }
//}






