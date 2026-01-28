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


/**
 * Created by Rishi Porwal
 */
class KPFirebaseMessagingService : FirebaseMessagingService() {

    val type ="OPEN_NOTIFICATION_LIST"
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
//        val isLoggedIn = AppUtil.getLoginStatus(applicationContext)
//        if (!isLoggedIn) {
//            Log.d("FCM_TEST", "User not logged then notification not showing ")
//            return
//        }

        Log.d("FCM_TEST", "Message received: ${message}")
        Log.d("FCM_TEST", "Custome received: ${message.data}")

            //val type = message.data["type"]
        val title = message.notification?.title ?: message.data["title"] ?: "Notification"
        val body = message.notification?.body ?: message.data["body"] ?: "You have a new message"
        //val type = message.data["type"] ?: ""

        if (type == "OPEN_NOTIFICATION_LIST") {
            showNotificationn(title, body)
            }


//        if (message.data.isNotEmpty()) {
//            //val type = message.data["type"]
//            val title = message.data["title"] ?: "Notification"
//            val body = message.data["body"] ?: "You have a new message"
//            if (type == "OPEN_NOTIFICATION_LIST") {
//                showNotification(title, body)
//            }
//        }
    }


//    {
//        "to": "<FCM_TOKEN>",
//        "data": {
//        "type": "OPEN_NOTIFICATION_LIST",
//        "title": "Kaushal Panjee",
//        "body": "This Notification Testing Purpose"
//    }
//    }


    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d("FCM_TOKEN", token)
    }

//    private fun showNotification(title: String, body: String) {
//
//        val channelId = "default_channel"
//
//        // Save flag in SharedPreferences
//        val prefs = getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)
//        prefs.edit().putBoolean("SHOULD_OPEN_NOTIFICATION", true).apply()
//
//        val intent = Intent(this, CommonActivity::class.java).apply {
//         //   flags =Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
//                    Intent.FLAG_ACTIVITY_CLEAR_TASK
//            putExtra("OPEN_NOTIFICATION_LIST", true)
//            action = System.currentTimeMillis().toString()
//
//        }
//
//        val pendingIntent = PendingIntent.getActivity(
//            this,
//            System.currentTimeMillis().toInt(),
//             intent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        ) //PendingIntent.FLAG_ONE_SHOT
//
//        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                channelId,
//                "General Notifications",
//                NotificationManager.IMPORTANCE_HIGH
//            )
//            manager.createNotificationChannel(channel)
//        }
//
//        val notification = NotificationCompat.Builder(this, channelId)
//            .setSmallIcon(R.drawable.ic_notification)
//            .setContentTitle(title)
//            .setContentText(body)
//            .setAutoCancel(true)
//            .setContentIntent(pendingIntent)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .build()
//
//        manager.notify(System.currentTimeMillis().toInt(), notification)
//    }


    private fun showNotificationn(title: String, body: String) {

        val channelId = "default_channel"

        val intent = Intent(this, CommonActivity::class.java).apply {
            //flags =Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("OPEN_NOTIFICATION_LIST", true)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    "General Notifications",
                    NotificationManager.IMPORTANCE_HIGH
                )
            )
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        manager.notify(1001, notification)
    }

}
