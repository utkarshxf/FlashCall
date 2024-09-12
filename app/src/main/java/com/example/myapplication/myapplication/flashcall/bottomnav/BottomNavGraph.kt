package com.example.myapplication.myapplication.flashcall.bottomnav

import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import co.hyperverge.hyperkyc.data.models.HyperKycConfig
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.Data.VideoCallRoute
import com.example.myapplication.myapplication.flashcall.Screens.EditProfileScreen
import com.example.myapplication.myapplication.flashcall.Screens.HomeScreen
import com.example.myapplication.myapplication.flashcall.Screens.InCallScreen
import com.example.myapplication.myapplication.flashcall.Screens.IncomingVideoCallScreen
import com.example.myapplication.myapplication.flashcall.Screens.OngoingVideoCallScreen
import com.example.myapplication.myapplication.flashcall.Screens.ProfileScreen
import com.example.myapplication.myapplication.flashcall.Screens.wallet.WalletScreen
import com.example.myapplication.myapplication.flashcall.ViewModel.AuthenticationViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.RegistrationViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.VideoCallViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.wallet.WalletViewModel
import io.getstream.video.android.core.Call

@Composable
fun BottomNavGraph(
    homeNavController: NavHostController,
    navController: NavController,
    hyperKycLauncher: ActivityResultLauncher<HyperKycConfig>,
    registrationViewModel: RegistrationViewModel = hiltViewModel(),
    authenticationViewModel: AuthenticationViewModel = hiltViewModel(),
    walletViewModel: WalletViewModel = hiltViewModel(),
    videoCallViewModel: VideoCallViewModel = hiltViewModel(),
) {
    val uiState by videoCallViewModel.videoMutableUiState.collectAsStateWithLifecycle(lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current)
    val incomingCall by videoCallViewModel.incomingCall.collectAsState(initial = null)
    val activeCall by videoCallViewModel.activeCall.collectAsState(initial = null)
    NavHost(navController = homeNavController, startDestination = ScreenRoutes.HomeScreen.route) {
        composable(route = ScreenRoutes.IncomingVideoCallScreen.route) {
            IncomingVideoCall(
                call = incomingCall,
                navController = homeNavController,
                videoCallViewModel = videoCallViewModel,
                onEmptyCall = { homeNavController.navigateToHome() })
        }
        composable(route = VideoCallRoute.OngoingVideoCall.videoCallRoute) {
            OngoingVideoCallScreen(true ,viewModel = videoCallViewModel, navController = homeNavController)
        }
        composable(route = ScreenRoutes.InCallScreen.route) {
            InCallScreen(call = incomingCall,
                navController = homeNavController,
                onEmptyCall = { homeNavController.navigateToHome() })
        }
        composable(route = ScreenRoutes.HomeScreen.route) {
            HomeScreen(
                homeNavController,
                registrationViewModel,
                authenticationViewModel = authenticationViewModel
            )
        }
        composable(route = ScreenRoutes.WalletScreen.route) {
            WalletScreen(
                navController, walletViewModel, authenticationViewModel
            )
        }
        composable(route = ScreenRoutes.ProfileScreen.route) {
            ProfileScreen(
                navController,
                hyperKycLauncher = hyperKycLauncher,
                registrationViewModel,
                authenticationViewModel
            )
        }
        composable(route = ScreenRoutes.EditScreen.route) {
            EditProfileScreen(navController = homeNavController)
        }
    }
}
@Composable
private fun IncomingVideoCall(
    call: Call?, navController: NavHostController,videoCallViewModel: VideoCallViewModel, onEmptyCall: () -> Unit
) {
    if(call!=null)
    {
        IncomingVideoCallScreen(call = call, navController = navController , videoCallViewModel)
    }
    else
    {
//        LaunchedEffect(Unit) {  onEmptyCall() }
    }
}

private fun NavHostController.navigateToHome() {
    navigate(ScreenRoutes.HomeScreen.route) {
        popUpTo(ScreenRoutes.HomeScreen.route) { inclusive = true }
    }
}


// Refactored InCallScreen
@Composable
fun InCallScreen(
    call: Call?, navController: NavHostController, onEmptyCall: () -> Unit
) {
    InCallScreen(
        callerName = call!!.user.name, timeLeft = "10:01", // Replace with actual time calculation
        callDuration = "11:11", // Replace with actual duration calculation
        navController = navController
    )
}