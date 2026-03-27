package com.kaushalpanjee

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import androidx.work.WorkManager
import com.kaushalpanjee.CBT.ExamSyncWorker
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class KaushalPanjeeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        
        // Initialize WorkManager with custom configuration
        initializeWorkManager()
    }

    private fun initializeWorkManager() {
        try {
            val workManagerConfig = Configuration.Builder()
                .setMinimumLoggingLevel(Log.INFO)
                .build()

            WorkManager.initialize(this, workManagerConfig)
            Log.d("WorkManager", "WorkManager initialized successfully")
        } catch (e: Exception) {
            Log.e("WorkManager", "Error initializing WorkManager: ${e.message}", e)
        }
    }
}

