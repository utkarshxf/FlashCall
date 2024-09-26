package com.example.myapplication.myapplication.flashcall.ViewModel

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.util.Log.d
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.myapplication.myapplication.flashcall.Data.Events
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.Data.model.APIResponse
import com.example.myapplication.myapplication.flashcall.Data.model.IsUserCreatedResponse
import com.example.myapplication.myapplication.flashcall.Data.model.ResendOTPResponse
import com.example.myapplication.myapplication.flashcall.Data.model.SendOTPResponseX
import com.example.myapplication.myapplication.flashcall.Data.model.VerifyOTPResponse
import com.example.myapplication.myapplication.flashcall.preferenceStore.userPref
import com.example.myapplication.myapplication.flashcall.repository.AuthRepository
import com.example.myapplication.myapplication.flashcall.repository.UserPreferencesRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authenticationRepository: AuthRepository,
    private val userPref: userPref,
    private val userPreferencesRepository:UserPreferencesRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    var inProcess = mutableStateOf(false)
    val eventMutableState = mutableStateOf<Events<String>?>(null)
    private var _isUserLoggedIn = false
    var isUserLoggedIn = _isUserLoggedIn
    private val _sendOTPState = MutableStateFlow<APIResponse<SendOTPResponseX>>(APIResponse.Empty)
    val sendOTPState: StateFlow<APIResponse<SendOTPResponseX>> = _sendOTPState

    private val _resendOTPState =
        MutableStateFlow<APIResponse<ResendOTPResponse>>(APIResponse.Empty)
    val resendOTPState: StateFlow<APIResponse<ResendOTPResponse>> = _resendOTPState

    private val _verifyOTPState =
        MutableStateFlow<APIResponse<VerifyOTPResponse>>(APIResponse.Empty)
    val verifyOTPState: StateFlow<APIResponse<VerifyOTPResponse>> = _verifyOTPState

    private val _phone: MutableState<String> = mutableStateOf("")
    val phoneNumber: State<String> = _phone

    private val _isCreatedUserState = MutableStateFlow<IsUserCreatedResponse?>(null)
    val isCreatedUserState: StateFlow<IsUserCreatedResponse?> = _isCreatedUserState


    fun signUP(phoneNumber: String, navController: NavController, sendToke: String? , loading: (Boolean) -> Unit) {
        _phone.value = phoneNumber  // Update the ViewModel's phone state
        loading(true)
        viewModelScope.launch {
            _sendOTPState.value = APIResponse.Loading
            try {
                authenticationRepository.sendOtp(
                    "https://flashcall.vercel.app/api/v1/send-otp", phoneNumber
                ).collect {
                    if (it == null) {
                        _sendOTPState.value = APIResponse.Error("Exception Occurred")
                        Log.e("SignUpError", "Exception Occurred")
                        loading(false)
                    } else {
                        _sendOTPState.value = APIResponse.Success(it)
                        Log.e("SignUpError", "$phoneNumber")
                        navController.navigate(ScreenRoutes.SignUpOTP.route)
                        loading(false)
                    }
                }
            } catch (e: Exception) {
                _sendOTPState.value = APIResponse.Error(e.message ?: "OTP SEND ERROR")
                loading(false)
            }
        }
    }

    fun resendOTP(phone: String, loading: (Boolean) -> Unit) {
        _resendOTPState.value = APIResponse.Loading
        loading(true)
        viewModelScope.launch {
            try {
                authenticationRepository.resendOtp(
                    "https://flashcall.vercel.app/api/v1/resend-otp", phone
                ).collect {
                    _resendOTPState.value = APIResponse.Success(it)
                    loading(false)
                }
            } catch (e: Exception) {
                _resendOTPState.value = APIResponse.Error(e.message ?: "OTP RESEND ERROR")
                loading(false)
            }
        }
    }

    fun verifyOTP(
        phone: String,
        otp: String,
        token: String?,
        navController: NavController,
        loading: (Boolean) -> Unit
    ) {
        _verifyOTPState.value = APIResponse.Loading
        loading(true)
        try {
            viewModelScope.launch {

                try {
                    Log.e("qwerty" , "$phone $otp" )

                    authenticationRepository.verifyOtp(
                        "api/v1/verify-otp", phone, otp
                    ).collect {
                        if (it.token != null) {
                            _verifyOTPState.value = APIResponse.Success(it)
                            loading(false)
                            if (isCreatedUserState.value != null) {
                                saveTokenToPreferences(context, it.token)
                                loading(false)
                                navController.navigate(ScreenRoutes.LoginDoneScreen.route) {
                                    popUpTo(ScreenRoutes.SignUpOTP.route) {
                                        inclusive = true
                                    }
                                }
                            } else {
                                saveTokenToPreferences(context, it.token)
                                loading(false)
                                navController.navigate(ScreenRoutes.RegistrationScreen.route) {
                                    popUpTo(ScreenRoutes.LoginDoneScreen.route) {
                                        inclusive = true
                                    }
                                }
                            }
                        }else{
                            _verifyOTPState.value = APIResponse.Error("Invalid OTP or Bad Request")
                            loading(false)
                            Toast.makeText(
                                navController.context, "Invalid OTP. Please try again.", Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                } catch (e: HttpException) {
                    Log.e("verifyOTP", "HTTP error code: ${e.code()} - ${e.message()}")
                    _verifyOTPState.value = APIResponse.Error("Invalid OTP or Bad Request")
                    loading(false)
                    Toast.makeText(
                        navController.context, "Invalid OTP. Please try again.", Toast.LENGTH_LONG
                    ).show()
                }
            }

        } catch (e: Exception) {
            // Handle other exceptions (e.g., network issues)
            Log.e("verifyOTP", "Exception: ${e.localizedMessage}")
            loading(false)
            _verifyOTPState.value =
                APIResponse.Error("An unexpected error occurred. Please try again.")
            Toast.makeText(
                navController.context,
                "An unexpected error occurred. Please try again.",
                Toast.LENGTH_LONG
            ).show()
        }

    }


    fun handleException(exception: Exception? = null, customMessage: String = "") {
        Log.e("SignUpError", "Exception Occured", exception)
        exception?.printStackTrace()

        val errorMessage = exception?.localizedMessage ?: ""

        val message = if (customMessage.isNullOrEmpty()) errorMessage else customMessage

        eventMutableState.value = Events(message)
        inProcess.value = false
    }

    val userToken = userPref.getToken().stateIn(
        scope = viewModelScope, started = SharingStarted.WhileSubscribed(), initialValue = ""
    )


    fun saveToken(token: String) {
        viewModelScope.launch {
            userPref.saveToken(token)
        }
    }

    fun iCreatedUser(phone: String, loading: (Boolean) -> Unit) {
        loading(true)
        viewModelScope.launch {
            try {
                // Collect the Flow<IsUserCreatedResponse?>
                authenticationRepository.isCreatedUser(
                    "api/v1/user/getUserByPhone", phone
                ).collect { userData ->
                    if (userData != null) {
                        // Process the user data here
                        d("iCreatedUser", "User data fetched successfully: $userData")
                        _isCreatedUserState.value = userData
//                        saveUserToPreferences(context, userData)
                        userPreferencesRepository.saveUser(userData)
                        loading(false)
                    } else {
                        Log.e("iCreatedUser", "User not found")
                        // Handle "No User Found" case here
                        _isCreatedUserState.value = null
                        loading(false)
                    }
                }
            } catch (e: Exception) {
                Log.e("iCreatedUser", "Exception: ${e.localizedMessage}")
                // Handle exceptions here
                loading(false)
            }
        }
    }

    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_prefs1", Context.MODE_PRIVATE)

    fun saveTokenToPreferences(context: Context, token: String) {
        sharedPreferences.edit().apply {
            putString("user_token", token)
            apply() // Apply changes asynchronously
        }
    }

    fun deleteTokenFromPreferences() {
        sharedPreferences.edit().apply {
            remove("user_token") // Remove the token
            apply() // Apply changes asynchronously
        }
    }

    fun getTokenFromPreferences(context: Context): String? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("user_prefs1", Context.MODE_PRIVATE)
        return sharedPreferences.getString("user_token", null)
    }

    fun saveUserToPreferences(context: Context, userData: IsUserCreatedResponse) {
        userPreferencesRepository.saveUser(userData)
    }

    fun getUserFromPreferences(context: Context): IsUserCreatedResponse? {
       return userPreferencesRepository.getUser()
    }
}

//
//{
//    "message": "OTP verified successfully",
//    "sessionToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwaG9uZSI6IjgxOTI5MjM5OTUiLCJ1c2VyIjp7Il9pZCI6IjY2ZWFhODIzNjRkYWJhYmRmNTgzNWRjNCIsInVzZXJuYW1lIjoiZ2F1cmkxMjMiLCJwaG9uZSI6Iis5MTgxOTI5MjM5OTUiLCJmdWxsTmFtZSI6Ik1vaGQgR2F1cmkiLCJmaXJzdE5hbWUiOiJ1cGRhdGUiLCJsYXN0TmFtZSI6Im5hbWUiLCJwaG90byI6Imh0dHBzOi8vZmlyZWJhc2VzdG9yYWdlLmdvb2dsZWFwaXMuY29tL3YwL2IvZmxhc2hjYWxsY2hhdC5hcHBzcG90LmNvbS9vL2ltYWdlcyUyRjEwMDAwMDU0NzY_YWx0PW1lZGlhJnRva2VuPWEwZTFhOGE3LTBiNTAtNGFkYi05YjBlLTc2NWYyN2YzYzU1ZCIsInByb2Zlc3Npb24iOiJBc3Ryb2xvZ2VyIiwidGhlbWVTZWxlY3RlZCI6IiM1MEE2NUMiLCJ2aWRlb1JhdGUiOiIyNSIsImF1ZGlvUmF0ZSI6IjI1IiwiY2hhdFJhdGUiOiIyNSIsInZpZGVvQWxsb3dlZCI6dHJ1ZSwiYXVkaW9BbGxvd2VkIjp0cnVlLCJjaGF0QWxsb3dlZCI6dHJ1ZSwiZ2VuZGVyIjoiTWFsZSIsImRvYiI6IjE4LTA5LTIwMjQiLCJiaW8iOiJ0aGlzIGlzIGJpbyB0ZXh0Iiwia3ljX3N0YXR1cyI6IkluY29tcGxldGUiLCJ3YWxsZXRCYWxhbmNlIjowLCJyZWZlcnJlZEJ5IjpudWxsLCJyZWZlcnJhbEFtb3VudCI6MCwibGlua3MiOlt7InRpdGxlIjoieW91dHViZSIsInVybCI6Imh0dHA6Ly95b3V0dWJlLmNvbSIsImlzQWN0aXZlIjp0cnVlfSx7InRpdGxlIjoieW91dHViZSIsInVybCI6Imh0dHBzOi8veW91dHViZS5jb20iLCJpc0FjdGl2ZSI6dHJ1ZX0seyJ0aXRsZSI6Imluc3RhZ3JhbSIsInVybCI6Imh0dHBzOi8vaW5zdGFncmFtLmNvbSIsImlzQWN0aXZlIjp0cnVlfSx7InRpdGxlIjoiVGVzdCIsInVybCI6InRlc3QuY29tIiwiaXNBY3RpdmUiOnRydWV9XSwiY3JlYXRlZEF0IjoiMjAyNC0wOS0xOFQxMDoxNDo1OS4yNzFaIiwidXBkYXRlZEF0IjoiMjAyNC0wOS0xOVQwNTozMjowMy4wNDRaIiwiX192IjowLCJ1c2VyVHlwZSI6ImNyZWF0b3IifSwiaWF0IjoxNzI2NzI4OTc5LCJleHAiOjE3MjczMzM3Nzl9.qcXWtgHRJILQtpV3LM2vhIdfcb6ARQMbrle4kbgZ6kI"
//}
//{
//    "error": "OTP Mismatch"
//}

