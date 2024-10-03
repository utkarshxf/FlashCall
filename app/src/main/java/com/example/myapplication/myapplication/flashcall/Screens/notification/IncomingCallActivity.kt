package com.example.myapplication.myapplication.flashcall.Screens.notification

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.Data.VideoCallRoute
import com.example.myapplication.myapplication.flashcall.Screens.IncomingCallScreen
import com.example.myapplication.myapplication.flashcall.Screens.OngoingVideoCallScreen
import com.example.myapplication.myapplication.flashcall.ViewModel.VideoCallViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.video.android.core.StreamVideo
import io.getstream.video.android.core.notifications.NotificationHandler
import io.getstream.video.android.model.streamCallId
import kotlinx.coroutines.launch

@AndroidEntryPoint
class IncomingCallActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showWhenLockedAndTurnScreenOn()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        val callId = intent.streamCallId(NotificationHandler.INTENT_EXTRA_CALL_CID)!!
        if (intent.action == "FINISH_ACTIVITY") {
            finish()
            return
        }
        lifecycleScope.launch {
            val call = StreamVideo.instance().call(callId.type, callId.id)
            setContent {
                val viewModel: VideoCallViewModel = hiltViewModel()
                val callNavController = rememberNavController()
                NavHost(
                    navController = callNavController,
                    startDestination = ScreenRoutes.IncomingCallScreen.route
                ) {
                    composable(route = ScreenRoutes.IncomingCallScreen.route) {
                        IncomingCallScreen(
                            call = call,
                            videoCallViewModel = viewModel,
                            navController = callNavController,
                            endActivity = { finish() }
                        )
                    }
                    composable(route = VideoCallRoute.OngoingVideoCall.videoCallRoute) {
                        OngoingVideoCallScreen(
                            call = call,
                            viewModel = viewModel,
                            navController = callNavController
                        )
                    }
                }
            }
        }
    }

    private fun showWhenLockedAndTurnScreenOn() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            @Suppress("DEPRECATION") window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
            )
        }
    }
}