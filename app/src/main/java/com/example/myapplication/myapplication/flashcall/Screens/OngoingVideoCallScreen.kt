package com.example.myapplication.myapplication.flashcall.Screens

import android.Manifest
import android.os.Build
import android.util.Log
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import io.getstream.video.android.core.Call
import kotlinx.coroutines.Delay
import kotlinx.coroutines.delay

@Composable
fun OngoingVideoCallScreen(
    videoCall : Boolean,
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
                videoCall = true,
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
    videoCall : Boolean,
    viewModel: VideoCallViewModel,
    navController:NavController
){
    val isCameraEnabled by call.camera.isEnabled.collectAsStateWithLifecycle(lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current)
    val isMicrophoneEnabled by call.microphone.isEnabled.collectAsStateWithLifecycle(lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current)
    val isSpeakerphoneEnabled by call.speaker.isEnabled.collectAsStateWithLifecycle(lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current)
    val activeVideoCall = viewModel.state.activeCall
    val timeLeft = viewModel.timeLeft

    // Listen to call status changes and end call if necessary
    LaunchedEffect(call) {
        if (!videoCall) {
            call.camera.setEnabled(false)
            call.microphone.setEnabled(false)
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
    LaunchedEffect(Unit) {
        viewModel.observeTimeLeft(call.id)
    }
    CompositionLocalProvider(
        androidx.lifecycle.compose.LocalLifecycleOwner provides androidx.compose.ui.platform.LocalLifecycleOwner.current,
    ){
        VideoTheme {
            Box(
                modifier = Modifier.fillMaxSize()
            ){
                CallContent(
                    call = call,
                    enableInPictureInPicture = true,
                    controlsContent = {
                        if(videoCall) {
                            Box(modifier = Modifier.fillMaxWidth(),
                            ) {
                                ControlActions(
                                    call = call,
                                    actions = listOf {
                                        Row(
                                            modifier = Modifier.width(350.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
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
                                                    Log.v("LeaveCallAction1" , "call")
                                                    viewModel.endCall{
                                                        viewModel.resetCallState()
                                                    }
                                                }
                                            )
                                        }
                                    }
                                )
                            }
                        } else {
                            ControlActions(
                                call = call,
                                actions = listOf(
                                    {
                                        ToggleMicrophoneAction(
                                            modifier = Modifier.size(52.dp),
                                            isMicrophoneEnabled = isMicrophoneEnabled,
                                            onCallAction = { call.microphone.setEnabled(it.isEnabled) }
                                        )
                                    },
                                    {
                                        LeaveCallAction(
                                            modifier = Modifier.size(52.dp),
                                            onCallAction = {
                                                viewModel.endCall{
                                                    viewModel.resetCallState()
                                                }
                                            }
                                        )
                                    }, {
                                        ToggleSpeakerphoneAction(
                                            modifier = Modifier.size(52.dp),
                                            isSpeakerphoneEnabled = isSpeakerphoneEnabled ){
                                                onCallAction -> call.speaker.setEnabled(onCallAction.isEnabled)
                                        }
                                    }
                                )
                            )
                        }
                    }
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .background(Color.White.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                ) {
                    Text(
                        text = "Time Left: ${formatTime(timeLeft)}",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
        }
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