package com.example.myapplication.myapplication.flashcall.Screens.callServices

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.video.android.compose.theme.VideoTheme
import io.getstream.video.android.compose.ui.components.call.ringing.RingingCallContent
import io.getstream.video.android.core.BuildConfig
import io.getstream.video.android.core.Call
import io.getstream.video.android.core.StreamVideo
import io.getstream.video.android.core.call.state.AcceptCall
import io.getstream.video.android.core.call.state.CallAction
import io.getstream.video.android.core.call.state.DeclineCall
import io.getstream.video.android.core.call.state.FlipCamera
import io.getstream.video.android.core.call.state.LeaveCall
import io.getstream.video.android.core.call.state.ToggleCamera
import io.getstream.video.android.core.call.state.ToggleMicrophone
import io.getstream.video.android.core.call.state.ToggleSpeakerphone
import io.getstream.video.android.core.notifications.NotificationHandler
import io.getstream.video.android.model.streamCallId
import kotlinx.coroutines.launch

@AndroidEntryPoint
class IncomingCallActivity_Temp : ComponentActivity() {
    private val callStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                "ACTION_CALL_ENDED" -> {
                    finishAndRemoveTask()
                }
            }
        }
    }
    private var showOngoingCall by mutableStateOf(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showWhenLockedAndTurnScreenOn()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val callId = intent.streamCallId(NotificationHandler.INTENT_EXTRA_CALL_CID)
        if (callId == null) {
            finishAndRemoveTask()
            return
        }
        lifecycleScope.launch {
            val call = StreamVideo.instance().call(callId.type, callId.id)
            if (NotificationHandler.ACTION_ACCEPT_CALL == intent.action && savedInstanceState == null) {
                call.accept()
                call.join()
            }
            setContent {
                CompositionLocalProvider(
                    androidx.lifecycle.compose.LocalLifecycleOwner provides androidx.compose.ui.platform.LocalLifecycleOwner.current,
                ) {
                    VideoTheme {
                        val onCallAction: (CallAction) -> Unit = { callAction ->
                            when (callAction) {
                                is ToggleCamera -> call.camera.setEnabled(callAction.isEnabled)
                                is ToggleMicrophone -> call.microphone.setEnabled(callAction.isEnabled)
                                is ToggleSpeakerphone -> call.speaker.setEnabled(callAction.isEnabled)
                                is FlipCamera -> call.camera.flip()
                                is LeaveCall -> {
                                    call.leave()
                                    finish()
                                }
                                is DeclineCall -> {
                                    lifecycleScope.launch {
                                        call.reject()
                                        call.leave()
                                        finish()
                                    }
                                }
                                is AcceptCall -> {
                                    lifecycleScope.launch {
                                        call.accept()
                                        call.join()
                                    }
                                }
                                else -> Unit
                            }
                        }
                        RingingCallContent(
                            call = call,
                            onCallAction = onCallAction,
                            modifier = Modifier.background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF1E1E1E),  // Top color
                                        Color(0xFF444444),  // Middle color
                                        Color(0xFF121212)   // Bottom color
                                    ),
                                    startY = 0f,
                                    endY = 3000f
                                )
                            ),
                            onBackPressed = {
                                call.leave()
                                finish()
                            },
                            onAcceptedContent = {
                                CallScreen(
                                    call = call,
                                    showDebugOptions = BuildConfig.DEBUG,
                                    onCallDisconnected = {
                                        finish()
                                    },
                                    onUserLeaveCall = {
                                        call.leave()
                                        finish()
                                    },
                                )
                            },
                            onRejectedContent = {
                                LaunchedEffect(key1 = call) {
                                    call.reject()
                                    finish()
                                }
                            },
                        )
                    }
                }
            }
        }
    }

    private fun handleCallAnswered(call: Call) {
        lifecycleScope.launch {
            call.accept()
            call.join()
            showOngoingCall = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(callStateReceiver)
    }

    private fun showWhenLockedAndTurnScreenOn() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            @Suppress("DEPRECATION") window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun EnsureVideoCallPermissions(onPermissionsGranted: () -> Unit) {
    val permissionsState = rememberMultiplePermissionsState(permissions = buildList {
        // Access to microphone
        add(Manifest.permission.CAMERA)
        add(Manifest.permission.RECORD_AUDIO)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            add(Manifest.permission.FOREGROUND_SERVICE)
        }
    })
    LaunchedEffect(key1 = Unit) {
        permissionsState.launchMultiplePermissionRequest()
    }
    LaunchedEffect(key1 = permissionsState.allPermissionsGranted) {
        if (permissionsState.allPermissionsGranted) {
            onPermissionsGranted()
        }
    }
}