package com.kaushalpanjee.compose.util

import android.content.Context
import com.kaushalpanjee.core.util.AppUtil

object SessionProvider {

    fun getToken(context: Context): String {
        return AppUtil.getSavedTokenPreference(context)
    }

}
