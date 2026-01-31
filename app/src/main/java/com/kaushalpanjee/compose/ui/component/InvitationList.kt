package com.kaushalpanjee.compose.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kaushalpanjee.compose.ui.commonComponent.EmptyListView
import com.kaushalpanjee.compose.ui.model.NotificationUiModel
import kotlinx.coroutines.flow.collectLatest

/**
* Created by Rishi Porwal 
*/

@OptIn(ExperimentalFoundationApi::class)
@Composable
 fun NotificationList(
    modifier: Modifier,
    notifications: List<NotificationUiModel>,
    actionLoadingIds: Set<String>,
    isLoadingMore: Boolean,
    onLoadMore: () -> Unit,
    onApprove: (String) -> Unit,
    onDisapprove: (String) -> Unit
) {
    val listState = rememberLazyListState()

    if (notifications.isEmpty()) {
        EmptyListView()
        return
    }

    LazyColumn(
        modifier=modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)

    ) {
        items(
            items = notifications,
            key = { it.id }
        ) { item ->
            NotificationItemCard(
                item = item,
                isLoading = actionLoadingIds.contains(item.id),
                onApprove = { onApprove(item.id) },
                onDisapprove = { onDisapprove(item.id) }
            )
        }

        if (isLoadingMore) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                }
            }
        }
    }

    // For Pagination Code
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collectLatest { lastVisibleIndex ->
                if (lastVisibleIndex == notifications.lastIndex) {
                    onLoadMore()
                }
            }
    }
}