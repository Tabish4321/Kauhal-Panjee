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
import com.google.firebase.messaging.R
import com.google.firebase.messaging.RemoteMessage
import com.kaushalpanjee.WelcomeActivity
import com.kaushalpanjee.common.CommonActivity

/**
 * Created by Rishi Porwal
 */
class KPFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("FCM_TEST", "Message received: ${message}")

        val title = message.notification?.title ?: "Notification"
        val body = message.notification?.body ?: "You have a new message"

        showNotification(title, body)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d("FCM_TOKEN", token)
    }

    private fun showNotification(title: String, body: String) {

        val channelId = "default_channel"
        val intent = Intent(this, CommonActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("OPEN_NOTIFICATION_LIST", true)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "General Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.common_google_signin_btn_icon_disabled)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        manager.notify(System.currentTimeMillis().toInt(), notification)
    }

//    private fun showNotification(title: String, body: String) {
//        val channelId = "default_channel"
//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        // Android 8+
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                channelId,
//                "General Notifications",
//                NotificationManager.IMPORTANCE_HIGH
//            )
//            notificationManager.createNotificationChannel(channel)
//        }
//
//        val notification = NotificationCompat.Builder(this, channelId)
//            .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark_normal_background)
//            .setContentTitle(title)
//            .setContentText(body)
//            .setAutoCancel(true)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .build()
//
//        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
//    }
}
