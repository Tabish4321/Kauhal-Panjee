package com.kaushalpanjee.compose.domain.usecase

import com.kaushalpanjee.compose.domain.repository.NotificationRepository
import com.kaushalpanjee.core.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
/**
 * Created by Rishi Porwal
 */
class UpdateNotificationStatusUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke(
        notificationId: String,
        candidateId: String?,
        instituteId: String?,
        status: String
    ): Flow<Resource<String>> {
        return repository.updateNotificationStatus(
            notificationId = notificationId,
            candidateId = candidateId,
            instituteId = instituteId,
            status = status
        )
    }
}