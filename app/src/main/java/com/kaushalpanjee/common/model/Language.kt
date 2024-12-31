package com.kaushalpanjee.common.model

data class Language(
    val name: String,
    var isRead: Boolean = false,
    var isWrite: Boolean = false,
    var isSpeak: Boolean = false
)
