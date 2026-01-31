package com.kaushalpanjee.compose.data.remote.response

/**
 * Created by Rishi Porwal
 */
data class NotificationListResponse(
    val content: List<UserNotification>?,
    val totalElements: Int?,
    val totalPages: Int?,
    val currentPage: Int?
)