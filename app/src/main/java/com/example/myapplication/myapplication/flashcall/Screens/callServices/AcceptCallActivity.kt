package com.example.myapplication.myapplication.flashcall.Screens.callServices

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.video.android.core.StreamVideo
import io.getstream.video.android.core.notifications.NotificationHandler
import io.getstream.video.android.model.streamCallId
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AcceptCallActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Handle incoming call
        Log.e("CallActivity", "Incoming call accepted")
        val callId = intent.streamCallId(NotificationHandler.INTENT_EXTRA_CALL_CID)
        lifecycleScope.launch {
            if (callId == null) {
                finishAndRemoveTask()
                return@launch
            }
            val call = StreamVideo.instance().call(callId.type, callId.id)
            setContent {
                CompositionLocalProvider(
                    androidx.lifecycle.compose.LocalLifecycleOwner provides androidx.compose.ui.platform.LocalLifecycleOwner.current,
                ) {
                    lifecycleScope.launch {
                        call.join()
                    }
                    CallScreen(call = call, showDebugOptions = true, onCallDisconnected = {
                        call.leave()
                        finishAndRemoveTask()
                    }, onUserLeaveCall = {
                        call.leave()
                        finishAndRemoveTask()
                    })
                }
            }
        }
    }
}
