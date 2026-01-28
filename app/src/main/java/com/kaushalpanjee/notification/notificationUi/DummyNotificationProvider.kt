package com.kaushalpanjee.notification.notificationUi


/**
 * Created by Rishi Porwal
 */
object DummyNotificationProvider : NotificationDataSource{

    override fun getNotifications(): List<NotificationUiModel> {
        val now = System.currentTimeMillis()

        return listOf(
            NotificationUiModel(
                id = "1",
                title = "Welcome to Kaushal Panjee ",
                message = "Your account has been successfully created. Start exploring now.",
                createdAt = now - 30_000, // 30 sec ago
                status = NotificationStatus.ACCEPTED
            ),

            NotificationUiModel(
                id = "2",
                title = "Training Assigned",
                message = "You have been assigned to the Digital Skills Training Center.",
                createdAt = now - 60 * 60 * 1000 // 1 hour ago
            ),

            NotificationUiModel(
                id = "3",
                title = "Profile Pending",
                message = "Please complete your profile to unlock all features.",
                createdAt = now - 3 * 60 * 60 * 1000 // 3 hours ago
            ),

            NotificationUiModel(
                id = "4",
                title = "Attendance Approved",
                message = "Your attendance for yesterday has been approved.",
                createdAt = now - 24 * 60 * 60 * 1000, // yesterday
                status = NotificationStatus.ACCEPTED
            ),

            NotificationUiModel(
                id = "5",
                title = "Training Request Rejected",
                message = "Your training transfer request was rejected. Contact admin for details.",
                createdAt = now - 2 * 24 * 60 * 60 * 1000, // 2 days ago
                status = NotificationStatus.REJECTED
            ),

            NotificationUiModel(
                id = "6",
                title = "New Feature Available ",
                message = "You can now track your progress directly from the dashboard.",
                createdAt = now - 5 * 24 * 60 * 60 * 1000 // 5 days ago
            )
        )
    }
}
