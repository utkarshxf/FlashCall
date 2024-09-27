package com.example.myapplication.myapplication.flashcall.Screens

import android.Manifest
import android.content.Context
import android.media.AudioManager
import android.os.Build
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.Data.model.SDKResponseState
import com.example.myapplication.myapplication.flashcall.ViewModel.VideoCallViewModel
import com.example.myapplication.myapplication.flashcall.ui.theme.MainColor
import com.example.myapplication.myapplication.flashcall.utils.LoadingIndicator
import com.example.myapplication.myapplication.flashcall.utils.formatTime
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import io.getstream.video.android.compose.theme.VideoTheme
import io.getstream.video.android.compose.ui.components.call.activecall.CallContent
import io.getstream.video.android.compose.ui.components.call.controls.ControlActions
import io.getstream.video.android.compose.ui.components.call.controls.actions.LeaveCallAction
import io.getstream.video.android.compose.ui.components.call.controls.actions.ToggleCameraAction
import io.getstream.video.android.compose.ui.components.call.controls.actions.ToggleMicrophoneAction
import io.getstream.video.android.compose.ui.components.call.controls.actions.ToggleSpeakerphoneAction
import io.getstream.video.android.compose.ui.components.call.renderer.FloatingParticipantVideo
import io.getstream.video.android.compose.ui.components.call.renderer.ParticipantVideo
import io.getstream.video.android.compose.ui.components.call.renderer.RegularVideoRendererStyle
import io.getstream.video.android.compose.ui.components.call.renderer.VideoRendererStyle
import io.getstream.video.android.compose.ui.components.video.VideoRenderer
import io.getstream.video.android.core.Call
import io.getstream.video.android.core.ParticipantState
import io.getstream.video.android.core.call.state.ToggleSpeakerphone
import kotlinx.coroutines.Delay
import kotlinx.coroutines.delay

@Composable
fun OngoingVideoCallScreen(
    viewModel: VideoCallViewModel,
    navController:NavController
){
    val uiState = viewModel.state.sdkResponseState
    EnsureVideoCallPermissions {
        viewModel.joinCall()
    }
    when(uiState){
        is SDKResponseState.Success -> {
            Log.d("VideoCall", "VideoCall:${(uiState as SDKResponseState.Success).data} ")
            VideoCallContent(
                call =  (uiState as SDKResponseState.Success).data,
                viewModel = viewModel,
                navController = navController
            )
        }

        is SDKResponseState.Error -> {
            VideoCallError(viewModel , navController)
        }
        is SDKResponseState.Loading -> {
            VideoCallLoading()
        }
    }
}

