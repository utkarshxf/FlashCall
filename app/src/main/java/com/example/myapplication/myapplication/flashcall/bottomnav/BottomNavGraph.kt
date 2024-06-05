package com.example.myapplication.myapplication.flashcall.bottomnav

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.Screens.HomeScreen
import com.example.myapplication.myapplication.flashcall.Screens.ProfileScreen
import com.example.myapplication.myapplication.flashcall.Screens.WalletScreen
import com.example.myapplication.myapplication.flashcall.ViewModel.RegistrationViewModel

@Composable
fun BottomNavGraph(navController: NavHostController, registrationViewModel: RegistrationViewModel) {

    NavHost(navController = navController, startDestination = ScreenRoutes.HomeScreen.route) {

        composable(route = ScreenRoutes.HomeScreen.route) {
            HomeScreen(navController, registrationViewModel)
        }
        composable(route = ScreenRoutes.WalletScreen.route) {
            WalletScreen(navController)
        }
        composable(route = ScreenRoutes.ProfileScreen.route) {
            ProfileScreen(navController)
        }
    }
}