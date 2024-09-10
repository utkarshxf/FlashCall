package com.example.myapplication.myapplication.flashcall.Screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.Data.VideoCallRoute
import com.example.myapplication.myapplication.flashcall.Data.model.SDKResponseState
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
    videoCallViewModel: VideoCallViewModel
) {
    CompositionLocalProvider(
        androidx.lifecycle.compose.LocalLifecycleOwner provides androidx.compose.ui.platform.LocalLifecycleOwner.current,
    ) {
        val state = videoCallViewModel.videoMutableUiState.collectAsStateWithLifecycle(lifecycleOwner = LocalLifecycleOwner.current)
        if(state.value == SDKResponseState.Loading)
        {
            VideoTheme {
                RingingCallContent(
                    call = call,
                    controlsContent = { CallControls(call, navController, videoCallViewModel)},
                    onAcceptedContent = { navigateToOngoingCall(navController) },
                    onNoAnswerContent = { navigateToMainScreen(navController) },
                    onRejectedContent = { navigateToMainScreen(navController) },
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
    videoCallViewModel: VideoCallViewModel
) {
    Box(
        modifier = Modifier.fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AcceptCallAction(
                modifier = Modifier.size(52.dp),
                bgColor = Color.Green,
                onCallAction = { navigateToOngoingCall(navController) }
            )
            LeaveCallAction(
                modifier = Modifier.size(52.dp),
                onCallAction = {
                    leaveCall(call, videoCallViewModel, navController)
                }
            )
        }
    }
}

private fun navigateToOngoingCall(navController: NavController) {
    navController.navigate(VideoCallRoute.OngoingVideoCall.videoCallRoute) {
        popUpTo(ScreenRoutes.IncomingVideoCallScreen.route) { inclusive = true }
    }
}

private fun navigateToMainScreen(navController: NavController) {
    navController.navigate(ScreenRoutes.MainScreen.route) {
        popUpTo(ScreenRoutes.IncomingVideoCallScreen.route) { inclusive = true }
    }
}

private fun leaveCall(call: Call, videoCallViewModel: VideoCallViewModel, navController: NavController) {
    try {
        videoCallViewModel.leaveCall(call) {
            navigateToMainScreen(navController)
        }
    } catch (e: Exception) {
        Log.e("NavigationError", "Error navigating: ${e.message}")
    }
}




