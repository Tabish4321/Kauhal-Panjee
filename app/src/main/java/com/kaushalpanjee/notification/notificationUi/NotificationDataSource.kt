package com.kaushalpanjee.notification.notificationUi

import com.kaushalpanjee.core.util.Resource
import com.kaushalpanjee.notification.with_api.model.NotificationUiModel
import kotlinx.coroutines.flow.Flow

/**
 * Created by Rishi Porwal
 */
interface NotificationDataSource {
    fun getNotifications(): List<NotificationUiModel>

   // fun getNotifications(): Flow<Resource<List<NotificationUiModel>>>

}