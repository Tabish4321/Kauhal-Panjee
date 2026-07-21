package com.kaushalpanjee.common.model.response

import com.google.gson.annotations.SerializedName

data class SubmitExamResponse(
    @SerializedName("responseCode")
    val responseCode: Int? = null,

    @SerializedName("message")
    val message: String? = null
)
