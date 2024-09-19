package com.example.myapplication.myapplication.flashcall.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.Data.VideoCallRoute
import com.example.myapplication.myapplication.flashcall.Data.model.SDKResponseState
import com.example.myapplication.myapplication.flashcall.ViewModel.VideoCallViewModel
import io.getstream.video.android.compose.theme.VideoTheme
import io.getstream.video.android.compose.ui.components.call.controls.actions.AcceptCallAction
import io.getstream.video.android.compose.ui.components.call.controls.actions.LeaveCallAction
import io.getstream.video.android.compose.ui.components.call.ringing.RingingCallContent
import io.getstream.video.android.core.Call

@Composable
fun IncomingVideoCallScreen(
    call: Call,
    navController: NavController,
    videoCallViewModel: VideoCallViewModel,
) {
    CompositionLocalProvider(
        androidx.lifecycle.compose.LocalLifecycleOwner provides androidx.compose.ui.platform.LocalLifecycleOwner.current,
    ) {
        val state = videoCallViewModel.state.sdkResponseState
        if (state == SDKResponseState.Loading) {
            VideoTheme {
                RingingCallContent(
                    call = call,
                    controlsContent = { CallControls(call, navController, videoCallViewModel) },
                    onAcceptedContent = {
                        navController.navigate(VideoCallRoute.OngoingVideoCall.videoCallRoute) {
                            popUpTo(ScreenRoutes.IncomingCallScreen.route) { inclusive = true }
                        }
                    },
                    onNoAnswerContent = {
                        videoCallViewModel.rejectCall()
                        {
                            videoCallViewModel.resetCallState()
                            navController.navigate(ScreenRoutes.MainScreen.route) {
                                popUpTo(ScreenRoutes.IncomingCallScreen.route) { inclusive = true }
                            }
                        }
                    },
                    onRejectedContent = {
                        videoCallViewModel.rejectCall()
                        {
                            videoCallViewModel.resetCallState()
                            navController.navigate(ScreenRoutes.MainScreen.route) {
                                popUpTo(ScreenRoutes.IncomingCallScreen.route) { inclusive = true }
                            }
                        }
                    },
                    isVideoType = true,
                )
            }
        }
    }
}

@Composable
private fun CallControls(
    call: Call,
    navController: NavController,
    videoCallViewModel: VideoCallViewModel,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp), contentAlignment = Alignment.BottomEnd
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AcceptCallAction(modifier = Modifier.size(52.dp),
                bgColor = Color.Green,
                onCallAction = {
                    navController.navigate(VideoCallRoute.OngoingVideoCall.videoCallRoute) {
                        popUpTo(ScreenRoutes.IncomingCallScreen.route) { inclusive = true }
                    }
                })
            LeaveCallAction(modifier = Modifier.size(52.dp), onCallAction = {
                videoCallViewModel.rejectCall {
                    navController.navigate(ScreenRoutes.MainScreen.route) {
                        popUpTo(ScreenRoutes.IncomingCallScreen.route) { inclusive = true }
                    }
                }
            })
        }
    }
}


@Preview(showBackground = true)
@Composable
fun VideoCallPreview(){
    //IncomingVideoCallScreen(call = hiltViewModel(), navController = rememberNavController(), videoCallViewModel = hiltViewModel() )
}


