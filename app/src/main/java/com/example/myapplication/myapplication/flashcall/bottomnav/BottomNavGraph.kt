package com.example.myapplication.myapplication.flashcall.bottomnav

import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import co.hyperverge.hyperkyc.data.models.HyperKycConfig
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.Screens.EditProfileScreen
import com.example.myapplication.myapplication.flashcall.Screens.HomeScreen
import com.example.myapplication.myapplication.flashcall.Screens.ProfileScreen
import com.example.myapplication.myapplication.flashcall.Screens.wallet.WalletScreen
import com.example.myapplication.myapplication.flashcall.ViewModel.AuthenticationViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.RegistrationViewModel

@Composable
fun BottomNavGraph(homeNavController: NavHostController, navController: NavController,hyperKycLauncher: ActivityResultLauncher<HyperKycConfig>, registrationViewModel: RegistrationViewModel = hiltViewModel(), authenticationViewModel: AuthenticationViewModel = hiltViewModel()) {

//    var registrationViewModel = hiltViewModel<RegistrationViewModel>()

    NavHost(navController = homeNavController, startDestination = ScreenRoutes.HomeScreen.route) {
        composable(route = ScreenRoutes.HomeScreen.route) {
            HomeScreen(homeNavController, registrationViewModel, authenticationViewModel = authenticationViewModel)
        }
        composable(route = ScreenRoutes.WalletScreen.route) {
            WalletScreen(navController)
        }
        composable(route = ScreenRoutes.ProfileScreen.route) {
            ProfileScreen(navController, hyperKycLauncher = hyperKycLauncher)
        }
        composable(route = ScreenRoutes.EditScreen.route){
            EditProfileScreen(navController = homeNavController)
        }
    }
}