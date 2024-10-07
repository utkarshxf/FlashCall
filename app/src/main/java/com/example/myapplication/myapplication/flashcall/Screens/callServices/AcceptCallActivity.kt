package com.example.myapplication.myapplication.flashcall.Screens.callServices

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
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
            var isPermissionGiven = true
            val call = StreamVideo.instance().call(callId.type, callId.id)
            Log.e("AcceptCallActivity123" , call.state.createdBy.value?.id.toString())
            Log.e("AcceptCallActivity123" , call.state.participants.value.toString())
            Log.e("AcceptCallActivity123" , call.state.endedByUser.value.toString())
            Log.e("AcceptCallActivity123" , call.state.connection.value.toString())
            Log.e("AcceptCallActivity123" , call.state.endedAt.value.toString())
            Log.e("AcceptCallActivity123" , call.state.toString())
            setContent {
                CompositionLocalProvider(
                    androidx.lifecycle.compose.LocalLifecycleOwner provides androidx.compose.ui.platform.LocalLifecycleOwner.current,
                ) {
                    EnsureVideoCallPermissions{
                        isPermissionGiven = true
                    }
                    if(isPermissionGiven)
                    {
                        lifecycleScope.launch {
                            call.accept()
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