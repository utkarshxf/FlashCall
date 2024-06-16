package com.example.myapplication.myapplication.flashcall

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import co.hyperverge.hyperkyc.HyperKyc
import co.hyperverge.hyperkyc.data.models.HyperKycConfig
import co.hyperverge.hyperkyc.data.models.result.HyperKycResult
import co.hyperverge.hyperkyc.data.models.result.HyperKycStatus
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.Data.VideoCallRoute
import com.example.myapplication.myapplication.flashcall.Screens.EditProfileScreen
import com.example.myapplication.myapplication.flashcall.Screens.HomeScreen
import com.example.myapplication.myapplication.flashcall.Screens.IncomingCallScreen
import com.example.myapplication.myapplication.flashcall.Screens.MainScreen
import com.example.myapplication.myapplication.flashcall.Screens.ProfileScreen
import com.example.myapplication.myapplication.flashcall.Screens.RegistrationScreen
import com.example.myapplication.myapplication.flashcall.Screens.SignUpOTP
import com.example.myapplication.myapplication.flashcall.Screens.SignUpScreen
import com.example.myapplication.myapplication.flashcall.Screens.VideoCall
import com.example.myapplication.myapplication.flashcall.Screens.WalletScreen
import com.example.myapplication.myapplication.flashcall.ViewModel.AuthenticationViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.RegistrationViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.SplashViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.VideoCallViewModel
import com.example.myapplication.myapplication.flashcall.navigation.RootNavGraph
import com.example.myapplication.myapplication.flashcall.ui.theme.FlashCallTheme
import com.example.myapplication.myapplication.flashcall.utils.rememberImeState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<SplashViewModel>()
    private val authenticationViewModel by viewModels<AuthenticationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !viewModel.isReady.value
            }
        }
        super.onCreate(savedInstanceState)
        setContent {
            FlashCallTheme {

                val imeState = rememberImeState()
                val scrollState = rememberScrollState()

                LaunchedEffect(key1 = imeState.value) {
                    if (imeState.value){
                        scrollState.animateScrollTo(scrollState.maxValue, tween(300))
                    }
                }


                    AppNavigation()
                RootNavGraph()



            }
        }
    }
}

@Composable
fun AppNavigation() {



    val navController = rememberNavController()
    var viewModel = hiltViewModel<AuthenticationViewModel>()
    var registrationViewModel = hiltViewModel<RegistrationViewModel>()
    var videoViewModel = hiltViewModel<VideoCallViewModel>()

    LaunchedEffect(
        key1 = Unit
    ) {

        videoViewModel.videoCall.collectLatest {
            if (it != null) {
                navController.navigate(ScreenRoutes.IncomingCallScreen.route)
            }
        }
    }

    val inComingCall by videoViewModel.videoCall.collectAsState()

    NavHost(navController = navController, startDestination = ScreenRoutes.MainScreen.route) {

        composable(route= ScreenRoutes.MainScreen.route) {
            MainScreen(navController = navController)
        }
        composable(route= ScreenRoutes.SignUpScreen.route) {
            SignUpScreen(navController = navController, viewModel = viewModel)
        }

        composable(route= ScreenRoutes.SignUpOTP.route,
        ) {
            SignUpOTP(navController = navController, viewModel = viewModel)
        }

        composable(route= ScreenRoutes.RegistrationScreen.route) {
            RegistrationScreen(navController = navController, registrationViewModel = registrationViewModel, viewModel)
        }

        composable(route = ScreenRoutes.HomeScreen.route){
            HomeScreen(navController = navController,registrationViewModel)
        }

        composable(route = ScreenRoutes.EditScreen.route){
            EditProfileScreen(navController = navController)
        }

        composable(route = ScreenRoutes.ProfileScreen.route){
            ProfileScreen(navController = navController)
        }
        composable(route = ScreenRoutes.WalletScreen.route) {
            WalletScreen(navController)
        }

        composable(route = VideoCallRoute.VideoCall.route) {
            VideoCall(videoCall = true, viewModel = videoViewModel, navController = navController)
        }

        composable(route = ScreenRoutes.IncomingCallScreen.route){
            IncomingCallScreen(call = inComingCall!!, navController = navController)
        }

    }
}