@Composable
fun VideoCallContent(
    call : Call,
    viewModel: VideoCallViewModel,
    navController:NavController
){
    val videoCall = call.type == "default"
    val me by call.state.me.collectAsState()
    var parentSize: IntSize by remember { mutableStateOf(IntSize(0, 0)) }

    val isCameraEnabled by call.camera.isEnabled.collectAsStateWithLifecycle(lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current)
    val isMicrophoneEnabled by call.microphone.isEnabled.collectAsStateWithLifecycle(lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current)
    var isMainSpeakerEnabled by remember { mutableStateOf(false) }
    val isSpeakerphoneEnabled by call.speaker.isEnabled.collectAsStateWithLifecycle(lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current)
    val activeVideoCall = viewModel.state.activeCall
    val timeLeft = viewModel.timeLeft
    var showEndCallConfirmation by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val audioManager = remember { context.getSystemService(Context.AUDIO_SERVICE) as AudioManager }

    // Listen to call status changes and end call if necessary
    LaunchedEffect(call) {
        viewModel.observeTimeLeft(call.id)
        if (call.type != "default") {
            call.camera.setEnabled(false)
            call.speaker.setEnabled(false)
        }else{
            call.speaker.setEnabled(true)
            call.camera.setEnabled(true)
        }
    }
    LaunchedEffect(activeVideoCall) {
        if(activeVideoCall==null)
        {
            viewModel.resetCallState()
            navController.navigate(ScreenRoutes.MainScreen.route){
                popUpTo(ScreenRoutes.InCallScreen.route) { inclusive = true }
            }
        }
    }
    CompositionLocalProvider(
        androidx.lifecycle.compose.LocalLifecycleOwner provides androidx.compose.ui.platform.LocalLifecycleOwner.current,
    ){
        VideoTheme {
            Box(
                modifier = Modifier.fillMaxSize()
            ){
                CallContent(
                    appBarContent = {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(16.dp)
                                .background(Color.White.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                                .padding(horizontal = 16.dp, vertical = 8.dp)

                        ) {
                            Text(
                                text = "Time Left: ${formatTime(timeLeft)}",
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        }
                    },
                    call = call,
                    enableInPictureInPicture = false,
                    controlsContent = {
                        if(videoCall) {
                            Box(modifier = Modifier
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
                                                onCallAction = { viewModel.toggleCamera(it.isEnabled) }
                                            )
                                            ToggleMicrophoneAction(
                                                modifier = Modifier
                                                    .size(52.dp)
                                                    .padding(horizontal = 20.dp),
                                                isMicrophoneEnabled = isMicrophoneEnabled,
                                                onCallAction = {viewModel.toggleMicrophone(it.isEnabled)}
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
                                        ToggleSpeakerphoneAction(isSpeakerphoneEnabled = isSpeakerphoneEnabled,
                                            modifier = Modifier
                                                .size(52.dp)
                                                .padding(start = 10.dp),
                                            onCallAction = { viewModel.toggleSpeaker(it.isEnabled) }
                                        )
                                        ToggleMicrophoneAction(
                                            modifier = Modifier
                                                .size(52.dp)
                                                .padding(horizontal = 20.dp),
                                            isMicrophoneEnabled = isMicrophoneEnabled,
                                            onCallAction = { viewModel.toggleMicrophone(it.isEnabled) }
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
                        viewModel.endCall {
                            viewModel.resetCallState()
                            navController.navigate(ScreenRoutes.MainScreen.route) {
                                popUpTo(ScreenRoutes.InCallScreen.route) { inclusive = true }
                            }
                        }
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
@Composable
fun SwitchSpeakerAction(
    modifier: Modifier = Modifier,
    isMainSpeakerEnabled: Boolean,
    onCallAction: () -> Unit
) {
    IconButton(
        onClick = onCallAction,
        modifier = modifier
    ) {
        Icon(
            imageVector = if (isMainSpeakerEnabled) Icons.Filled.VolumeUp else Icons.Filled.Phone,
            contentDescription = if (isMainSpeakerEnabled) "Switch to earpiece" else "Switch to main speaker",
            tint = Color.White
        )
    }
}

@Composable
fun VideoCallError(viewModel: VideoCallViewModel,navController:NavController)
{
    LaunchedEffect(Unit) {
        delay(1000)
        Log.e("VideoCallError" , "VideoCallError")
        viewModel.resetCallState()
        navController.navigate(ScreenRoutes.MainScreen.route){
            popUpTo(ScreenRoutes.InCallScreen.route) { inclusive = true }
        }
    }
    Box(modifier = Modifier.fillMaxSize())
    {
        Text(
            modifier = Modifier.align(Alignment.Center),
            fontSize = 16.sp,
            style = MaterialTheme.typography.bodyMedium,
            color = MainColor,
            text = "Something went wrong; failed to join a call"
        )
    }
}

@Composable
private fun VideoCallLoading() {
    Box(modifier = Modifier.fillMaxSize()) {
        LoadingIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun EnsureVideoCallPermissions(onPermissionsGranted: () -> Unit) {
    val permissionsState = rememberMultiplePermissionsState(
        permissions = buildList {
            // Access to microphone
            add(Manifest.permission.CAMERA)
            add(Manifest.permission.RECORD_AUDIO)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                add(Manifest.permission.FOREGROUND_SERVICE)
            }
        }
    )
    LaunchedEffect(key1 = Unit) {
        permissionsState.launchMultiplePermissionRequest()
    }
    LaunchedEffect(key1 = permissionsState.allPermissionsGranted) {
        if (permissionsState.allPermissionsGranted) {
            onPermissionsGranted()
        }
    }
}