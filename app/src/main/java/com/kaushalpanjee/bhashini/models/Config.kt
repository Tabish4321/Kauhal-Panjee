package com.kaushalpanjee.bhashini.models

import com.kaushalpanjee.core.util.AppConstant.StaticURL.BHASHINI_SERVICE_ID

data class Config(
    val language: Language,
    val serviceId: String = BHASHINI_SERVICE_ID
)
