package com.kaushalpanjee.compose.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kaushalpanjee.compose.presentation.contract.NotificationContract
import com.kaushalpanjee.compose.ui.commonComponent.ErrorView
import com.kaushalpanjee.compose.ui.commonComponent.LoadingView
import com.kaushalpanjee.core.util.Resource

/**
 * Created by Rishi Porwal
 */
@Composable
fun NotificationContent(
    modifier: Modifier = Modifier,
    state: NotificationContract.State,
    onLoadMore: () -> Unit,
    onApprove: (String) -> Unit,
    onDisapprove: (String) -> Unit
) {
    when (val notifications = state.notifications) {
        is Resource.Loading -> LoadingView()

        is Resource.Error -> ErrorView(
            message = notifications.error!!.message ?: "Failed to load notifications"
        )

        is Resource.Success -> {
            notifications.data?.let {
                NotificationList(
                    modifier = modifier,
                    notifications = it,
                    actionLoadingIds = state.actionLoadingIds,
                    isLoadingMore = state.isLoadingMore,
                    onLoadMore = onLoadMore,
                    onApprove = onApprove,
                    onDisapprove = onDisapprove
                )
            }
        }
    }
}


