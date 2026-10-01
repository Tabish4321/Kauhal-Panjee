package com.kaushalpanjee

import android.app.Application
import android.util.Log
import com.d2k.samiksha.SamikshaConfig
import com.d2k.samiksha.SamikshaSdk
import com.d2k.samiksha.security.PinningConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class KaushalPanjeeApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        SamikshaSdk.initialize(
            config = SamikshaConfig(
                baseUrl = BuildConfig.SAMIKSHA_BASE_URL,
                apiKey = BuildConfig.SAMIKSHA_API_KEY,
                pinning = PinningConfig.Pinned(
                    primary = BuildConfig.SAMIKSHA_PIN_PRIMARY,
                    backup = BuildConfig.SAMIKSHA_PIN_BACKUP
                        .takeIf { it.isNotBlank() }
                )
            ),
            onReady = { warnings ->
                warnings.forEach {
                    Log.w("Samiksha", it)
                }

                Log.d("Samiksha", "SDK initialized successfully")
            },
            onFailure = { error ->
                Log.e("Samiksha", "SDK initialization failed: ${error.message}")
            }
        )



    }
}