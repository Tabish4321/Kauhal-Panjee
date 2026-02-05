package com.kaushalpanjee.compose.presentation.contract


import com.kaushalpanjee.common.model.response.InsertRes
import com.kaushalpanjee.core.util.Resource

object ChangePasswordContract {

    data class State(
        val oldPassword: String = "",
        val newPassword: String = "",
        val confirmPassword: String = "",
        val changePasswordResult:  InsertRes? = null,
        val isLoading: Boolean = false
    )

    sealed class Event {
        data class OldPasswordChanged(val value: String) : Event()
        data class NewPasswordChanged(val value: String) : Event()
        data class ConfirmPasswordChanged(val value: String) : Event()
        object Submit : Event()
    }

    sealed class SideEffect {
        data class ShowToast(val message: String) : SideEffect()
        data class ShowError(val message: String) : SideEffect()
        object NavigateHome : SideEffect()
        object ForceUpdate : SideEffect()
        object SessionExpired : SideEffect()
    }
}
