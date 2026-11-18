package com.kaushalpanjee.core.util

import android.os.Build
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kaushalpanjee.BuildConfig

object FirebaseCrashManager {

    private val crashlytics: FirebaseCrashlytics
        get() = FirebaseCrashlytics.getInstance()

    // App start par call karein
    fun initialize() {

        crashlytics.setCustomKey("device_manufacturer", Build.MANUFACTURER)
        crashlytics.setCustomKey("device_model", Build.MODEL)
        crashlytics.setCustomKey("android_version", Build.VERSION.RELEASE ?: "Unknown")
        crashlytics.setCustomKey("android_sdk", Build.VERSION.SDK_INT)
        crashlytics.setCustomKey("app_version", BuildConfig.VERSION_NAME)
        crashlytics.setCustomKey("app_version_code", BuildConfig.VERSION_CODE)
        crashlytics.setCustomKey("app_platform", "Android")

        crashlytics.log("Kaushal Panjee application started")
    }

    // Current screen track karne ke liye
    fun setCurrentScreen(screenName: String) {
        crashlytics.setCustomKey("current_screen", screenName)
        crashlytics.log("Screen opened: $screenName")
    }

    // User action track karne ke liye
    fun log(message: String) {
        crashlytics.log(message)
    }

    // Non-fatal exception track karne ke liye
    fun recordException(
        exception: Throwable,
        screen: String,
        message: String
    ) {
        crashlytics.setCustomKey("error_screen", screen)
        crashlytics.log(message)
        crashlytics.recordException(exception)
    }

    // Login ke baad anonymous/internal user ID set karein
    fun setUserId(userId: String) {
        crashlytics.setUserId(userId)
    }

    // Logout par user ID clear karein
    fun clearUserId() {
        crashlytics.setUserId("")
    }
}
