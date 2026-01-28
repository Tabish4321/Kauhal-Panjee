package com.kaushalpanjee.notification.with_api

/**
 * Created by Rishi Porwal
 */
data class PushNotificationRequest(
    val title: String?,
    val body: String?,
    val type: String?,
    val scheme: String?,
    val token: String?,
    val loginId: String?,
    val instituteTrade: String?,
    val instituteId: Int?,
    val centerId: Int?,
    val centerTrade: String?,
    val entityCode: String?
)
