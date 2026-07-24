package com.kaushalpanjee.notification.with_api

enum class NotificationStatus {
    PENDING,
    APPROVED,
    REJECTED;

    companion object {
        fun from(value: String?): NotificationStatus =
            when (value?.uppercase()) {
                "ACCEPTED" -> APPROVED
                "REJECTED" -> REJECTED
                else -> PENDING
            }
    }
}