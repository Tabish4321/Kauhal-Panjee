package com.kaushalpanjee.compose.data.mapper

import com.kaushalpanjee.compose.data.remote.response.UserNotification
import com.kaushalpanjee.compose.ui.model.NotificationUiModel

/**
 * Created by Rishi Porwal
 */

fun UserNotification.toUiModel(): NotificationUiModel =
    NotificationUiModel(
        id = id?.toString().orEmpty(),
        title = title.orEmpty(),
        message = body.orEmpty(),
        createdAt = createdOn.orEmpty(),
        invitationStatus = invitationFlag.orEmpty(),
        instituteTrade = instituteTrade,
        instituteId = instituteId,
        entityCode = entityCode,
        candidateId = candidateId
    )