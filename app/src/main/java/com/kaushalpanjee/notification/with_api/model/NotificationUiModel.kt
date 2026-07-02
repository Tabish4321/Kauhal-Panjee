package com.kaushalpanjee.notification.with_api.model

import com.kaushalpanjee.notification.with_api.NotificationStatus
import com.kaushalpanjee.notification.with_api.model.res.UserNotification

/**
 * Created by Rishi Porwal
 */
data class NotificationUiModel(
    val id: String,
    val title: String,
    val message: String,
    val createdAt: String,
    val invitationStatus: String,   // P / A / R
    val instituteTrade: String?,
    val instituteId: String?,       // backend sends String
    val entityCode: String?,
    val candidateId: String?
)


fun UserNotification.toUiModel(): NotificationUiModel =
    NotificationUiModel(
        id = id?.toString().orEmpty(),
        title = title.orEmpty(),
        message = body.orEmpty(),
        createdAt = createdOn.orEmpty(),
        invitationStatus = invitationFlag.orEmpty(), // P / A / R
        instituteTrade = instituteTrade,
        instituteId = instituteId,
        entityCode = entityCode,
        candidateId = candidateId
    )


fun String.toInvitationText(): String =
    when (this) {
        "A" -> "Accepted"
        "R" -> "Rejected"
        "P" -> "Pending"
        else -> "Pending"
    }
