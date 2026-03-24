package com.kaushalpanjee.bhashini.api

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody


//Use bhashin app ajit ranjan 24/03/2026 time 18:07PM
object BhashiniApi {

    private val client = OkHttpClient()

    fun post(
        url: String,
        body: RequestBody,
        headers: Map<String, String>
    ): Pair<Boolean, String> {

        return try {

            val request = Request.Builder().apply {
                url(url)
                post(body)
                headers.forEach { (k, v) -> addHeader(k, v) }
            }.build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string().orEmpty()

            if (response.isSuccessful) {
                Log.d("BHASHINI_API", "✅ Success")
                true to responseBody
            } else {
                Log.e("BHASHINI_API", "❌ Failed: ${response.code}")
                false to responseBody
            }

        } catch (e: Exception) {
            Log.e("BHASHINI_API", "❌ Exception: ${e.message}")
            false to ""
        }
    }
}