package com.kaushalpanjee.notification.notificationUi

/**
 * Created by Rishi Porwal
 */
data class NotificationUiModel(
    val id: String,
    val title: String,
    val message: String,
    val createdAt: Long,
    var status: NotificationStatus = NotificationStatus.PENDING,
    val isApproved: Boolean=true
)

