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

        val isLoggedIn = AppUtil.getLoginStatus(applicationContext)
        if (!isLoggedIn) {
            Log.d("FCM_TEST", "User not logged then notification not showing ")
            return
        }

        if (message.notification != null) {
            val title = message.notification?.title ?: "Kaushal Panjee"
            val body = message.notification?.body ?: "You have a new Invitation"

            showNotificationn(title, body)
            return
        }
        if (message.data.isNotEmpty()) {
            val title = message.data["title"] ?: "Kaushal Panjee"
            val body = message.data["body"] ?: "You have a new Invitation"

            showNotificationn(title, body)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d("FCM_TOKEN", token)
    }


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
