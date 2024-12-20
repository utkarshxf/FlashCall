package com.example.myapplication.myapplication.flashcall.Screens.callServices

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.video.android.compose.theme.VideoTheme
import io.getstream.video.android.core.BuildConfig
import io.getstream.video.android.core.StreamVideo
import io.getstream.video.android.core.notifications.NotificationHandler
import io.getstream.video.android.model.streamCallId
import kotlinx.coroutines.launch

@AndroidEntryPoint
class IncomingCallActivity : ComponentActivity() {
    private val callStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {

                "FINISH_ACTIVITY" -> {
                    Log.d("DeclineCallReceiver", "is this method called from notification")
                    finishAndRemoveTask()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showWhenLockedAndTurnScreenOn()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val callId = intent.streamCallId(NotificationHandler.INTENT_EXTRA_CALL_CID)
        val isAccepted = intent.getBooleanExtra("call_accepted", false)

        if (callId == null) {
            finishAndRemoveTask()
            return
        }

        lifecycleScope.launch {
            registerReceiver(callStateReceiver, IntentFilter("ACTION_CALL_ENDED"))
            val call = StreamVideo.instance().call(callId.type, callId.id)
            setContent {
                CompositionLocalProvider(
                    androidx.lifecycle.compose.LocalLifecycleOwner provides androidx.compose.ui.platform.LocalLifecycleOwner.current,
                ) {
                    VideoTheme {
                        var showOngoingCall by remember { mutableStateOf(false) }

                        var permissionsGranted by remember { mutableStateOf(false) }

                        EnsureVideoCallPermissions {
                            permissionsGranted = true
                        }

                        if(isAccepted && permissionsGranted){
                            LaunchedEffect(key1 = Unit) {
                                showOngoingCall = true
                                call.join()
                            }
                        }

                        if (permissionsGranted) {
                            LaunchedEffect(key1 = Unit) {
                                when (intent.action) {
                                    NotificationHandler.ACTION_ACCEPT_CALL -> {
                                        if (savedInstanceState == null) {
                                            call.join()
                                            showOngoingCall = true
                                        }
                                    }

                                    NotificationHandler.ACTION_REJECT_CALL -> {
                                        if (savedInstanceState == null) {
                                            call.reject()
                                            finishAndRemoveTask()
                                        }
                                    }
                                }
                            }
                            if (!showOngoingCall) {
                                IncomingCallScreen(call = call, endActivity = {
                                    lifecycleScope.launch {
                                        call.reject()
                                        call.leave()
                                        showOngoingCall = false
                                    }
                                    NotificationManagerCompat.from(this@IncomingCallActivity).cancel(callId.hashCode())
                                    finishAndRemoveTask()
                                }, onAcceptCall = {
                                    lifecycleScope.launch {
                                        call.join()
                                        showOngoingCall = true
                                    }
                                })
                            } else {
                                CallScreen(call = call,
                                    showDebugOptions = BuildConfig.DEBUG,
                                    onCallDisconnected = {
                                        call.leave()
                                        finishAndRemoveTask()
                                    },
                                    onUserLeaveCall = {
                                        call.leave()
                                        finishAndRemoveTask()
                                    })
                            }
                        } else {
                            Text("Permissions are required to start the video call.")
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