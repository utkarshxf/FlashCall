package com.example.myapplication.myapplication.flashcall.Screens.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AnswerCallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val callId = intent.getStringExtra("CALL_ID")
        // Implement logic to answer the call using the Stream SDK
        Log.d("AnswerCallReceiver", "Answering call: $callId")
        // You'll need to implement the actual call answering logic here
    }
}

class DeclineCallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val callId = intent.getStringExtra("CALL_ID")
        // Implement logic to decline the call using the Stream SDK
        Log.d("DeclineCallReceiver", "Declining call: $callId")
        // You'll need to implement the actual call declining logic here
    }
}