package com.manriquetavi.jetregisterapp.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.messaging.FirebaseMessagingService

class FCMService: FirebaseMessagingService() {

    companion object {
        private const val  TAG = "FCM Notification"
        const val DEFAULT_NOTIFICATION_ID = 0
    }

    override fun onNewToken(token: String) {
        Log.i(TAG, "new FCM token created: $token")
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        initNotificationChannel(notificationManager)
    }

    private fun initNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION_CODES.O <= Build.VERSION.SDK_INT) {
            notificationManager.createNotificationChannelIfNotExist(
                channelId = "1",
                channelName = "Default"

            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun NotificationManager.createNotificationChannelIfNotExist(
    channelId: String,
    channelName: String,
    importance: Int = NotificationManager.IMPORTANCE_DEFAULT
) {
    var channel = this.getNotificationChannel(channelId)
    if (channel == null) {
        channel = NotificationChannel(
            channelId,
            channelName,
            importance
        )
    }
    this.createNotificationChannel(channel)
}