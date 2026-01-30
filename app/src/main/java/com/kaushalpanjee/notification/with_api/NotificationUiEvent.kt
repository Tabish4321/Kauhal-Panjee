package com.kaushalpanjee.notification.with_api

sealed class NotificationUiEvent {
    data class ShowToast(val message: String) : NotificationUiEvent()
}