package com.example.myapplication.myapplication.flashcall.Screens

import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import co.hyperverge.hyperkyc.data.models.HyperKycConfig
import com.example.myapplication.myapplication.flashcall.ViewModel.RegistrationViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.chats.ChatRequestViewModel
import com.example.myapplication.myapplication.flashcall.bottomnav.BottomBar
import com.example.myapplication.myapplication.flashcall.bottomnav.BottomNavGraph

@Composable
fun MainScreen(navController: NavController,hyperKycLauncher: ActivityResultLauncher<HyperKycConfig>, registrationViewModel: RegistrationViewModel,chatRequestViewModel: ChatRequestViewModel = hiltViewModel())
{
    val homeNavController = rememberNavController()
    val chatRequestCreated by chatRequestViewModel.chatRequestCreated.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (!chatRequestCreated) {
                BottomBar(navController = homeNavController)
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Log.d("chatrequest", "Chat request listener is called")
            chatRequestViewModel.listenForChatRequests("66bf03168d7a6ca948b334bd")

            if (chatRequestCreated) {
                // Show the Incoming Chat Screen
                IncomingChatScreen(navController = navController)
            } else {
                // Show the main navigation when no chat request is active
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
