package com.utilize.core.util

import com.google.gson.Gson

object GsonUtils {
    fun <T> parseJson(json: String, tClass: Class<T>): T {
        return Gson().fromJson(json, tClass)
    }
}