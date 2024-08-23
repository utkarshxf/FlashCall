package com.example.myapplication.myapplication.flashcall

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.compose.animation.core.tween
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import co.hyperverge.hyperkyc.HyperKyc
import co.hyperverge.hyperkyc.data.models.HyperKycConfig
import co.hyperverge.hyperkyc.data.models.result.HyperKycStatus
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.Data.VideoCallRoute
import com.example.myapplication.myapplication.flashcall.Data.model.Resource
import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.ChatState
import com.example.myapplication.myapplication.flashcall.Screens.EditProfileScreen
import com.example.myapplication.myapplication.flashcall.Screens.HomeScreen
import com.example.myapplication.myapplication.flashcall.Screens.InCallScreen
import com.example.myapplication.myapplication.flashcall.Screens.IncomingAudioCallScreen
//import com.example.myapplication.myapplication.flashcall.Screens.HomeScreenDemo
import com.example.myapplication.myapplication.flashcall.Screens.IncomingCallScreen
import com.example.myapplication.myapplication.flashcall.Screens.IncomingChatScreen
import com.example.myapplication.myapplication.flashcall.Screens.KYCScreen
import com.example.myapplication.myapplication.flashcall.Screens.LoginDoneScreen
import com.example.myapplication.myapplication.flashcall.Screens.MainScreen
import com.example.myapplication.myapplication.flashcall.Screens.ProfileScreen
import com.example.myapplication.myapplication.flashcall.Screens.RegistrationScreen
import com.example.myapplication.myapplication.flashcall.Screens.SelectSpecialityScreen
import com.example.myapplication.myapplication.flashcall.Screens.SignUpOTP
import com.example.myapplication.myapplication.flashcall.Screens.SignUpScreen
import com.example.myapplication.myapplication.flashcall.Screens.VideoCall
import com.example.myapplication.myapplication.flashcall.Screens.chats.ChatRequestScreen
import com.example.myapplication.myapplication.flashcall.Screens.chats.ChatRoomScreen

import com.example.myapplication.myapplication.flashcall.Screens.feedback.FeedbackScreen
import com.example.myapplication.myapplication.flashcall.Screens.profileOptions.PaymentSettings
import com.example.myapplication.myapplication.flashcall.Screens.wallet.WalletScreen
import com.example.myapplication.myapplication.flashcall.ViewModel.AuthenticationViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.RegistrationViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.SplashViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.VideoCallViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.chats.ChatRequestViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.chats.ChatViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.chats.ChattingFCMViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.wallet.WalletViewModel
//import com.example.myapplication.myapplication.flashcall.navigation.RootNavGraph
import com.example.myapplication.myapplication.flashcall.ui.theme.FlashCallTheme
import com.example.myapplication.myapplication.flashcall.utils.rememberImeState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<SplashViewModel>()
    private val authenticationViewModel by viewModels<AuthenticationViewModel>()
    private val chatRequestViewModel by viewModels<ChatRequestViewModel>()
//    private val chattingFCMViewModel: ChattingFCMViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !viewModel.isReady.value
            }
        }

        var config = HyperKycConfig(
            appId = "muzdob",
            appKey = "2ns9u1evoeugbrydykl7",
            workflowId = "workflow_9KW4mUl",
            transactionId = "TestTransact8",
        )
//        val state = chattingFCMViewModel.state
        val hyperKycLauncher: ActivityResultLauncher<HyperKycConfig> = registerForActivityResult(HyperKyc.Contract()) { result ->

            // handle result post workflow finish/exit

            when(result.status){

                HyperKycStatus.AUTO_APPROVED->{
                    Log.d("HyperKyc", "Auto Approved")


                }

                HyperKycStatus.ERROR->{
                    Log.d("HyperKyc", "Error")

                }

                HyperKycStatus.NEEDS_REVIEW->{
                    Log.d("HyperKyc", "Needs Review")

                }

                HyperKycStatus.AUTO_DECLINED->{
                    Log.d("HyperKyc", "AUTO_DECLINED")


                }

                HyperKycStatus.USER_CANCELLED->{
                    Log.d("HyperKyc", "User_Declined")

                }


            }

            val data = result.details
            Log.d("HyperKyc", "Data: $data")
            Log.e("HyperKyc", "Status: ${result.transactionId}")
        }



//        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        setContent {

            FlashCallTheme {

                val imeState = rememberImeState()
                val scrollState = rememberScrollState()

                LaunchedEffect(key1 = imeState.value) {
                    if (imeState.value){
                        scrollState.animateScrollTo(scrollState.maxValue, tween(300))
                    }
                }


                    AppNavigation(hyperKycLauncher)



            }
        }


    }
}

