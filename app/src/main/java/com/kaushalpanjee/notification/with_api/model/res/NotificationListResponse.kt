package com.kaushalpanjee.notification.with_api.model.res

/**
 * Created by Rishi Porwal
 */

data class NotificationListResponse(
    val content: List<UserNotification>?,
    val totalElements: Int?,
    val totalPages: Int?,
    val currentPage: Int?
)

