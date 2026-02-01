package com.kaushalpanjee.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kaushalpanjee.R
import com.kaushalpanjee.common.CommonActivity
import com.kaushalpanjee.core.util.AppUtil

class KPFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "FCM_SERVICE"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "Message received from: ${remoteMessage.from}")
        Log.d(TAG, "Data: ${remoteMessage.data}")
        Log.d(TAG, "Notification: ${remoteMessage.notification}")

        // Get notification data
        val title = remoteMessage.notification?.title ?: remoteMessage.data["title"] ?: "Notification"
        val body = remoteMessage.notification?.body ?: remoteMessage.data["body"] ?: ""
        val notificationId = (System.currentTimeMillis() % 10000).toInt()

        // Store in SharedPreferences that a notification was received
        // This works even when app is closed!
        AppUtil.setNotificationClicked(this, true)
        //AppUtil.saveNotificationData(this, remoteMessage.data)

        // Show notification
        showNotification(title, body, notificationId, remoteMessage.data)
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "New token: $token")
      //  AppUtil.saveFCMToken(this, token)
    }

    private fun showNotification(
        title: String,
        body: String,
        notificationId: Int,
        data: Map<String, String>
    ) {
        try {
            // 1. Create channel
            createNotificationChannel()

            // 2. Create intent - CRITICAL: Add ALL data to intent
            val intent = Intent(this, CommonActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

                // 🔥 CRITICAL: Add a custom action so we can identify notification click
                action = "NOTIFICATION_CLICK"

                // 🔥 Add ALL FCM data to intent
                data.forEach { (key, value) ->
                    putExtra(key, value)
                }

                // Add identification extras
                putExtra("from_notification", true)
                putExtra("notification_id", notificationId)
                putExtra("title", title)
                putExtra("body", body)
                putExtra("fcm_data", data.toString())

                // Add timestamp
                putExtra("timestamp", System.currentTimeMillis())
            }

            // 3. Create pending intent
            val pendingIntentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }

            val pendingIntent = PendingIntent.getActivity(
                this,
                notificationId, // Different request code for each notification
                intent,
                pendingIntentFlags
            )

            // 4. Build notification
            val notification = NotificationCompat.Builder(this, "default_channel")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setDefaults(NotificationCompat.DEFAULT_ALL) // Sound, vibration, etc.
                .build()

            // 5. Show notification
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(notificationId, notification)

            Log.d(TAG, "Notification shown: $title")

        } catch (e: Exception) {
            Log.e(TAG, "Error showing notification", e)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "default_channel",
                "Default Channel",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Default notifications"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 250, 500)
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}