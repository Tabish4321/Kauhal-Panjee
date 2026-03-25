package com.kaushalpanjee.notification.with_api

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kaushalpanjee.core.util.Resource
import com.kaushalpanjee.notification.NotificationItemCard
import com.kaushalpanjee.notification.with_api.NotificationStatus
import com.kaushalpanjee.notification.with_api.model.NotificationUiModel
import com.kaushalpanjee.common.CommonViewModel

@Composable
fun NotificationContent(
    modifier: Modifier = Modifier,
    state: Resource<List<NotificationUiModel>>,
    onLoadMore: () -> Unit,
    onApprove: (String) -> Unit,

    onDisapprove: (String) -> Unit,
    commonViewModel: CommonViewModel
) {
    when (state) {

        is Resource.Loading -> {
            LoadingView(modifier)
        }

        is Resource.Error -> {
            ErrorView(
                modifier = modifier
            )
        }

        is Resource.Success -> {
            val list = state.data
            if (list.isNullOrEmpty()) {
                EmptyListView(modifier)
            } else {
                NotificationList(
                    modifier = modifier,
                    notifications = list,
                    onLoadMore = onLoadMore,
                    onAction = { id, status ->
                        when (status) {
                            NotificationStatus.APPROVED -> onApprove(id)
                            NotificationStatus.REJECTED -> onDisapprove(id)
                            else -> Unit
                        }
                    },
                    commonViewModel = commonViewModel
                )
            }
        }
    }
}




@Composable
 fun LoadingView(modifier: Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorView(modifier: Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Failed to load notifications")
    }
}

@Composable
private fun NotificationList(
    modifier: Modifier,
    notifications: List<NotificationUiModel>,
    onLoadMore: () -> Unit,
    onAction: (String, NotificationStatus) -> Unit,
    commonViewModel: CommonViewModel
) {
    val listState = rememberLazyListState()

//    if (notifications.isEmpty()) {
//        EmptyNotificationView(modifier)
//        return
//    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(
            items = notifications,
            key = { it.id }
        ) { item -> NotificationItemCard(
                item = item,
                onApprove = {
                    onAction(item.id, NotificationStatus.APPROVED)
                },
                onDisapprove = {
                    onAction(item.id, NotificationStatus.REJECTED)
                },
                commonViewModel = commonViewModel
            )

        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex == notifications.lastIndex) {
                    onLoadMore()
                }
            }
    }
}


// Pagination trigger
//    if (listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ==
//        notifications.lastIndex
//    ) {
//        onLoadMore()
//    }
