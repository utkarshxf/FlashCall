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
                        "https://app.flashcall.me/api/v1/verify-otp", phone, otp
                    ).collect {
                        _verifyOTPState.value = APIResponse.Success(it)
                        if (it.token != null) {
                            navController.navigate(ScreenRoutes.LoginDoneScreen.route) {
                                popUpTo(ScreenRoutes.LoginDoneScreen.route) {
                                    inclusive = true
                                }
                            }
                            loading(false)
                            delay(2000)
                            if (isCreatedUserState.value != null) {
                                saveTokenToPreferences(context, it.token)
                                loading(false)
                                navController.navigate(ScreenRoutes.MainScreen.route) {
                                    popUpTo(ScreenRoutes.LoginDoneScreen.route) {
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
                    "https://flashcall.vercel.app/api/v1/user/getUserByPhone", phone
                ).collect { userData ->
                    if (userData != null) {
                        // Process the user data here
                        d("iCreatedUser", "User data fetched successfully: $userData")
                        _isCreatedUserState.value = userData
                        saveUserToPreferences(context, userData)
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
            apply()
        }
    }

    fun getTokenFromPreferences(context: Context): String? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("user_prefs1", Context.MODE_PRIVATE)
        return sharedPreferences.getString("user_token", null)
    }

    fun saveUserToPreferences(context: Context, userData: IsUserCreatedResponse) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("user_prefs1", Context.MODE_PRIVATE)
        sharedPreferences.edit().apply {
            // Save each value to SharedPreferences
            putString("user_id", userData._id)
            putString("_id", userData._id)
            putString("username", userData.username)
            putString("phone", userData.phone)
            putString("full_name", userData.fullName)
            putString("first_name", userData.firstName)
            putString("last_name", userData.lastName)
            putString("photo", userData.photo)
            putString("theme_selected", userData.themeSelected)
            putString("bio", userData.bio)
            putString("profession", userData.profession)
            putString("dob", userData.dob)
            putString("gender", userData.gender)
            putFloat("wallet_balance", userData.walletBalance?.toFloat() ?: 0f)
            putBoolean("audio_allowed", userData.audioAllowed ?: false)
            putBoolean("chat_allowed", userData.chatAllowed ?: false)
            putBoolean("video_allowed", userData.videoAllowed ?: false)
            putString("user_type", userData.userType)
            putString("message", userData.message)
            apply()
        }
    }

    fun getUserFromPreferences(context: Context): IsUserCreatedResponse? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("user_prefs1", Context.MODE_PRIVATE)

        val userId = sharedPreferences.getString("user_id", null)
        val username = sharedPreferences.getString("username", null)
        val phone = sharedPreferences.getString("phone", null)
        val fullName = sharedPreferences.getString("full_name", null)
        val firstName = sharedPreferences.getString("first_name", null)
        val lastName = sharedPreferences.getString("last_name", null)
        val photo = sharedPreferences.getString("photo", null)
        val themeSelected = sharedPreferences.getString("theme_selected", null)
        val bio = sharedPreferences.getString("bio", null)
        val profession = sharedPreferences.getString("profession", null)
        val dob = sharedPreferences.getString("dob", null)
        val gender = sharedPreferences.getString("gender", null)
        val walletBalance = sharedPreferences.getFloat("wallet_balance", 0f).toDouble()
        val audioAllowed = sharedPreferences.getBoolean("audio_allowed", false)
        val chatAllowed = sharedPreferences.getBoolean("chat_allowed", false)
        val videoAllowed = sharedPreferences.getBoolean("video_allowed", false)
        val userType = sharedPreferences.getString("user_type", null)
        val message = sharedPreferences.getString("message", null)

        return if (userId != null) {
            // Create the user data object
            IsUserCreatedResponse(
                _id = userId,
                username = username,
                phone = phone,
                fullName = fullName,
                firstName = firstName,
                lastName = lastName,
                photo = photo,
                themeSelected = themeSelected,
                bio = bio,
                profession = profession,
                dob = dob,
                gender = gender,
                walletBalance = walletBalance,
                audioAllowed = audioAllowed,
                chatAllowed = chatAllowed,
                videoAllowed = videoAllowed,
                userType = userType,
                message = message
            )
        } else {
            null // Return null if no user data is found
        }
    }
}
