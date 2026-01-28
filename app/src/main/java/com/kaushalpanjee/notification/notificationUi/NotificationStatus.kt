package com.kaushalpanjee.notification.notificationUi

enum class NotificationStatus {
    PENDING,
    ACCEPTED,
    REJECTED;

    companion object {
        fun from(value: String?): NotificationStatus =
            when (value?.uppercase()) {
                "ACCEPTED" -> ACCEPTED
                "REJECTED" -> REJECTED
                else -> PENDING
            }
    }
}
