package com.kaushalpanjee.compose.domain.repository



import com.kaushalpanjee.compose.ui.model.NotificationUiModel
import com.kaushalpanjee.core.util.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Created by Rishi Porwal
 */
interface NotificationRepository {
    fun getNotifications(page: Int, size: Int): Flow<Resource<List<NotificationUiModel>>>
    fun updateNotificationStatus(
        notificationId: String,
        candidateId: String?,
        instituteId: String?,
        status: String
    ): Flow<Resource<String>>
}