package com.example.myapplication.myapplication.flashcall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.Screens.EditProfileScreen
import com.example.myapplication.myapplication.flashcall.Screens.HomeScreen
import com.example.myapplication.myapplication.flashcall.Screens.RegistrationScreen
import com.example.myapplication.myapplication.flashcall.Screens.SignUpOTP
import com.example.myapplication.myapplication.flashcall.Screens.SignUpScreen
import com.example.myapplication.myapplication.flashcall.ViewModel.AuthenticationViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.SplashViewModel
import com.example.myapplication.myapplication.flashcall.ui.theme.FlashCallTheme
import dagger.hilt.android.AndroidEntryPoint

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
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    var viewModel = hiltViewModel<AuthenticationViewModel>()
    
    NavHost(navController = navController, startDestination = ScreenRoutes.SignUpScreen.route) {

        composable(route= ScreenRoutes.SignUpScreen.route) {
            SignUpScreen(navController = navController, viewModel = viewModel)
        }

        composable(route= ScreenRoutes.SignUpOTP.route) {
            SignUpOTP(navController = navController, viewModel = viewModel)
        }

        composable(route= ScreenRoutes.RegistrationScreen.route) {
            RegistrationScreen(navController = navController)
        }

        composable(route = ScreenRoutes.HomeScreen.route){
            HomeScreen(navController = navController)
        }

        composable(route = ScreenRoutes.EditScreen.route){
            EditProfileScreen(navController = navController)
        }
    }
}

