package com.example.myapplication.myapplication.flashcall.Data

sealed class ScreenRoutes(val route: String) {

    data object SignUpScreen : ScreenRoutes("SignUpScreen")
    data object SignUpOTP : ScreenRoutes("SignUpOTP")
    data object RegistrationScreen : ScreenRoutes("RegistrationScreen")
    data object HomeScreen : ScreenRoutes("HomeScreen")
    data object EditScreen : ScreenRoutes("EditScreen")
    data object IncomingCallScreen: ScreenRoutes("IncomingCallScreen")

    data object ProfileScreen : ScreenRoutes("ProfileScreen")
    data object WalletScreen : ScreenRoutes("WalletScreen")
    data object MainScreen : ScreenRoutes("MainScreen")
    data object ChatRequestNotification : ScreenRoutes("ChatRequestNotificationScreen")
    data object HomeScreenDemo : ScreenRoutes("HomeScreenDemo")

    data object PaymentSettings : ScreenRoutes("PaymentSettings")
    data object ChatRoomScreen : ScreenRoutes("ChatRoomScreen")

    data object FeedbackScreen : ScreenRoutes("FeedbackScreen")

    data object SelectSpeciality : ScreenRoutes("SelectSpeciality")


    data object LoginDoneScreen : ScreenRoutes("LoginDoneScreen")

    data object IncomingAudioCallScreen : ScreenRoutes("IncomingCallScreen")

    data object InCallScreen : ScreenRoutes("InCallScreen")

    data object InComingChatScreen : ScreenRoutes("InComingChatScreen")

    data object KycScreen : ScreenRoutes("KycScreen")

}