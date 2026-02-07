package com.kaushalpanjee.compose.domain.usecase



import com.kaushalpanjee.compose.domain.repository.NotificationRepository
import com.kaushalpanjee.compose.ui.model.NotificationUiModel
import com.kaushalpanjee.core.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
/**
 * Created by Rishi Porwal
 */
class GetNotificationsUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke(page: Int, size: Int): Flow<Resource<List<NotificationUiModel>>> {
        return repository.getNotifications(page, size)
    }
}
