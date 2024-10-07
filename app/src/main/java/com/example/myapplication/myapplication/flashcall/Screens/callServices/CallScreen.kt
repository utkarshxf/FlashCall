package com.example.myapplication.myapplication.flashcall.Screens.callServices

import android.content.Context
import android.media.AudioManager
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import io.getstream.video.android.compose.theme.VideoTheme
import io.getstream.video.android.compose.ui.components.call.activecall.CallContent
import io.getstream.video.android.compose.ui.components.call.controls.ControlActions
import io.getstream.video.android.compose.ui.components.call.controls.actions.FlipCameraAction
import io.getstream.video.android.compose.ui.components.call.controls.actions.LeaveCallAction
import io.getstream.video.android.compose.ui.components.call.controls.actions.ToggleCameraAction
import io.getstream.video.android.compose.ui.components.call.controls.actions.ToggleMicrophoneAction
import io.getstream.video.android.compose.ui.components.call.controls.actions.ToggleSpeakerphoneAction
import io.getstream.video.android.core.Call
import io.getstream.video.android.core.RealtimeConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CallScreen(
    call: Call,
    showDebugOptions: Boolean = false,
    onCallDisconnected: () -> Unit = {},
    onUserLeaveCall: () -> Unit = {},
) {
    val context = LocalContext.current
    val isCameraEnabled by call.camera.isEnabled.collectAsStateWithLifecycle()
    val isMicrophoneEnabled by call.microphone.isEnabled.collectAsStateWithLifecycle()
    val speakingWhileMuted by call.state.speakingWhileMuted.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val messageScope = rememberCoroutineScope()
    val db = FirebaseFirestore.getInstance()

    val connection by call.state.connection.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = connection) {
        if (connection == RealtimeConnection.Disconnected) {
            onCallDisconnected.invoke()
        } else if (connection is RealtimeConnection.Failed) {
            Toast.makeText(
                context,
                "Call connection failed (${(connection as RealtimeConnection.Failed).error}",
                Toast.LENGTH_LONG,
            ).show()
            onCallDisconnected.invoke()
        }
    }
    VideoCallContent(call , endActivity = {
        onUserLeaveCall()
    })

}
@Composable
fun VideoCallContent(
    call: Call,
    endActivity: () -> Unit
) {
    var timeLeft by remember { mutableStateOf(0.0) }
    val videoCall = call.type == "default"
    val me by call.state.me.collectAsState()
    var parentSize: IntSize by remember { mutableStateOf(IntSize(0, 0)) }
    val isCameraEnabled by call.camera.isEnabled.collectAsStateWithLifecycle()
    val isMicrophoneEnabled by call.microphone.isEnabled.collectAsStateWithLifecycle()
    var isMainSpeakerEnabled by remember { mutableStateOf(false) }
    val isSpeakerphoneEnabled by call.speaker.isEnabled.collectAsStateWithLifecycle()
    var showEndCallConfirmation by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val audioManager = remember { context.getSystemService(Context.AUDIO_SERVICE) as AudioManager }
    LaunchedEffect(Unit) {
        if (call.type != "default") {
            call.camera.setEnabled(false)
            call.speaker.setEnabled(false)
        } else {
            call.speaker.setEnabled(true)
            call.camera.setEnabled(true)
        }
    }
    fun observeTimeLeft(callId: String , lifecycleScope: CoroutineScope) {
        lifecycleScope.launch {
            val documentRef = FirebaseFirestore.getInstance()
                .collection("calls")
                .document(callId)

            documentRef.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("Firestore", "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val newTimeLeft = snapshot.getDouble("timeLeft")
                    newTimeLeft?.let {
                        timeLeft = it
                    }
                }
            }
        }
    }
    LaunchedEffect(Unit) {
        observeTimeLeft(call.id , scope)
    }
    CompositionLocalProvider(
        androidx.lifecycle.compose.LocalLifecycleOwner provides androidx.compose.ui.platform.LocalLifecycleOwner.current,
    ) {
        VideoTheme {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                CallContent(
                    appBarContent = {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(16.dp)
                                .background(
                                    Color.White.copy(alpha = 0.5f),
                                    RoundedCornerShape(16.dp)
                                )
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(text = formatTimeLeft(timeLeft))
                        }
                    },
                    call = call,
                    enableInPictureInPicture = false,
                    controlsContent = {
                        if (videoCall) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 20.dp),
                            ) {
                                ControlActions(
                                    call = call,
                                    actions = listOf {
                                        Row(
                                            modifier = Modifier.width(350.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            ToggleCameraAction(
                                                modifier = Modifier
                                                    .size(52.dp)
                                                    .padding(start = 10.dp),
                                                isCameraEnabled = isCameraEnabled,
                                                onCallAction = { call.camera.setEnabled(it.isEnabled) }
                                            )
                                            FlipCameraAction(
                                                modifier = Modifier.size(52.dp),
                                                onCallAction = { call.camera.flip() }
                                            )
                                            ToggleMicrophoneAction(
                                                modifier = Modifier.size(52.dp),
                                                isMicrophoneEnabled = isMicrophoneEnabled,
                                                onCallAction = { call.microphone.setEnabled(it.isEnabled) }
                                            )
                                            LeaveCallAction(
                                                modifier = Modifier.size(52.dp),
                                                onCallAction = {
                                                    showEndCallConfirmation = true
                                                }
                                            )
                                        }
                                    }
                                )
                            }
                        } else {
                            ControlActions(
                                call = call,
                                actions = listOf {
                                    Row(
                                        modifier = Modifier.width(350.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        ToggleSpeakerphoneAction(
                                            isSpeakerphoneEnabled = isSpeakerphoneEnabled,
                                            modifier = Modifier
                                                .size(52.dp)
                                                .padding(start = 10.dp),
                                            onCallAction = { call.speaker.setEnabled(it.isEnabled) }
                                        )
                                        ToggleMicrophoneAction(
                                            modifier = Modifier
                                                .size(52.dp)
                                                .padding(horizontal = 20.dp),
                                            isMicrophoneEnabled = isMicrophoneEnabled,
                                            onCallAction = { call.microphone.setEnabled(it.isEnabled) }
                                        )
                                        LeaveCallAction(
                                            modifier = Modifier.size(52.dp),
                                            onCallAction = {
                                                showEndCallConfirmation = true
                                            }
                                        )
                                    }
                                }
                            )
                        }
                    },
                )
            }
        }
    }

    if (showEndCallConfirmation) {
        AlertDialog(
            onDismissRequest = { showEndCallConfirmation = false },
            title = { Text("Are you sure?") },
            text = { Text("Proceeding further will End the Ongoing Call.") },
            confirmButton = {
                Button(
                    onClick = {
                        showEndCallConfirmation = false
                        call.leave()
                        endActivity()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Proceed")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showEndCallConfirmation = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
fun formatTimeLeft(timeLeftInSeconds: Double): String {
    val minutes = (timeLeftInSeconds / 60).toInt()
    val seconds = (timeLeftInSeconds % 60).toInt().toString().padStart(2, '0')
    return "time left: $minutes:$seconds"
}



//@Composable
//private fun SpeakingWhileMuted() {
//    Snackbar {
//        Text(text = "You're talking while muting the microphone!")
//    }
//}