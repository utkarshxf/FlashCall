package com.example.myapplication.myapplication.flashcall.Screens.callServices

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.app.Notification
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
import androidx.compose.runtime.LaunchedEffect
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.myapplication.myapplication.flashcall.BaseClass
import com.example.myapplication.myapplication.flashcall.MainActivity
import com.example.myapplication.myapplication.flashcall.R
import com.example.myapplication.myapplication.flashcall.Screens.common.maskIfPhoneNumber
import io.getstream.video.android.core.StreamVideo
import io.getstream.video.android.core.notifications.DefaultNotificationHandler
import io.getstream.video.android.core.notifications.NotificationHandler
import io.getstream.video.android.model.StreamCallId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CustomNotificationHandler(
    private val application: Application,
) : DefaultNotificationHandler(
    application = application,
    hideRingingNotificationInForeground = false,
) {
    private val channelId = "incoming_calls"
    private val channelName = "Incoming Calls"

    init {
        Log.v("CustomNotificationHandler", "CustomNotificationHandler initialized")
        setupNotificationChannel()
    }

    override fun getChannelId(): String {
        return channelId
    }

    private fun setupNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                BaseClass.CHANNEL_ID,
                "Incoming Calls",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setShowBadge(true)
                enableLights(true)
                enableVibration(true)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            notificationManager.createNotificationChannel(channel)
            Log.d("CustomNotificationHandler", "Notification channel created")
        } else {
            Log.d(
                "CustomNotificationHandler",
                "Notification channel not created (Android version < Oreo)"
            )
        }
    }

    override fun onRingingCall(callId: StreamCallId, callDisplayName: String) {
        Log.d("CallActivity", "onRingingCall triggered for $callId")
        super.onRingingCall(callId, callDisplayName)
//        val fullScreenIntent = Intent(application, IncomingCallActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            putExtra(NotificationHandler.INTENT_EXTRA_CALL_CID, callId)
//            putExtra(NotificationHandler.INTENT_EXTRA_CALL_DISPLAY_NAME, callDisplayName)
//        }
//
//
//        val acceptCallIntent = Intent(application, IncomingCallActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            putExtra(NotificationHandler.INTENT_EXTRA_CALL_CID, callId)
//            putExtra("call_accepted", true)
//            putExtra(NotificationHandler.INTENT_EXTRA_CALL_DISPLAY_NAME, callDisplayName)
//        }
//
//        val rejectCallIntent = Intent(application, DeclineCallReceiver::class.java).apply {
//            putExtra(NotificationHandler.INTENT_EXTRA_CALL_CID, callId.id)  // Passing callId to the receiver
//        }
//        val rejectCallPendingIntent: PendingIntent = PendingIntent.getBroadcast(
//            application, 0, rejectCallIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )

//        val fullScreenPendingIntent = PendingIntent.getActivity(
//            application,
//            0,
//            fullScreenIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//
//        val acceptPendingIntent = PendingIntent.getActivity(
//            application,
//            0,
//            acceptCallIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )

//        val builder = NotificationCompat.Builder(application, channelId)
//            .setSmallIcon(R.drawable.voice1)
//            .setContentTitle("Incoming call")
//            .setContentText("${maskIfPhoneNumber(callDisplayName)} is calling")
//            .setCategory(NotificationCompat.CATEGORY_CALL)
//            .setPriority(NotificationCompat.PRIORITY_MAX)
//            .setFullScreenIntent(fullScreenPendingIntent, true)
//            .setOngoing(true)
//            .setAutoCancel(true)
//            .setSound(RingtoneManager.getDefaultUri(R.raw.call_incoming_sound))
//            .setVibrate(longArrayOf(0, 500, 1000))
//            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//            .addAction(
//                R.drawable.logo,  // Your custom icon for Accept button
//                "Reject",                    // Text for the Accept button
//                rejectCallPendingIntent          // PendingIntent triggered when Accept is pressed
//            )
//            .addAction(
//                R.drawable.logo,  // Your custom icon for Accept button
//                "Answer",                    // Text for the Accept button
//                acceptPendingIntent          // PendingIntent triggered when Accept is pressed
//            )


//        val notification = builder.build()
//
//        if (ActivityCompat.checkSelfPermission(
//                application,
//                Manifest.permission.POST_NOTIFICATIONS
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            Log.v("qwerty00" , callId.hashCode().toString())
//            notificationManager.notify( callId.hashCode(), notification)
//        } else {
//            Log.w("CustomNotificationHandler", "POST_NOTIFICATIONS permission not granted")
//        }
//
//        fullScreenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        application.startActivity(fullScreenIntent)
    }


//    private fun createAnswerIntent(callId: StreamCallId): PendingIntent {
//        val intent = Intent(application, AnswerCallReceiver::class.java).apply {
//            action = "ANSWER_CALL"
//            putExtra(NotificationHandler.INTENT_EXTRA_CALL_CID, callId)
//        }
//        return PendingIntent.getBroadcast(
//            application,
//            1,
//            intent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//    }

//    private fun createDeclineIntent(callId: StreamCallId): PendingIntent {
//        val intent = Intent(application, DeclineCallReceiver::class.java).apply {
//            action = "DECLINE_CALL"
//            putExtra(NotificationHandler.INTENT_EXTRA_CALL_CID, callId)
//        }
//        return PendingIntent.getBroadcast(
//            application,
//            2,
//            intent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//    }

    @SuppressLint("MissingPermission")
    override fun onMissedCall(callId: StreamCallId, callDisplayName: String) {
        Log.d("CustomNotificationHandler", "onMissedCall triggered for $callId")
        super.onMissedCall(callId, callDisplayName)
        // Cancel the ringing notification
//        notificationManager.cancel(callId.hashCode())
//
//        // Send broadcast to finish IncomingCallActivity
//        val intent = Intent("ACTION_CALL_ENDED")
//        application.sendBroadcast(intent)
//
//        val notification = NotificationCompat.Builder(application, getChannelId())
//            .setSmallIcon(R.drawable.vector)
//            .setContentIntent(buildContentIntent())
//            .setCategory(NotificationCompat.CATEGORY_CALL)
//            .setDefaults(NotificationCompat.DEFAULT_ALL)
//            .setOnlyAlertOnce(true)
//            .setWhen(System.currentTimeMillis())
//            .setContentTitle("Missed Call from ${maskIfPhoneNumber(callDisplayName)}")
//            .setAutoCancel(true)
//            .build()
//        notificationManager.notify(callId.hashCode(), notification)
//    }
//
//    private fun buildContentIntent() = PendingIntent.getActivity(
//        application,
//        0,
//        Intent(application, MainActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
//        },
//        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
//    )
    }
}