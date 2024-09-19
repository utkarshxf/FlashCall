package com.example.myapplication.myapplication.flashcall.Screens

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import co.hyperverge.hyperkyc.HyperKyc
import co.hyperverge.hyperkyc.data.models.HyperKycConfig
import co.hyperverge.hyperkyc.data.models.result.HyperKycStatus
import com.example.myapplication.myapplication.flashcall.BaseClass
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.MainActivity
import com.example.myapplication.myapplication.flashcall.ViewModel.AuthenticationViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.RegistrationViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.VideoCallViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.chats.ChatRequestViewModel
import com.example.myapplication.myapplication.flashcall.bottomnav.BottomBar
import com.example.myapplication.myapplication.flashcall.bottomnav.BottomNavGraph
import com.example.myapplication.myapplication.flashcall.bottomnav.Screen
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
        val screens = listOf(
            ScreenRoutes.WalletScreen,
            ScreenRoutes.ProfileScreen,
            ScreenRoutes.HomeScreen,
        )
        val navBackStackEntry by homeNavController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        val bottomBarDestination = screens.any { it.route == currentDestination?.route }

        if (!chatRequestCreated && incomingCall == null && activeVideoCall == null) {
            if(bottomBarDestination){
                BottomBar(navController = homeNavController)
            }
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



