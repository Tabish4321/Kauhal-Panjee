package com.kaushalpanjee.compose.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaushalpanjee.BuildConfig
import com.kaushalpanjee.common.model.request.ChangePassReq
import com.kaushalpanjee.common.model.response.InsertRes
import com.kaushalpanjee.compose.domain.usecase.GetChangePasswordUseCases
import com.kaushalpanjee.core.util.AESCryptography
import com.kaushalpanjee.core.util.AppConstant
import com.kaushalpanjee.core.util.AppUtil
import com.kaushalpanjee.core.util.Resource
import com.kaushalpanjee.core.util.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.kaushalpanjee.compose.presentation.contract.ChangePasswordContract
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val getChangePasswordUseCases: GetChangePasswordUseCases,
    private val userPreferences: UserPreferences,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(ChangePasswordContract.State())
    val state: StateFlow<ChangePasswordContract.State> = _state.asStateFlow()

    private val _sideEffects = MutableSharedFlow<ChangePasswordContract.SideEffect>()
    val sideEffects = _sideEffects.asSharedFlow()

    private var isLoading = false

    fun onEvent(event: ChangePasswordContract.Event) {
        when (event) {
            is ChangePasswordContract.Event.OldPasswordChanged ->
                _state.update { it.copy(oldPassword = event.value) }

            is ChangePasswordContract.Event.NewPasswordChanged ->
                _state.update { it.copy(newPassword = event.value) }

            is ChangePasswordContract.Event.ConfirmPasswordChanged ->
                _state.update { it.copy(confirmPassword = event.value) }

            ChangePasswordContract.Event.Submit ->
                submitChangePassword()
        }
    }

    private fun submitChangePassword() {
        if (isLoading) return

        val state = _state.value

        if (state.oldPassword.isBlank() ||
            state.newPassword.isBlank() ||
            state.confirmPassword.isBlank()
        ) {
            emitSideEffect(ChangePasswordContract.SideEffect.ShowToast("Please fill all fields"))
            return
        }

        if (state.newPassword != state.confirmPassword) {
            emitSideEffect(ChangePasswordContract.SideEffect.ShowToast("Confirm password not matched"))
            return
        }

        val request = ChangePassReq(
            appVersion = BuildConfig.VERSION_NAME,
            loginId = userPreferences.getUseID(),
            oldPassword = AppUtil.sha512Hash(state.oldPassword),
            newPassword = AESCryptography.encryptIntoBase64String(
                state.newPassword,
                AppConstant.Constants.ENCRYPT_KEY,
                AppConstant.Constants.ENCRYPT_IV_KEY
            )
        )

        isLoading = true
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            getChangePasswordUseCases(changePassword = request, header =  AppUtil.getSavedTokenPreference(context)).collectLatest { result ->
                isLoading = false

                when (result) {
                    is Resource.Loading -> Unit

                    is Resource.Error -> {
                        _state.update { it.copy(isLoading = false) }
                        emitSideEffect(
                            ChangePasswordContract.SideEffect.ShowError(
                                result.error?.message ?: "Something went wrong"
                            )
                        )
                    }

                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                changePasswordResult = result.data
                            )
                        }
                        handleResponse(result.data)
                    }
                }
            }
        }
    }

    private fun handleResponse(data: InsertRes?) {
        when (data?.responseCode) {
            200 -> {
                emitSideEffect(
                    ChangePasswordContract.SideEffect.ShowToast(data.responseDesc)
                )
                emitSideEffect(ChangePasswordContract.SideEffect.NavigateHome)
            }

            301 -> emitSideEffect(ChangePasswordContract.SideEffect.ForceUpdate)

            206, 207, 208 ->
                emitSideEffect(
                    ChangePasswordContract.SideEffect.ShowToast(data.responseDesc)
                )

            401 -> emitSideEffect(ChangePasswordContract.SideEffect.SessionExpired)

            else ->
                emitSideEffect(
                    ChangePasswordContract.SideEffect.ShowError("Something went wrong")
                )
        }
    }

    private fun emitSideEffect(effect: ChangePasswordContract.SideEffect) {
        viewModelScope.launch { _sideEffects.emit(effect) }
    }
}


