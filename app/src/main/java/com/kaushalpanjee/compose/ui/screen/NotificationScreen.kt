package com.kaushalpanjee.compose.ui.screen



import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.kaushalpanjee.compose.presentation.contract.NotificationContract
import com.kaushalpanjee.compose.presentation.viewmodel.NotificationViewModel
import com.kaushalpanjee.compose.ui.component.NotificationContent
import com.kaushalpanjee.compose.ui.commonComponent.NotificationTopBar

/**
 * Created by Rishi Porwal
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    navController: NavController,
    viewModel: NotificationViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.onEvent(NotificationContract.Event.LoadNotifications)
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collectLatest { effect ->
            when (effect) {
                is NotificationContract.SideEffect.ShowToast ->
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()

                is NotificationContract.SideEffect.ShowError ->
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }



    Scaffold(
        topBar = {
            NotificationTopBar(
                title = "Invitations",
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { padding ->
        NotificationContent(
            modifier = Modifier.padding(padding),
            state = state,
            onLoadMore = {
                viewModel.onEvent(NotificationContract.Event.LoadMoreNotifications)
            },
            onApprove = {
                viewModel.onEvent(
                    NotificationContract.Event.UpdateNotificationStatus(it, "APPROVED")
                )
            },
            onDisapprove = {
                viewModel.onEvent(
                    NotificationContract.Event.UpdateNotificationStatus(it, "REJECTED")
                )
            }
        )
    }
}
