package com.example.chat.data.network.datasource

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.framework.navigation.DeepLinks
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import androidx.core.net.toUri
import com.example.chat.R

class WhatsAppCloneMessagingService : FirebaseMessagingService() {
    companion object {
        const val CHANNEL_ID = "Chat_message"
        const val CHANNEL_DESCRIPTION = "Receive a notification when a chat message is received"
        const val CHANNEL_TITLE = "New chat message notification"
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if (message.data.isNotEmpty()) {
            val senderName =
                message.data["senderName"]
            val messageContent =
                message.data["message"]
            val chatId = message.data["chatId"]
            val messageId = message.data["messageId"]
            if (chatId != null && messageId != null) {
                showNotification(
                    senderName, messageId,
                    messageContent, chatId
                )
            }
        }
    }

    private fun showNotification(
        senderName: String?,
        messageId: String,
        messageContent: String?,
        chatId: String
    ) {
        val notificationManager = getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager
// Create a notification channel
// (if you want to support versions lower than Android Oreo, you will have to check the version here)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_TITLE,
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = CHANNEL_DESCRIPTION
                }
            notificationManager.createNotificationChannel(channel)
        }

        val deepLinkUrl =
            DeepLinks.chatRoute.replace("{chatId}", chatId)
        val intent = Intent(
            Intent.ACTION_VIEW,
            deepLinkUrl.toUri()
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
// Create a PendingIntent for the Intent
        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            intent, PendingIntent.FLAG_IMMUTABLE
        )
// Build the notification
        val notification = NotificationCompat.Builder(
            this,
            CHANNEL_ID
        )
            .setSmallIcon(
                com.google.android.gms.base.R.drawable.googleg_standard_color_18
            )
            .setContentTitle(senderName)
            .setContentText(messageContent)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
// Show the notification
        notificationManager.notify(messageId.toInt(), notification)
    }


}