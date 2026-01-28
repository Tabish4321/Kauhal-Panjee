package com.kaushalpanjee.notification.with_api.model

import com.kaushalpanjee.notification.notificationUi.NotificationStatus
import com.kaushalpanjee.notification.with_api.model.res.UserNotification

/**
 * Created by Rishi Porwal
 */
data class NotificationUiModel(
    val id: String,
    val title: String,
    val message: String,
    val createdAt: String,
    val invitationStatus: NotificationStatus,
)

fun UserNotification.toUiModel(): NotificationUiModel =
    NotificationUiModel(
        id = id.toString(),
        title = title.orEmpty(),
        message = body.orEmpty(),
        invitationStatus = NotificationStatus.from(invitationStatus),
        createdAt = createdAt.orEmpty()
    )
