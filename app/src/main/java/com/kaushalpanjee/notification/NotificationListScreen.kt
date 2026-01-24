package com.kaushalpanjee.notification

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

/**
 * Created by Rishi Porwal
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationListScreen(
    navController: NavController
) {
    val notifications = remember {
        DummyNotificationProvider.getNotifications()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notifications") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(items = notifications) { item -> NotificationItemCard(item) }

        }
    }
}

@Composable
fun NotificationItemCard(item: NotificationUiModel) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = item.message,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.time,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

object DummyNotificationProvider {

    fun getNotifications(): List<NotificationUiModel> {
        return listOf(
            NotificationUiModel(
                "1",
                "Welcome",
                "Thanks for joining our app",
                "Just now"
            ),
            NotificationUiModel(
                "2",
                "Training Assigned",
                "You have been assigned a new training center",
                "1 hour ago"
            ),
            NotificationUiModel(
                "3",
                "Update",
                "New features are available",
                "Yesterday"
            ),
            NotificationUiModel(
            "3",
            "Update",
            "New features are available",
            "Yesterday"
           ),
            NotificationUiModel(
             "3",
             "Update",
             "New features are available",
             "Yesterday"
            )
        )
    }
}

data class NotificationUiModel(
    val id: String,
    val title: String,
    val message: String,
    val time: String
)