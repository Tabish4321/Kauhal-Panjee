package com.kaushalpanjee

import android.app.Application
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.kaushalpanjee.core.util.AppUtil
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class KaushalPanjeeApplication  : Application() {

    companion object {
        private const val TAG = "KaushalPanjeeApp"
    }

    override fun onCreate() {
        super.onCreate()

        // Initialize app components
        initializeApp()

        Log.d(TAG, "🔥 KaushalPanjeeApplication created")
    }

    private fun initializeApp() {
        // Initialize any app-wide components here
        // Like analytics, crash reporting, etc.

        Log.d(TAG, "🔥 App components initialized")
    }

    override fun onTerminate() {
        super.onTerminate()
        Log.d(TAG, "🔥 App terminated")
    }
}