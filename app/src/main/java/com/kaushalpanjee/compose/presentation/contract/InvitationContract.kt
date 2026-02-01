package com.kaushalpanjee.compose.presentation.contract

import com.kaushalpanjee.compose.ui.model.NotificationUiModel
import com.kaushalpanjee.core.util.Resource
/**
 * Created by Rishi Porwal
 */
sealed interface InvitationContract {
    data class State(
        val notifications: Resource<List<NotificationUiModel>> = Resource.Loading(),
        val actionLoadingIds: Set<String> = emptySet(),
        val isLoadingMore: Boolean = false,
        val isRefreshing: Boolean = false,
        val currentPage: Int = 0,
        val isLastPage: Boolean = false
    ) {
        val isLoading: Boolean
            get() = notifications is Resource.Loading && !isLoadingMore && !isRefreshing
    }

    sealed class Event {
        object LoadNotifications : Event()
        object LoadMoreNotifications : Event()
        object RefreshNotifications : Event()
        data class UpdateNotificationStatus(
            val notificationId: String,
            val status: String
        ) : Event()
    }

    sealed class SideEffect {
        data class ShowToast(val message: String) : SideEffect()
        data class ShowError(val message: String) : SideEffect()
    }
}