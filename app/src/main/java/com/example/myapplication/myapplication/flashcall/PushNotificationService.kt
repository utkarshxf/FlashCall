package com.example.myapplication.myapplication.flashcall

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.getstream.android.push.firebase.FirebaseMessagingDelegate

class PushNotificationService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d("FCM", "Stream message processed")

        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        try {
            if (FirebaseMessagingDelegate.handleRemoteMessage(message)) {
                // RemoteMessage was from Stream and it is already processed
                Log.d("FCM", "Stream message processed")
            } else {
                // Handle custom notification

                super.onMessageReceived(message)
                message.notification?.let { sendNotification(it) }
                    ?: run {
//                        handleDataMessage(message.data)
                    }
                Log.d("FCM", "message processed")
            }
        } catch (exception: IllegalStateException) {
            // StreamVideo was not initialized
            Log.e("PushNotificationService", "StreamVideo was not initialized", exception)
            // Still try to show the notification
            message.notification?.let { sendNotification(it) }
                ?: run {
//                    handleDataMessage(message.data)
                }
        }
    }


//    override fun onMessageReceived(message: RemoteMessage) {
//        super.onMessageReceived(message)
//        message.notification?.let {
//            // Handle the message
//            Log.d("FCM", "Message Notification Body: ${it.body}")
//            sendNotification(it)
//        }
//    }

    private fun sendNotification(notification: RemoteMessage.Notification) {
        // Create an intent that will open the app's MainActivity
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        // Create a PendingIntent with the intent to open the activity
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        // Create a notification channel (Required for Android 8.0 and above)
        val channelId = "default_channel"
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.voice1)  // Your app's notification icon
            .setContentTitle(notification.title)
            .setContentText(notification.body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setSound(null)
            .setVibrate(longArrayOf(0))
            .setLights(0, 0, 0)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setFullScreenIntent(pendingIntent, true)
            .setAutoCancel(true)  // Dismiss notification after it is clicked
            .setContentIntent(pendingIntent)  // Open app on click

        // Get the NotificationManager service
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create notification channel for Android 8.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Default Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Show the notification
        notificationManager.notify(0, notificationBuilder.build())
    }

}