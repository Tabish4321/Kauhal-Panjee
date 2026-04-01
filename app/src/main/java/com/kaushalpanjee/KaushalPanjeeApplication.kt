package com.kaushalpanjee

import android.app.Application
import android.util.Log
import com.d2k.samiksha.SamikshaSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class KaushalPanjeeApplication  : Application(){


    override fun onCreate() {
        super.onCreate()

        SamikshaSdk.init(
            context = this,
            baseUrl = "https://samikshaapi.d2kindia.com/",
            apiKey = "624f2281-b0f1-44e3-9d3e-24826a53e7a6",
            calledFrom = "com.kaushalpanjee",
            apiVersion = "1",

            onSuccess = {
                Log.d("SDK", "Init Success")
            },

            onFailure = { error ->
                Log.e("SDK", "Init Failed: $error")
            }
        )
    }


}