@Composable
fun AppNavigation(hyperKycLauncher: ActivityResultLauncher<HyperKycConfig>) {


    val navController = rememberNavController()
    var viewModel = hiltViewModel<AuthenticationViewModel>()
    var registrationViewModel = hiltViewModel<RegistrationViewModel>()
    var videoViewModel = hiltViewModel<VideoCallViewModel>()
    var chatRequestViewModel = hiltViewModel<ChatRequestViewModel>()
    var splashViewModel = hiltViewModel<SplashViewModel>()
    var chatViewModel = hiltViewModel<ChatViewModel>()
    var walletViewModel = hiltViewModel<WalletViewModel>()
    var authenticationViewModel = hiltViewModel<AuthenticationViewModel>()
    var chattingFCMViewModel = hiltViewModel<ChattingFCMViewModel>()
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit) {

        val token = authenticationViewModel.getTokenFromPreferences(context)

        if (!token.isNullOrEmpty()) {
            navController.navigate(ScreenRoutes.MainScreen.route) {
                popUpTo(0) { inclusive = true } // Clear the backstack
            }
        }
    }

    LaunchedEffect(
        key1 = Unit
    ) {

        videoViewModel.videoCall.collectLatest {
            if (it != null) {
                navController.navigate(ScreenRoutes.IncomingCallScreen.route)
            }
        }
    }

//    LaunchedEffect(
//        key1 =Unit
//    ) {
//        splashViewModel.navigationEvent.collectLatest { event ->
//            when (event) {
//                SplashViewModel.NavigationEvent.NavigateToHome -> {
//                    navController.navigate(ScreenRoutes.MainScreen.route)
//                }
//                SplashViewModel.NavigationEvent.NavigateToRegistration -> {
//
//                }
//            }
//        }
//    }





    val inComingCall by videoViewModel.videoCall.collectAsState()
    val state = chattingFCMViewModel.state

    NavHost(navController = navController, startDestination = ScreenRoutes.SignUpScreen.route) {

        composable(route= ScreenRoutes.SignUpScreen.route) {
            SignUpScreen(navController = navController, viewModel = viewModel)
        }
        
        composable(ScreenRoutes.HomeScreenDemo.route){
//            HomeScreenDemo(navController = navController)
        }

        composable(route= ScreenRoutes.SignUpOTP.route,
        ) {
            SignUpOTP(navController = navController, viewModel = viewModel)
        }

        composable(route= ScreenRoutes.RegistrationScreen.route) {
            RegistrationScreen(navController = navController, registrationViewModel = registrationViewModel, viewModel)
        }

        composable(route = ScreenRoutes.HomeScreen.route){
            HomeScreen(navController = navController,registrationViewModel,viewModel)
        }

        composable(route = ScreenRoutes.EditScreen.route){
            EditProfileScreen(navController = navController)
        }

        composable(route = ScreenRoutes.ProfileScreen.route){
            ProfileScreen(navController = navController,hyperKycLauncher, registrationViewModel, authenticationViewModel)
        }

        composable(route = ScreenRoutes.WalletScreen.route) {
            WalletScreen(navController, walletViewModel, registrationViewModel,authenticationViewModel )
        }

        composable(route = VideoCallRoute.VideoCall.route) {

            VideoCall(videoCall = true, viewModel = videoViewModel, navController = navController)
        }

        composable(route = ScreenRoutes.IncomingCallScreen.route){
            IncomingCallScreen(call = inComingCall!!, navController = navController)
        }

        composable(route= ScreenRoutes.MainScreen.route) {
            MainScreen(navController = navController,hyperKycLauncher,registrationViewModel)
//            LaunchedEffect(
//                key1 = Unit
//            ) {
//                delay(5000)
//                chatRequestViewModel.pendingChatRequest.collectLatest {result->
//                    when(result) {
//                        is Resource.Error -> {
//                            Log.d("ChatRequestError", "Error: ${result.message}")
//                        }
//                        is Resource.Loading -> {
//                            Log.d("ChatRequestLoading", "Loading: ${result.message}")
//                        }
//                        is Resource.Success -> {
//                            navController.navigate(ScreenRoutes.ChatRequestNotification.route)
//                            chatRequestViewModel.clearPendingChatRequest()
//                        }
//                    }
//                }
//            }
        }

        composable(route = ScreenRoutes.ChatRequestNotification.route) {
            ChatRequestScreen(
                token = state.remoteToken,
                onTokenChange = chattingFCMViewModel::onRemoteTokenChange ,
                onSubmit = chattingFCMViewModel::onSubmitChange
            )
        }
        
        composable(route = ScreenRoutes.PaymentSettings.route) {
            PaymentSettings(navController = navController)
        }
        composable(route = ScreenRoutes.ChatRoomScreen.route){
            ChatRoomScreen(chatViewModel, chatRequestViewModel, authenticationViewModel)
        }

        composable(route = ScreenRoutes.FeedbackScreen.route){
            FeedbackScreen(navController = navController)
        }
        composable(route = ScreenRoutes.SelectSpeciality.route){
            SelectSpecialityScreen(navController = navController)
        }
        composable(route = ScreenRoutes.LoginDoneScreen.route){
            LoginDoneScreen()
        }
        composable(route = ScreenRoutes.IncomingAudioCallScreen.route){
            IncomingAudioCallScreen(callerName = "Prakhar", navController = navController )
            
        }
        composable(route = ScreenRoutes.InCallScreen.route){
            InCallScreen(callerName = "Prakhar Bhatnagar", timeLeft = "10:02", callDuration = "15:15", navController = navController )

        }
        composable(route = ScreenRoutes.InComingChatScreen.route){
            IncomingChatScreen(navController = navController)

        }
        composable(route = ScreenRoutes.KycScreen.route){
            KYCScreen()

        }



    }
}