package com.kaushalpanjee.notification.notificationUi


/**
 * Created by Rishi Porwal
 */
object DummyNotificationProvider : NotificationDataSource{

    override fun getNotifications(): List<NotificationUiModel> {
        return listOf(
            NotificationUiModel(
                "1",
                "Welcome",
                "Thanks for joining our app",
                "Just now"
            ),
            NotificationUiModel(
                "2",
                "Training Assigned",
                "You have been assigned a new training center",
                "1 hour ago"
            ),
            NotificationUiModel(
                "4",
                "Update",
                "New features are available",
                "Yesterday"
            ),
            NotificationUiModel(
                "5",
                "Update",
                "New features are available",
                "Yesterday"
            ),
            NotificationUiModel(
                "6",
                "Update",
                "New features are available",
                "Yesterday"
            )
        )
    }
}
