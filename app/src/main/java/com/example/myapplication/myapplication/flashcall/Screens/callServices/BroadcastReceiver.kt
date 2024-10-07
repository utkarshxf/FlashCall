package com.example.myapplication.myapplication.flashcall.Screens.callServices

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import io.getstream.video.android.core.StreamVideo
import io.getstream.video.android.core.notifications.NotificationHandler
import io.getstream.video.android.model.streamCallId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AnswerCallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val callId = intent.streamCallId(NotificationHandler.INTENT_EXTRA_CALL_CID)
        if (callId == null) {
            Log.e("AnswerCallReceiver", "Received null callId")
            return
        }

        val ongoingCallIntent = Intent(context, IncomingCallActivity::class.java).apply {
            putExtra(NotificationHandler.INTENT_EXTRA_CALL_CID, callId)
            action = NotificationHandler.ACTION_ACCEPT_CALL
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        context.startActivity(ongoingCallIntent)

        // Dismiss the notification
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(callId.hashCode())
    }
}
//class DeclineCallReceiver : BroadcastReceiver() {
//    override fun onReceive(context: Context, intent: Intent) {
//        val callId = intent.getStringExtra("CALL_ID") ?: return
//
//        val call = StreamVideo.instance().call(callId.type, callId.id)
//        NotificationManagerCompat.from(context).cancelAll()
//
//        // Reject the call asynchronously
//        CoroutineScope(Dispatchers.IO).launch {
//            call.reject()
//        }
//    }
//}