package com.kaushalpanjee.compose.presentation.contract

import com.kaushalpanjee.common.model.LoginResponse
import com.kaushalpanjee.common.model.response.UnnatiListResponse
import com.kaushalpanjee.core.util.Resource

/**
 * Created by Rishi Porwal
 */


sealed class LoginEvent {
    data class OnUsernameChanged(val username: String) : LoginEvent()
    data class OnPasswordChanged(val password: String) : LoginEvent()
    data class OnShowPasswordChanged(val showPassword: Boolean) : LoginEvent()
    object OnLoginClicked : LoginEvent()
    object OnForgotPasswordClicked : LoginEvent()
    object OnRegisterClicked : LoginEvent()
    object OnAboutUnnatiClicked : LoginEvent()
    object OnLanguageChangeClicked : LoginEvent()
    object GetToken : LoginEvent()
    object GetUnnatiData : LoginEvent()
}

data class LoginState(
    val username: String = "",
    val password: String = "",
    val showPassword: Boolean = false,
    val isLoading: Boolean = false,
    val token: String = "",
    val saltPassword: String = "",
    val loginResponse: Resource<LoginResponse>? = null,
    val unnatiData: Resource<UnnatiListResponse>? = null,
    val versionName: String = "",
    val androidId: String = ""
)

sealed class LoginEffect {
    data class ShowSnackbar(val message: String) : LoginEffect()
    data class NavigateTo(val destination: String) : LoginEffect()
    data class ShowUpdateDialog(val message: String) : LoginEffect()
    data class ShowToast(val message: String) : LoginEffect()
    object NavigateToHome : LoginEffect()
    object NavigateToRegister : LoginEffect()
    object NavigateToForgotPassword : LoginEffect()
    object NavigateToAboutUnnati : LoginEffect()
    object NavigateToLanguageChange : LoginEffect()
}