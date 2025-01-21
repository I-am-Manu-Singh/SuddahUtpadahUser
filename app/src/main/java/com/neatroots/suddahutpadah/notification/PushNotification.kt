package com.neatroots.suddahutpadah.notification


data class PushNotification(
    val to: String? =null,
    val data: NotificationData
)