package com.example.myapplication.myapplication.flashcall.Screens.callServices

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import io.getstream.video.android.core.StreamVideo
import io.getstream.video.android.core.notifications.NotificationHandler
import io.getstream.video.android.model.streamCallId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AnswerCallReceiver : BroadcastReceiver() {
    private val scope = CoroutineScope(Dispatchers.IO)
    override fun onReceive(context: Context, intent: Intent) {
        val callId = intent.streamCallId(NotificationHandler.INTENT_EXTRA_CALL_CID)!!
        val call = StreamVideo.instance().call(callId.type, callId.id)
        scope.launch {
            call.join()
        }
        val ongoingCallIntent = Intent(context, IncomingCallActivity::class.java).apply {
            putExtra(NotificationHandler.INTENT_EXTRA_CALL_CID, callId)
            putExtra("NAVIGATE_TO_ONGOING_CALL", true)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        context.startActivity(ongoingCallIntent)
        // Dismiss the notification
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(callId.hashCode())
    }
}

// DeclineCallReceiver.kt
class DeclineCallReceiver : BroadcastReceiver() {
    private val scope = CoroutineScope(Dispatchers.IO)
    override fun onReceive(context: Context, intent: Intent) {
        val callId = intent.streamCallId(NotificationHandler.INTENT_EXTRA_CALL_CID)!!
        val call = StreamVideo.instance().call(callId.type, callId.id)
        scope.launch {
            call.reject()
        }
        // Finish the IncomingCallActivity if it's open
        val closeIntent = Intent(context, IncomingCallActivity::class.java).apply {
            action = "FINISH_ACTIVITY"
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        context.startActivity(closeIntent)
        // Dismiss the notification
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(callId.hashCode())
    }
}