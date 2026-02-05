package com.kaushalpanjee.compose.ui.changePassword

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.kaushalpanjee.compose.presentation.contract.ChangePasswordContract
import com.kaushalpanjee.compose.presentation.viewmodel.ChangePasswordViewModel
import com.kaushalpanjee.compose.ui.screen.ChangePasswordScreen
import com.kaushalpanjee.core.util.AppUtil
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ChangePasswordRoute(
    viewModel: ChangePasswordViewModel = hiltViewModel(),
    onNavigateHome: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collectLatest { effect ->
            when (effect) {
                is ChangePasswordContract.SideEffect.ShowToast ->
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()

                is ChangePasswordContract.SideEffect.ShowError ->
                    Toast.makeText(context, effect.message, Toast.LENGTH_LONG).show()

                ChangePasswordContract.SideEffect.NavigateHome ->{onNavigateHome()}


                ChangePasswordContract.SideEffect.SessionExpired ->{}
                   // AppUtil.showSessionExpiredDialog(context)

                ChangePasswordContract.SideEffect.ForceUpdate ->
                    Toast.makeText(
                        context,
                        "Please update from Play Store",
                        Toast.LENGTH_LONG
                    ).show()
            }
        }
    }

    ChangePasswordScreen(
        uiState = state,
        onOldPassChange = {
            viewModel.onEvent(ChangePasswordContract.Event.OldPasswordChanged(it))
        },
        onNewPassChange = {
            viewModel.onEvent(ChangePasswordContract.Event.NewPasswordChanged(it))
        },
        onConfirmPassChange = {
            viewModel.onEvent(ChangePasswordContract.Event.ConfirmPasswordChanged(it))
        },
        onSubmit = {
            viewModel.onEvent(ChangePasswordContract.Event.Submit)
        },
        onBack = {
            onNavigateHome()
        }
    )
}
