//package com.kaushalpanjee.notification.with_api
//
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.ArrowBack
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.navigation.NavController
//import com.kaushalpanjee.common.CommonViewModel
//import com.kaushalpanjee.notification.notificationUi.NotificationStatus
//
//@Composable
//fun NotificationListScreenN(
//    navController: NavController,
//    commonViewModel: CommonViewModel
//) {
//    val state by commonViewModel.notifications.collectAsState()
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Notifications") },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
//                    }
//                }
//            )
//        }
//    ) { padding ->
//
//        NotificationContent(
//            modifier = Modifier.padding(padding),
//            state = state,
//            onApprove = { id ->
//                commonViewModel.updateNotificationStatus(
//                    id,
//                    NotificationStatus.ACCEPTED
//                )
//            },
//            onDisapprove = { id ->
//                commonViewModel.updateNotificationStatus(
//                    id,
//                    NotificationStatus.REJECTED
//                )
//            }
//        )
//    }
//}
