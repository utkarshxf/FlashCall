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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.video.android.compose.theme.VideoTheme
import io.getstream.video.android.core.BuildConfig
import io.getstream.video.android.core.Call
import io.getstream.video.android.core.StreamVideo
import io.getstream.video.android.core.notifications.NotificationHandler
import io.getstream.video.android.model.streamCallId
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class IncomingCallActivity : ComponentActivity() {
    private val callStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                "ACTION_CALL_ENDED" -> {
                    finish()
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showWhenLockedAndTurnScreenOn()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val callId = intent.streamCallId(NotificationHandler.INTENT_EXTRA_CALL_CID)
        if (callId == null) {
            finish()
            return
        }
        lifecycleScope.launch {
//            val streamVideo: StreamVideo = StreamVideo.instance()
//            streamVideo.state.ringingCall.collectLatest { incomingCall ->
//                if (incomingCall == null) {
//                    finish()
//                    return@collectLatest
//                }
//            }
            registerReceiver(callStateReceiver, IntentFilter("ACTION_CALL_ENDED"))
            val call = StreamVideo.instance().call(callId.type, callId.id)
            setContent {
                CompositionLocalProvider(
                    androidx.lifecycle.compose.LocalLifecycleOwner provides androidx.compose.ui.platform.LocalLifecycleOwner.current,
                ) {
                    VideoTheme {
                        var showOngoingCall by remember { mutableStateOf(false) }

                        LaunchedEffect(key1 = Unit) {
                            when (intent.action) {
                                NotificationHandler.ACTION_ACCEPT_CALL -> {
                                    if (savedInstanceState == null) {
                                        call.accept()
                                        call.join()
                                        showOngoingCall = true
                                    }
                                }

                                NotificationHandler.ACTION_REJECT_CALL -> {
                                    if (savedInstanceState == null) {
                                        call.reject()
                                        finish()
                                    }
                                }
                            }
                        }

                        if (!showOngoingCall) {
                            IncomingCallScreen(call = call,
                                endActivity = { finish() },
                                onAcceptCall = {
                                    lifecycleScope.launch {
                                        call.accept()
                                        call.join()
                                        showOngoingCall = true
                                    }
                                })
                        } else {
                            CallScreen(call = call,
                                showDebugOptions = BuildConfig.DEBUG,
                                onCallDisconnected = { finish() },
                                onUserLeaveCall = {
                                    call.leave()
                                    finish()
                                }
                            )
                        }
                    }
                }
            }
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

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun EnsureVideoCallPermissions(onPermissionsGranted: () -> Unit) {
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
}