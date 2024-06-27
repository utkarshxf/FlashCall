package com.example.myapplication.myapplication.flashcall.Screens

import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import co.hyperverge.hyperkyc.data.models.HyperKycConfig
import com.example.myapplication.myapplication.flashcall.ViewModel.RegistrationViewModel
import com.example.myapplication.myapplication.flashcall.bottomnav.BottomBar
import com.example.myapplication.myapplication.flashcall.bottomnav.BottomNavGraph

@Composable
fun MainScreen(navController: NavController,hyperKycLauncher: ActivityResultLauncher<HyperKycConfig>, registrationViewModel: RegistrationViewModel)
{
    val homeNavController = rememberNavController()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomBar(navController = homeNavController)
        }
    ) {
        Column(modifier = Modifier
            .padding(it)
            .fillMaxSize())
        {
            BottomNavGraph(homeNavController, navController, hyperKycLauncher, registrationViewModel = registrationViewModel)
        }
    }
}
