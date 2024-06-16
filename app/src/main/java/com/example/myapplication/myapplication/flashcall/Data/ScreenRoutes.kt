package com.example.myapplication.myapplication.flashcall.Data

sealed class ScreenRoutes(val route: String) {

    data object SignUpScreen : ScreenRoutes("SignUpScreen")
    data object SignUpOTP : ScreenRoutes("SignUpOTP")
    data object RegistrationScreen : ScreenRoutes("RegistrationScreen")
    data object HomeScreen : ScreenRoutes("HomeScreen")
    data object EditScreen : ScreenRoutes("EditScreen")

    data object ProfileScreen : ScreenRoutes("ProfileScreen")
    data object WalletScreen : ScreenRoutes("WalletScreen")
    data object IncomingCallScreen : ScreenRoutes("IncomingCallScreen")

    data object MainScreen : ScreenRoutes("MainScreen")
}