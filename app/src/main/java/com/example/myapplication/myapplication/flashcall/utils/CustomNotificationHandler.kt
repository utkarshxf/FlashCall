package com.example.myapplication.myapplication.flashcall.utils

import android.annotation.SuppressLint
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.myapplication.myapplication.flashcall.BaseClass
import com.example.myapplication.myapplication.flashcall.MainActivity
import com.example.myapplication.myapplication.flashcall.R
import com.example.myapplication.myapplication.flashcall.Screens.broadcast.AnswerCallReceiver
import com.example.myapplication.myapplication.flashcall.Screens.broadcast.DeclineCallReceiver
import io.getstream.video.android.core.notifications.DefaultNotificationHandler
import io.getstream.video.android.model.StreamCallId

class CustomNotificationHandler(
    private val application: Application,
) : DefaultNotificationHandler(
    application = application,
    hideRingingNotificationInForeground = true,
) {
    init {
        Log.v("CustomNotificationHandler", "CustomNotificationHandler initialized")
    }
    override fun getChannelId(): String {
        return BaseClass.CHANNEL_ID
    }
    @SuppressLint("MissingPermission")
    override fun onRingingCall(callId: StreamCallId, callDisplayName: String) {
        Log.d("CustomNotificationHandler", "onRingingCall triggered for $callDisplayName")

        val intent = Intent(application, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("CALL_ID", callId.toString())
        }

        val pendingIntent = PendingIntent.getActivity(
            application,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(application, getChannelId())
            .setSmallIcon(R.drawable.voice1) // Make sure you have this icon in your drawable resources
            .setContentTitle("Incoming Call")
            .setContentText("$callDisplayName is calling")
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setFullScreenIntent(pendingIntent, true)
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE))
            .setVibrate(longArrayOf(0, 500, 1000))
            .addAction(
                R.drawable.group99, // Make sure you have this icon
                "Answer",
                createAnswerIntent(callId)
            )
            .addAction(
                R.drawable.endcall,
                "Decline",
                createDeclineIntent(callId)
            )
            .build()

        notificationManager.notify(callId.hashCode(), notification)
    }


    private fun createAnswerIntent(callId: StreamCallId): PendingIntent {
        val intent = Intent(application, AnswerCallReceiver::class.java).apply {
            action = "ANSWER_CALL"
            putExtra("CALL_ID", callId.toString())
        }
        return PendingIntent.getBroadcast(
            application,
            1,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun createDeclineIntent(callId: StreamCallId): PendingIntent {
        val intent = Intent(application, DeclineCallReceiver::class.java).apply {
            action = "DECLINE_CALL"
            putExtra("CALL_ID", callId.toString())
        }
        return PendingIntent.getBroadcast(
            application,
            2,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
    @SuppressLint("MissingPermission")
    override fun onMissedCall(callId: StreamCallId, callDisplayName: String) {
        val notification = NotificationCompat.Builder(application, getChannelId())
            .setSmallIcon(R.drawable.vector)
            .setContentIntent(buildContentIntent())
            .setContentTitle("Tutorial Missed Call from $callDisplayName")
            .setAutoCancel(true)
            .build()
        notificationManager.notify(callId.hashCode(), notification)
    }

    private fun buildContentIntent() = PendingIntent.getActivity(
        application,
        0,
        Intent(application, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        },
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
    )
}