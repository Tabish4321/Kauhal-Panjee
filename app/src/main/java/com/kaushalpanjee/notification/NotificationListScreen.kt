package com.kaushalpanjee.notification

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.kaushalpanjee.BuildConfig
import com.kaushalpanjee.common.CommonViewModel
import com.kaushalpanjee.common.model.request.TrainingCenterReq
import com.kaushalpanjee.core.util.AppUtil
import com.kaushalpanjee.core.util.Resource
import com.kaushalpanjee.core.util.UserPreferences
import com.kaushalpanjee.notification.notificationUi.DummyNotificationProvider
import com.kaushalpanjee.notification.notificationUi.NotificationDataSource
import com.kaushalpanjee.notification.notificationUi.NotificationUiModel

/**
 * Created by Rishi Porwal
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationListScreen(
    navController: NavController,
    commonViewModel: CommonViewModel,
    context: Context,
    dataSource: NotificationDataSource = DummyNotificationProvider
) {
    val context = LocalContext.current
    val notifications = remember { dataSource.getNotifications() }
    val listState = rememberLazyListState()

//    val notificationState by commonViewModel
//        .getNotificationList
//        .collectAsStateWithLifecycle(initialValue = Resource.Loading())

//    LaunchedEffect(Unit) {
//        commonViewModel.getTrainingListAPI(
//            TrainingCenterReq(
//                BuildConfig.VERSION_NAME,
//                sectorCode = "SECTOR",
//                districtCode = "DISTRICT",
//                userId = UserPreferences(context).getUseID(),
//                androidId = AppUtil.getAndroidId(context)
//            ),
//            AppUtil.getSavedTokenPreference(context)
//        )
//    }


//    when (trainingState) {
//
//        is Resource.Loading -> {
//            CircularProgressIndicator()
//        }
//
//        is Resource.Error -> {
//            ErrorView(
//                message = trainingState.error?.message ?: "Something went wrong"
//            )
//        }
//
//        is Resource.Success -> {
//            val response = trainingState.data
//
//            when (response?.responseCode) {
//                200 -> {
//                    NotificationList(
//                        list = response.centerList
//                    )
//                }
//
//                301 -> {
//                    ShowSnackBar("Please update from PlayStore")
//                }
//
//                401 -> {
//                    AppUtil.showSessionExpiredDialog(navController, context)
//                }
//
//                else -> {
//                    ShowSnackBar("Something went wrong")
//                }
//            }
//        }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notifications") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(
                items = notifications,
                key = { it.id }
            ) { item ->
                NotificationItemCard(
                    item = item,
                    onClick = {
                        Toast
                            .makeText(context, item.title, Toast.LENGTH_SHORT)
                            .show()
                    }
                )
            }
        }
    }
}


@Composable
fun NotificationItemCard(
    item: NotificationUiModel,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(4.dp))

            Text(
                text = item.message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = item.time,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}