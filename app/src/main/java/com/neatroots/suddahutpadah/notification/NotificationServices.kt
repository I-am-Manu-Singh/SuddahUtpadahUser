package com.neatroots.suddahutpadah.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.neatroots.suddahutpadah.R
import com.neatroots.suddahutpadah.activities.HomeMainActivity
import kotlin.random.Random

class NotificationServices: FirebaseMessagingService() {

    val channelID = "basket"
    override fun onMessageReceived(message: RemoteMessage) {
        val manager = getSystemService(NOTIFICATION_SERVICE)
        createNotificationChannel(manager as NotificationManager)

        val intent = Intent(this, HomeMainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(this, channelID)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["body"])
            .setSmallIcon(R.drawable.icon_updated)
            .setAutoCancel(false)
            .setContentIntent(pendingIntent)
            .build()

        manager.notify(Random.nextInt(), notification)
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(channelID, "basket", NotificationManager.IMPORTANCE_DEFAULT)
            .apply {
                description = "Notification"
                enableLights(true)
            }
        notificationManager.createNotificationChannel(channel)
    }
}