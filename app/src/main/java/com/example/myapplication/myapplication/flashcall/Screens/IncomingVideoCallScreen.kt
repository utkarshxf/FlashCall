package com.example.myapplication.myapplication.flashcall.Screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.Data.VideoCallRoute
import com.example.myapplication.myapplication.flashcall.ViewModel.VideoCallViewModel
import io.getstream.video.android.compose.theme.VideoTheme
import io.getstream.video.android.compose.ui.components.call.controls.actions.AcceptCallAction
import io.getstream.video.android.compose.ui.components.call.controls.actions.LeaveCallAction
import io.getstream.video.android.compose.ui.components.call.ringing.RingingCallContent
import io.getstream.video.android.core.Call
import io.getstream.video.android.core.RingingState
import kotlinx.coroutines.flow.collectLatest

@Composable
fun IncomingVideoCallScreen(
    call: Call,
    navController: NavController,
    videoCallViewModel: VideoCallViewModel = hiltViewModel()
) {
//    LaunchedEffect(Unit) {
//        videoCallViewModel.incomingCall.collectLatest { call ->
//            if (call == null) {
//                navController.navigate(ScreenRoutes.MainScreen.route) {
//                    popUpTo(ScreenRoutes.IncomingVideoCallScreen.route) {
//                        inclusive = true
//                    }
//                }
//            }
//        }
//    }
    CompositionLocalProvider(
        androidx.lifecycle.compose.LocalLifecycleOwner provides androidx.compose.ui.platform.LocalLifecycleOwner.current,
    ) {
        VideoTheme {
            RingingCallContent(
                call = call,
                controlsContent = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp)
                            .align(Alignment.BottomCenter)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .align(Alignment.Center)
                                .size(52.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AcceptCallAction(modifier = Modifier.size(52.dp),
                                bgColor = Color.Green,
                                onCallAction = {
                                    navController.navigate(VideoCallRoute.OngoingVideoCall.videoCallRoute)
                                    {
                                        popUpTo(ScreenRoutes.IncomingVideoCallScreen.route) {
                                            inclusive = true
                                        }
                                    }
                                })
                            LeaveCallAction(modifier = Modifier.size(52.dp), onCallAction = {
                                try {
                                    videoCallViewModel.leaveCall(call) {
                                        navController.navigate(ScreenRoutes.MainScreen.route) {
                                            popUpTo(ScreenRoutes.IncomingVideoCallScreen.route) {
                                                inclusive = true
                                            }
                                        }
                                    }
                                } catch (e: Exception) {
                                    Log.e("NavigationError", "Error navigating: ${e.message}")
                                }
                            })
                        }
                    }
                },
                onAcceptedContent = {
                    navController.navigate(VideoCallRoute.OngoingVideoCall.videoCallRoute) {
                        popUpTo(ScreenRoutes.IncomingVideoCallScreen.route) {
                            inclusive = true
                        }
                    }
                },
                onNoAnswerContent = {
                    navController.navigate(ScreenRoutes.MainScreen.route){
                        popUpTo(ScreenRoutes.IncomingVideoCallScreen.route) {
                            inclusive = true
                        }
                    }
                },
                onRejectedContent = {
                    navController.navigate(ScreenRoutes.MainScreen.route){
                        popUpTo(ScreenRoutes.IncomingVideoCallScreen.route) {
                            inclusive = true
                        }
                    }
                },
                isVideoType = true,
            )
        }
    }
}






