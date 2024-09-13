package com.example.myapplication.myapplication.flashcall.Screens

import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import co.hyperverge.hyperkyc.data.models.HyperKycConfig
import com.example.myapplication.myapplication.flashcall.BaseClass
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.ViewModel.AuthenticationViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.RegistrationViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.VideoCallViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.chats.ChatRequestViewModel
import com.example.myapplication.myapplication.flashcall.bottomnav.BottomBar
import com.example.myapplication.myapplication.flashcall.bottomnav.BottomNavGraph
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MainScreen(
    navController: NavController,
    hyperKycLauncher: ActivityResultLauncher<HyperKycConfig>,
    registrationViewModel: RegistrationViewModel,
    chatRequestViewModel: ChatRequestViewModel = hiltViewModel(),
    authenticationViewModel: AuthenticationViewModel = hiltViewModel(),
    videoCallViewModel: VideoCallViewModel = hiltViewModel()
) {
    val homeNavController = rememberNavController()
    val chatRequestCreated by chatRequestViewModel.chatRequestCreated.collectAsState()
    val incomingCall = videoCallViewModel.state.incomingCall
    val activeVideoCall = videoCallViewModel.state.activeCall
    val context = LocalContext.current

    incomingCall?.let {
        homeNavController.navigate(ScreenRoutes.IncomingVideoCallScreen.route)
    }
    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
        if (!chatRequestCreated && incomingCall == null && activeVideoCall == null) {
            BottomBar(navController = homeNavController)
        }
    }) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            val uid = authenticationViewModel.getUserFromPreferences(context)
            chatRequestViewModel.listenForChatRequests(uid?._id.toString())
            if (chatRequestCreated) {
                IncomingChatScreen(navController = navController)
            } else {
                BottomNavGraph(
                    homeNavController = homeNavController,
                    navController = navController,
                    hyperKycLauncher = hyperKycLauncher,
                    registrationViewModel = registrationViewModel
                )
            }
        }
    }
}


