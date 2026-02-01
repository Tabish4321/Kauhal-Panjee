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
import com.kaushalpanjee.compose.presentation.contract.InvitationContract
import com.kaushalpanjee.compose.presentation.viewmodel.NotificationViewModel
import com.kaushalpanjee.compose.ui.commonComponent.BaseTopBar
import com.kaushalpanjee.compose.ui.invitation_component.NotificationContent

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
        viewModel.onEvent(InvitationContract.Event.LoadNotifications)
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collectLatest { effect ->
            when (effect) {
                is InvitationContract.SideEffect.ShowToast ->
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()

                is InvitationContract.SideEffect.ShowError ->
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(
        topBar = {
            BaseTopBar(
                title = "Invitations",
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { padding ->
        NotificationContent(
            modifier = Modifier.padding(padding),
            state = state,
            onLoadMore = {
                viewModel.onEvent(InvitationContract.Event.LoadMoreNotifications)
            },
            onApprove = {
                viewModel.onEvent(
                    InvitationContract.Event.UpdateNotificationStatus(it, "APPROVED")
                )
            },
            onDisapprove = {
                viewModel.onEvent(
                    InvitationContract.Event.UpdateNotificationStatus(it, "REJECTED")
                )
            }
        )
    }
}
