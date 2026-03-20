package com.kaushalpanjee.bhashini.config


import com.kaushalpanjee.core.util.AppConstant.StaticURL.BHASHINI_AUTH
import com.kaushalpanjee.core.util.AppConstant.StaticURL.BHASHINI_URL

fun getApiConfig(): BhashiniApiConfig {
    return BhashiniApiConfig(
        url = BHASHINI_URL,
        authToken = BHASHINI_AUTH
    )
}