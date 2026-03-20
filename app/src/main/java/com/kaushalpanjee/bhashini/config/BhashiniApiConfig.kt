package com.kaushalpanjee.bhashini.config

data class BhashiniApiConfig(
    val url: String,
    val authToken: String,
    val contentType: String = "application/json"
)