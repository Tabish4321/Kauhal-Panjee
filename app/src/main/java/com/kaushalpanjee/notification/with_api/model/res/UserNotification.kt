package com.kaushalpanjee.notification.with_api.model.res

/**
 * Created by Rishi Porwal
 */
data class UserNotification(
    val id: Int?,
    val title: String?,
    val body: String?,
    val type: String?,
    val status: String?,
    val scheme: String?,
    val instituteTrade: String?,
    val instituteId: Int?,
    val centerId: Int?,
    val centerTrade: String?,
    val entityCode: String?,
    val candidateId: String?,
    val invitationStatus: String?,
    val createdAt: String?
)