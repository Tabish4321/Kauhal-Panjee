package com.kaushalpanjee

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class KaushalPanjeeApplication  : Application(){

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        @Volatile
        private lateinit var instance: KaushalPanjeeApplication

        fun getContext(): Context {
            return instance.applicationContext
        }
    }
}