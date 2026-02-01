package com.kaushalpanjee.compose.firebaseService

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.Intent.getIntent
import android.os.Build
import android.os.Bundle
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

        val isLoggedIn = AppUtil.getLoginStatus(applicationContext)
        if (!isLoggedIn) {
            Log.d("FCM_TEST", "User not logged then notification not showing ")
            return
        }

        if (message.data.isEmpty()){
            val title =  "Notification"
            val body = "You have a new message"
            showNotification(
                title = title,
                body = body
            )
            return
        }
        val data = message.data
        val title = data["title"] ?: "Notification"
        val body = data["body"] ?: "You have a new message"
        val type = data["type"]

        // Optional: extract extra fields if needed later
        val scheme = data["scheme"]
        val instituteTrade = data["instituteTrade"]
        val entityCode = data["entityCode"]

        showNotification(
            title = title,
            body = body
        )

//        when (type) {
//            "INVITATION" -> {
//                showNotificationn(
//                    title = title,
//                    body = body
//                )
//            }
//            else -> {
//                showNotificationn(
//                    title = title,
//                    body = body
//                )
//            }
//        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d("FCM_TOKEN", token)
    }

    private fun showNotification(title: String, body: String) {

        val intent = Intent(this, CommonActivity::class.java)

        // ✅ Put everything in Bundle
        val bundle = Bundle().apply {
            putBoolean("OPEN_NOTIFICATION_LIST", true)
            putString("notification_type", "INVITATION")
        }

        intent.putExtras(bundle)

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TOP
//            Intent.FLAG_ACTIVITY_NEW_TASK or
//                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
//                    Intent.FLAG_ACTIVITY_CLEAR_TASK

        val pendingIntent = PendingIntent.getActivity(
            this,
            0, // constant request code
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        Log.d("FCM_DEBUG", "Sending bundle OPEN_NOTIFICATION_LIST=true")

        val channelId = "default_channel"
        val manager = getSystemService(NotificationManager::class.java)

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





//    private fun showNotification(title: String, body: String) {
//
//        val channelId = "default_channel"
//
//        AppUtil.saveNotificationStatus(applicationContext,true)
//
////        val bundle: Bundle? = getIntent().getExtras()
////        if (bundle != null) {
////            //bundle must contain all info sent in "data" field of the notification
////        }
//        val intent = Intent(this, CommonActivity::class.java).apply {
//            flags =Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//           // flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            putExtra("OPEN_NOTIFICATION_LIST", true)
//        }
//
//        val pendingIntent = PendingIntent.getActivity(
//            this,
//            0,
//            intent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            manager.createNotificationChannel(
//                NotificationChannel(
//                    channelId,
//                    "General Notifications",
//                    NotificationManager.IMPORTANCE_HIGH
//                )
//            )
//        }
//
//        val notification = NotificationCompat.Builder(this, channelId)
//            .setSmallIcon(R.drawable.ic_notification)
//            .setContentTitle(title)
//            .setContentText(body)
//            .setAutoCancel(true)
//            .setContentIntent(pendingIntent)
//            .build()
//
//        manager.notify(1001, notification)
//    }

}