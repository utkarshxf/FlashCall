package com.example.myapplication.myapplication.flashcall

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.example.myapplication.myapplication.flashcall.Screens.chats.IncomingChatActivity
import com.example.myapplication.myapplication.flashcall.Screens.common.maskIfPhoneNumber
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.getstream.android.push.firebase.FirebaseMessagingDelegate


class PushNotificationService : FirebaseMessagingService() {
    private val channelId = "incoming_calls"
    private val channelName = "Incoming Calls"
    private lateinit var notificationManager: NotificationManager
    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()
    }

    // [START on_new_token]

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
//                val serviceIntent = Intent(this, ChatForegroundService::class.java).apply {
//                    // Optionally put extras here to pass data to the service
//                    putExtra("some_key", "some_value")
//                }
////                startForegroundService(serviceIntent)
//                val activityIntent = Intent(this, IncomingChatActivity::class.java).apply {
//                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
//                }
//                startActivity(activityIntent)
                showHighPriorityNotification(message)
            }
        } catch (e: Exception) {

        }
    }

    private fun showHighPriorityNotification(message: RemoteMessage) {
        val intent = Intent(this, IncomingChatActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val rejectCallIntent = Intent(application, IncomingChatActivity::class.java).apply {

        }
        val rejectCallPendingIntent: PendingIntent = PendingIntent.getBroadcast(
            application, 0, rejectCallIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val acceptCallIntent = Intent(application, IncomingChatActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val acceptPendingIntent = PendingIntent.getActivity(
            application,
            0,
            acceptCallIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationSound = RingtoneManager.getDefaultUri(R.raw.call_incoming_sound)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(io.getstream.video.android.core.R.drawable.stream_video_ic_call)
            .setContentTitle(maskIfPhoneNumber(message.notification?.title))
            .setContentText(
                maskIfPhoneNumber(message.notification?.body)
            ).setAutoCancel(true).setSound(notificationSound)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setFullScreenIntent(pendingIntent, true)
            .setContentIntent(pendingIntent)
            .setVibrate(longArrayOf(0, 500, 1000, 500, 1000)).setLights(Color.RED, 3000, 3000)
            .addAction(
                R.drawable.logo,  // Your custom icon for Accept button
                "Decline",                     // Text for the Accept button
                rejectCallPendingIntent            // PendingIntent triggered when Accept is pressed
            ).addAction(
                R.drawable.logo,                   // Your custom icon for Accept button
                "Answer",                    // Text for the Accept button
                acceptPendingIntent             // PendingIntent triggered when Accept is pressed
            )
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (ActivityCompat.checkSelfPermission(
                application, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            notificationManager.notify(
                System.currentTimeMillis().toInt(),
                notificationBuilder.build()
            )
        } else {

        }
        startActivity(intent)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationSound =
                RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_RINGTONE)
            val channel = NotificationChannel(
                channelId, channelName, NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "High priority channel for incoming calls"
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 1000, 500, 1000)
                setSound(
                    notificationSound,
                    AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()
                )
                lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            }
            notificationManager.createNotificationChannel(channel)
        }
    }
}