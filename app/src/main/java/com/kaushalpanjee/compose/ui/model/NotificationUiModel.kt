package com.kaushalpanjee.compose.ui.model

/**
 * Created by Rishi Porwal
 */
data class NotificationUiModel(
    val id: String,
    val title: String,
    val message: String,
    val createdAt: String,
    val invitationStatus: String,
    val instituteTrade: String?,
    val instituteId: String?,
    val entityCode: String?,
    val candidateId: String?
) {
    val isPending: Boolean
        get() = invitationStatus == "P"

    val isApproved: Boolean
        get() = invitationStatus == "A" || invitationStatus == "APPROVED"

    val isRejected: Boolean
        get() = invitationStatus == "R" || invitationStatus == "REJECTED"
}