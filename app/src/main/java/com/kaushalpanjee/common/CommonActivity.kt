package com.kaushalpanjee.common

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import com.kaushalpanjee.R
import com.kaushalpanjee.core.basecomponent.BaseActivity
import com.kaushalpanjee.core.util.AppUtil
import com.kaushalpanjee.databinding.ActivityCommonBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommonActivity : BaseActivity<ActivityCommonBinding>(ActivityCommonBinding::inflate) {

    companion object {
        private const val TAG = "CommonActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Request permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                101
            )
        }

        // 🔥 CRITICAL: Log all intent extras
        logIntentExtras("onCreate")

        // Check if launched from notification
        checkNotificationIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        // 🔥 CRITICAL: Log all intent extras
        logIntentExtras("onNewIntent")

        // Check if launched from notification (app was already running)
        checkNotificationIntent(intent)    }


    private fun logIntentExtras(source: String) {
        Log.d(TAG, "🔍 $source - Intent extras:")
        intent?.extras?.keySet()?.forEach { key ->
            val value = intent.extras?.get(key)
            Log.d(TAG, "   $key = $value (${value?.javaClass?.simpleName})")
        }

        // Also log action and data
        Log.d(TAG, "🔍 Action: ${intent?.action}")
        Log.d(TAG, "🔍 Data: ${intent?.data}")
        Log.d(TAG, "🔍 Flags: ${intent?.flags}")
    }

    private fun checkNotificationIntent(intent: Intent?) {
        if (intent == null) return

        // 🔥 MULTIPLE WAYS to detect notification click:

        // 1. Check our custom extra
        val fromExtra = intent.getBooleanExtra("from_notification", false)

        // 2. Check for FCM data in extras
        val hasFcmData = intent.extras?.containsKey("google.message_id") == true ||
                intent.extras?.containsKey("from") == true ||
                intent.extras?.containsKey("gcm.n.analytics_data") == true

        // 3. Check our custom action
        val fromAction = intent.action == "NOTIFICATION_CLICK"

        // 4. Check if app was launched by notification click (system flag)
        //val launchedFromHistory = (intent.flags and Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) != 0

        Log.d(TAG, "📱 Notification check:")
        Log.d(TAG, "   fromExtra: $fromExtra")
        Log.d(TAG, "   hasFcmData: $hasFcmData")
        Log.d(TAG, "   fromAction: $fromAction")
     //   Log.d(TAG, "   launchedFromHistory: $launchedFromHistory")

        // If ANY of these are true, it's from notification
        val fromNotification = fromExtra || hasFcmData || fromAction

        if (fromNotification) {
            Log.d(TAG, "✅ App launched from notification!")

            // Save to SharedPreferences (works in all states)
            AppUtil.setNotificationClicked(this, true)

            // Also save notification data
            val notificationData = mutableMapOf<String, String>()
            intent.extras?.keySet()?.forEach { key ->
                val value = intent.getStringExtra(key)
                if (value != null) {
                    notificationData[key] = value
                }
            }
            AppUtil.saveNotificationData(this, notificationData)

            Log.d(TAG, "✅ Notification data saved to SharedPreferences")
        } else {
            Log.d(TAG, "❌ Not from notification")
        }
    }
}


//if (AppUtil.wasNotificationClicked()) {
//    navigateToNotification()
//    AppUtil.clearNotificationFlag()
//}