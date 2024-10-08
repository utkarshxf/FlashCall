package com.example.myapplication.myapplication.flashcall

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.example.myapplication.myapplication.flashcall.Screens.callServices.CustomNotificationHandler
import com.example.myapplication.myapplication.flashcall.Screens.callServices.IncomingCallActivity
import com.example.myapplication.myapplication.flashcall.Screens.chats.ChatForegroundService
import com.example.myapplication.myapplication.flashcall.Screens.chats.IncomingChatRequestActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.getstream.android.push.firebase.FirebaseMessagingDelegate


class PushNotificationService : FirebaseMessagingService() {
init {

    Log.v("PushNotificationService", "PushNotificationService initialized")
}
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    // [START on_new_token]
    /**
     * Called if the FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
                Log.d("IsForeground", "OnReceiveMessage")

                // Start the foreground service
                val serviceIntent = Intent(this, ChatForegroundService::class.java).apply {
                    // Optionally put extras here to pass data to the service
                    putExtra("some_key", "some_value")
                }
                startForegroundService(serviceIntent)
            }
        }catch (e:Exception) {
            Log.e("RemoteMessage" , e.toString())
        }
//        try {
//            if (FirebaseMessagingDelegate.handleRemoteMessage(message)) {
//                // RemoteMessage was from Stream and it is already processed
//                Log.d("FCM", "Stream message processed")
//            } else {
////                super.onMessageReceived(message)
//                Log.v("PushNotificationService", "message received")
//                message.notification?.let {
//                    Log.v("overlayScreenLaunch", "start incoming chat activity")
//
//                    // Start the foreground service
//                    val serviceIntent = Intent(this, ChatForegroundService::class.java).apply {
//                        // Optionally put extras here to pass data to the service
//                        putExtra("some_key", "some_value")
//                    }
//                    startForegroundService(serviceIntent)
//
////                    val intent = Intent(this, IncomingChatRequestActivity::class.java).apply {
////                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Necessary to start an Activity from Service
////                    }
////                    startActivity(intent)
////                    sendNotification(it)
//                }
//                Log.v("message.data" , message.data.toString())
//                    ?: run {
////                        handleDataMessage(message.data)
//                    }
//                Log.d("FCM", "message processed")
//            }
//        } catch (exception: IllegalStateException) {
//            // StreamVideo was not initialized
//            Log.e("PushNotificationService", "StreamVideo was not initialized", exception)
//            // Still try to show the notification
//            message.notification?.let {
//                Log.e("overlayScreenLaunch", "start over lay screen", exception)
//                val serviceIntent = Intent(this, ChatForegroundService::class.java).apply {
//                    // Optionally put extras here to pass data to the service
//                    putExtra("some_key", "some_value")
//                }
//                startForegroundService(serviceIntent)
////                val intent = Intent(this, IncomingChatRequestActivity::class.java).apply {
////                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Necessary to start an Activity from Service
////                }
////                startActivity(intent)
////                sendNotification(it)
//            }
//
//                ?: run {
//                    Log.v("message.data" , message.data.toString())
////                    handleDataMessage(message.data)
//                }
//        }
    }


//    override fun onMessageReceived(message: RemoteMessage) {
//        super.onMessageReceived(message)
//        message.notification?.let {
//            // Handle the message
//            Log.d("FCM", "Message Notification Body: ${it.body}")
//            sendNotification(it)
//        }
//    }


    private fun handleDataMessage(data: Map<String, String>) {
        // Create a notification from data payload
        val title = data["title"] ?: "New Message"
        val body = data["body"] ?: "You have a new message"
        val notification = RemoteMessage.Builder(title)
//        sendNotification(notification)
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private fun sendNotification(notification: RemoteMessage.Notification) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.voice1)
            .setContentTitle(notification.title)
            .setContentText(notification.body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setOngoing(true)
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(R.raw.call_incoming_sound))
            .setVibrate(longArrayOf(0, 500, 1000))
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "default_channel"
            val channelName = "Default Channel"
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Channel for all notifications"
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 1000)
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}