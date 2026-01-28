//package com.kaushalpanjee.notification.with_api
//
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.lazy.rememberLazyListState
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import com.kaushalpanjee.core.util.Resource
//import com.kaushalpanjee.notification.NotificationItemCard
//import com.kaushalpanjee.notification.notificationUi.NotificationStatus
//import com.kaushalpanjee.notification.notificationUi.NotificationUiModel
//
//@Composable
// fun NotificationContent(
//    modifier: Modifier = Modifier,
//    state: Resource<List<NotificationUiModel>>,
//    onApprove: (String) -> Unit,
//    onDisapprove: (String) -> Unit
//) {
//    when (state) {
//        is Resource.Loading -> LoadingView(modifier)
//        is Resource.Error -> ErrorView(modifier)
//        is Resource.Success -> NotificationList(
//            modifier = modifier,
//            notifications = state.data
//        ) { id, status ->
//            when (status) {
//                NotificationStatus.ACCEPTED -> onApprove(id)
//                NotificationStatus.REJECTED -> onDisapprove(id)
//                else -> {}
//            }
//        }
//    }
//}
//
//
//@Composable
//private fun LoadingView(modifier: Modifier) {
//    Box(
//        modifier = modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        CircularProgressIndicator()
//    }
//}
//
//@Composable
//private fun ErrorView(modifier: Modifier) {
//    Box(
//        modifier = modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Text("Failed to load notifications")
//    }
//}
//
//
//@Composable
//private fun NotificationList(
//    modifier: Modifier,
//    notifications: List<NotificationUiModel>,
//    onAction: (String, NotificationStatus) -> Unit
//) {
//    val listState = rememberLazyListState()
//
//    LazyColumn(
//        modifier = modifier.fillMaxSize(),
//        state = listState,
//        contentPadding = PaddingValues(vertical = 8.dp)
//    ) {
//        items(
//            items = notifications,
//            key = { it.id }
//        ) { item ->
//            NotificationItemCard(
//                item = item,
//                onApprove = {
//                    onAction(item.id, NotificationStatus.ACCEPTED)
//                },
//                onDisapprove = {
//                    onAction(item.id, NotificationStatus.REJECTED)
//                }
//            )
//        }
//    }
//}
//
//
