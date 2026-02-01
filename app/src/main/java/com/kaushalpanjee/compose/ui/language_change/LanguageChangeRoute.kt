package com.kaushalpanjee.compose.ui.language_change

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.kaushalpanjee.compose.presentation.contract.LanguageChangeEffect
import com.kaushalpanjee.compose.presentation.contract.LanguageChangeEvent
import com.kaushalpanjee.compose.presentation.viewmodel.LanguageChangeViewModel
import com.kaushalpanjee.compose.ui.screen.LanguageChangeScreen
import com.kaushalpanjee.core.util.AppUtil
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LanguageChangeRoute(
    viewModel: LanguageChangeViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onApplyLanguage: (String) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.setInitialLanguage(
            AppUtil.getSavedLanguagePreference(context)
        )
    }

    LaunchedEffect(viewModel) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is LanguageChangeEffect.ApplyLanguage -> {
                    onApplyLanguage(effect.code)
                }
            }
        }
    }

    LanguageChangeScreen(
        state = state,
        onBackClick = onBack,
        onLanguageClick = {
            viewModel.process(LanguageChangeEvent.SelectLanguage(it))
        },
        onDialogConfirm = {
            viewModel.process(LanguageChangeEvent.ConfirmChange)
        },
        onDialogDismiss = {
            viewModel.process(LanguageChangeEvent.DismissDialog)
        }
    )
}
