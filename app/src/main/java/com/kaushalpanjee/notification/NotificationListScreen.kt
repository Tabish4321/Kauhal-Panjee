package com.kaushalpanjee.notification

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.kaushalpanjee.BuildConfig
import com.kaushalpanjee.common.CommonViewModel
import com.kaushalpanjee.common.model.request.TrainingCenterReq
import com.kaushalpanjee.core.util.AppUtil
import com.kaushalpanjee.core.util.Resource
import com.kaushalpanjee.core.util.UserPreferences
import com.kaushalpanjee.notification.notificationUi.DummyNotificationProvider
import com.kaushalpanjee.notification.notificationUi.NotificationDataSource
import com.kaushalpanjee.notification.notificationUi.NotificationStatus
import com.kaushalpanjee.notification.notificationUi.NotificationUiModel

/**
 * Created by Rishi Porwal
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationListScreen(
    navController: NavController,
    commonViewModel: CommonViewModel,
    dataSource: NotificationDataSource = DummyNotificationProvider
) {
    val context = LocalContext.current
    val notifications = remember { dataSource.getNotifications() }
    val listState = rememberLazyListState()
    //val state by commonViewModel.notifications.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.LightGray,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black,
                    actionIconContentColor = Color.Black
                ),
                title = { Text("Notifications") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        },
    ) { padding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(vertical = 8.dp),
        ) {
            items(
                items = notifications,
                key = { it.id }
            ) { item ->
                NotificationItemCard(
                    item = item,
                    onApprove = {
                        // update
                         item.status = NotificationStatus.ACCEPTED
                        Toast.makeText(context, "Approved", Toast.LENGTH_SHORT).show()
                    },
                    onDisapprove = {
                        // update
                         item.status = NotificationStatus.REJECTED
                        Toast.makeText(context, "Disapproved", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}
