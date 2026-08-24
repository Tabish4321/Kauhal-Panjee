package com.kaushalpanjee.core.util

import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

object MultipartUtil {

    private val TEXT_MEDIA_TYPE =
        "text/plain".toMediaType()

    /**
     * Any object -> JSON -> RequestBody
     */
    fun <T> createDataPart(
        data: T
    ): RequestBody {

        val json = Gson().toJson(data)

        return json.toRequestBody(
            TEXT_MEDIA_TYPE
        )
    }

    fun createFilePart(
        name: String,
        file: File?
    ): MultipartBody.Part? {

        if (file == null || !file.exists()) {
            return null
        }

        val requestBody =
            file.asRequestBody(
                "application/octet-stream".toMediaType()
            )

        return MultipartBody.Part.createFormData(
            name,
            file.name,
            requestBody
        )
    }
}