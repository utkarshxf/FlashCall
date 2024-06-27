package com.example.myapplication.myapplication.flashcall.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.Data.VideoCallRoute
import com.example.myapplication.myapplication.flashcall.ViewModel.VideoCallViewModel
import io.getstream.video.android.compose.theme.VideoTheme
import io.getstream.video.android.compose.ui.components.call.controls.actions.AcceptCallAction
import io.getstream.video.android.compose.ui.components.call.controls.actions.LeaveCallAction
import io.getstream.video.android.compose.ui.components.call.ringing.RingingCallContent
import io.getstream.video.android.core.Call

@Composable
fun IncomingCallScreen(
    call: Call,
    navController: NavController
){
    CompositionLocalProvider(
        androidx.lifecycle.compose.LocalLifecycleOwner provides androidx.compose.ui.platform.LocalLifecycleOwner.current,
    ) {
        VideoTheme {
            RingingCallContent(
                call = call,
                controlsContent = {
                    Box(modifier = Modifier.fillMaxWidth()
                        .padding(32.dp)
                        .align(Alignment.BottomCenter)){
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .align(Alignment.Center)
                                .size(52.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AcceptCallAction(
                                modifier = Modifier.size(52.dp),
                                bgColor = Color.Green,
                                onCallAction = {
//                                          videoCallViewModel.joinCall()
                                    navController.navigate(VideoCallRoute.VideoCall.route)
                                }
                            )
                            LeaveCallAction(
                                modifier = Modifier.size(52.dp),
                                onCallAction = {
                                    navController.navigate(ScreenRoutes.MainScreen.route)
                                }
                            )
                        }
                    }
                },
                onAcceptedContent = {
                    navController.navigate(VideoCallRoute.VideoCall.route) },
                onNoAnswerContent = {
                    navController.navigate(ScreenRoutes.MainScreen.route)
                }
            )
        }
    }
}