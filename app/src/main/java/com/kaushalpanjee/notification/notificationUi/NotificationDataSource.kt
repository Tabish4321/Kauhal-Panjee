package com.kaushalpanjee.notification.notificationUi

/**
 * Created by Rishi Porwal
 */
interface NotificationDataSource {
    fun getNotifications(): List<NotificationUiModel>

}