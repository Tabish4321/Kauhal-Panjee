package com.kaushalpanjee.notification.with_api.model

import com.kaushalpanjee.bhashini.helper.BhashiniHelper
import com.kaushalpanjee.notification.with_api.model.res.UserNotification
import kotlinx.coroutines.runBlocking

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
/**
 * Created by Ajit Ranajan
 */

fun UserNotification.toUiModel(): NotificationUiModel {
//            Ajit Ranjan remove click to Accept/Reject.
    val cleanBody = body.orEmpty()
        .replace("\n click to Accept/Reject.", "", true)
        .trim()


//    Ajit Ranjan add Bhashini
    val translatedTitle = runBlocking {
        BhashiniHelper.translate(title.orEmpty())
    }
//    Ajit Ranjan add Bhashini
    val translatedMessage = runBlocking {
        BhashiniHelper.translate(cleanBody)
    }

    return NotificationUiModel(
        id = id?.toString().orEmpty(),
        title = translatedTitle,
        message = translatedMessage,
        createdAt = createdOn.orEmpty(),
        invitationStatus = invitationFlag.orEmpty(),
        instituteTrade = instituteTrade,
        instituteId = instituteId,
        entityCode = entityCode,
        candidateId = candidateId
    )
}


fun String.toInvitationText(): String =
    when (this) {
        "A" -> "Accepted"
        "R" -> "Rejected"
        "P" -> "Pending"
        else -> "Pending"
    }