package com.kaushalpanjee.compose.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

/**
 * Created by Rishi Porwal
 */

// Extension on Context
fun Context.getStringResource(@StringRes resId: Int): String {
    return this.getString(resId)
}

// For formatting
fun Context.getStringResource(@StringRes resId: Int, vararg args: Any): String {
    return this.getString(resId, *args)
}

fun Context.findActivity(): Activity? {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is Activity) return ctx
        ctx = ctx.baseContext
    }
    return null
}
