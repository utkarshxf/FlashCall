package com.example.myapplication.myapplication.flashcall

//import com.example.myapplication.myapplication.flashcall.Screens.HomeScreenDemo

//import com.example.myapplication.myapplication.flashcall.navigation.RootNavGraph
import android.app.Application
import android.content.ComponentCallbacks2
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.compose.animation.core.tween
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import co.hyperverge.hyperkyc.HyperKyc
import co.hyperverge.hyperkyc.data.models.HyperKycConfig
import co.hyperverge.hyperkyc.data.models.result.HyperKycStatus
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.Screens.EditProfileScreen
import com.example.myapplication.myapplication.flashcall.Screens.HomeScreen
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
import com.example.myapplication.myapplication.flashcall.Screens.SplashScreen
import com.example.myapplication.myapplication.flashcall.Screens.chats.ChatRequestScreen
import com.example.myapplication.myapplication.flashcall.Screens.chats.ChatRoomScreen
import com.example.myapplication.myapplication.flashcall.Screens.feedback.FeedbackScreen
import com.example.myapplication.myapplication.flashcall.Screens.profileOptions.PaymentSettings
import com.example.myapplication.myapplication.flashcall.Screens.wallet.WalletScreen
import com.example.myapplication.myapplication.flashcall.ViewModel.AuthenticationViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.RegistrationViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.SplashViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.chats.ChatRequestViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.chats.ChatViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.chats.ChattingFCMViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.wallet.WalletViewModel
import com.example.myapplication.myapplication.flashcall.ui.theme.FlashCallTheme
import com.example.myapplication.myapplication.flashcall.utils.rememberImeState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<SplashViewModel>()
    private val authenticationViewModel by viewModels<AuthenticationViewModel>()
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
        val hyperKycLauncher: ActivityResultLauncher<HyperKycConfig> =
            registerForActivityResult(HyperKyc.Contract()) { result ->
                // handle result post workflow finish/exit

                when (result.status) {

                    HyperKycStatus.AUTO_APPROVED -> {
                        Log.d("HyperKyc", "Auto Approved")
                    }

                    HyperKycStatus.ERROR -> {
                        Log.d("HyperKyc", "Error")
                    }

                    HyperKycStatus.NEEDS_REVIEW -> {
                        Log.d("HyperKyc", "Needs Review")
                    }

                    HyperKycStatus.AUTO_DECLINED -> {
                        Log.d("HyperKyc", "AUTO_DECLINED")
                    }

                    HyperKycStatus.USER_CANCELLED -> {
                        Log.d("HyperKyc", "User_Declined")

                    }
                }
                val data = result.details
                Log.d("HyperKyc", "Data: $data")
                Log.e("HyperKyc", "Status: ${result.transactionId}")
            }

        setContent {

            FlashCallTheme {

                val imeState = rememberImeState()
                val scrollState = rememberScrollState()

                LaunchedEffect(key1 = imeState.value) {
                    if (imeState.value) {
                        scrollState.animateScrollTo(scrollState.maxValue, tween(300))
                    }
                }
                AppNavigation(hyperKycLauncher)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        (this.application as? BaseClass)?.updateUserStatus(
            authenticationViewModel.getUserFromPreferences(application)?.phone.toString(),
            false
        )
    }
}

@Composable
fun AppNavigation(hyperKycLauncher: ActivityResultLauncher<HyperKycConfig>) {
    val navController = rememberNavController()
    var viewModel = hiltViewModel<AuthenticationViewModel>()
    var registrationViewModel = hiltViewModel<RegistrationViewModel>()
    var chatRequestViewModel = hiltViewModel<ChatRequestViewModel>()
    var splashViewModel = hiltViewModel<SplashViewModel>()
    var chatViewModel = hiltViewModel<ChatViewModel>()
    var walletViewModel = hiltViewModel<WalletViewModel>()
    var authenticationViewModel = hiltViewModel<AuthenticationViewModel>()
    var chattingFCMViewModel = hiltViewModel<ChattingFCMViewModel>()
    val context = LocalContext.current
    val state = chattingFCMViewModel.state

    LaunchedEffect(key1 = Unit) {
        val token = authenticationViewModel.getTokenFromPreferences(context)
        if (!token.isNullOrEmpty()) {
            (context.applicationContext as? BaseClass)?.streamBuilder(context)
            navController.navigate(ScreenRoutes.MainScreen.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    NavHost(navController = navController, startDestination = ScreenRoutes.SignUpScreen.route) {
        composable(route = ScreenRoutes.SignUpScreen.route) {
            SignUpScreen(navController = navController, viewModel = viewModel)
        }
        composable(route = ScreenRoutes.SignUpOTP.route) {
            SignUpOTP(navController = navController, viewModel = viewModel)
        }
        composable(route = ScreenRoutes.RegistrationScreen.route) {
            RegistrationScreen(
                navController = navController,
                registrationViewModel = registrationViewModel,
                viewModel
            )
        }
        composable(route = ScreenRoutes.HomeScreen.route) {
            HomeScreen(navController = navController, registrationViewModel, viewModel)
        }
        composable(route = ScreenRoutes.EditScreen.route) {
            EditProfileScreen(navController = navController)
        }
        composable(route = ScreenRoutes.ProfileScreen.route) {
            ProfileScreen(
                navController = navController,
                hyperKycLauncher,
                registrationViewModel,
                authenticationViewModel
            )
        }
        composable(route = ScreenRoutes.WalletScreen.route) {
            WalletScreen(navController, walletViewModel, authenticationViewModel)
        }

        composable(route = ScreenRoutes.IncomingAudioCallScreen.route) {
            IncomingCallScreen(callerName = "Audio Call Screen", navController = navController)
        }

        composable(route = ScreenRoutes.MainScreen.route) {
            MainScreen(navController = navController, hyperKycLauncher, registrationViewModel)
        }

        composable(route = ScreenRoutes.ChatRequestNotification.route) {
            ChatRequestScreen(
                token = state.remoteToken,
                onTokenChange = chattingFCMViewModel::onRemoteTokenChange,
                onSubmit = chattingFCMViewModel::onSubmitChange
            )
        }

        composable(route = ScreenRoutes.PaymentSettings.route) {
            PaymentSettings(navController = navController)
        }
        composable(route = ScreenRoutes.ChatRoomScreen.route) {
            ChatRoomScreen(navController)
        }

        composable(route = ScreenRoutes.FeedbackScreen.route) {
            FeedbackScreen(navController = navController)
        }
        composable(route = ScreenRoutes.SelectSpeciality.route) {
            SelectSpecialityScreen(navController = navController)
        }
        composable(route = ScreenRoutes.LoginDoneScreen.route) {
            LoginDoneScreen(navController = navController)
        }
        composable(route = ScreenRoutes.InComingChatScreen.route) {
            IncomingChatScreen(navController = navController)
        }
        composable(route = ScreenRoutes.KycScreen.route) {
            KYCScreen(navController = navController, hyperKycLauncher)
        }
    }
}



