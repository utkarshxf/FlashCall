package com.example.myapplication.myapplication.flashcall.ViewModel

import android.util.Log
import android.util.Log.d
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.myapplication.myapplication.flashcall.Data.Events
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor( private val authenticationRepository: AuthRepository) : ViewModel() {

    var inProcess = mutableStateOf(false)
    val eventMutableState = mutableStateOf<Events<String>?>(null)

//    init {
//        viewModelScope.launch {
////            authenticationRepository.sendOtp("https://flashcall.vercel.app/api/v1/send-otp","7008150349").collect {
////                Log.d("OTP", "onCreate: $it")
////            }
//
//            authenticationRepository.resendOtp("https://flashcall.vercel.app/api/v1/send-otp","7008150349").collect{
//                Log.d("ResendOTP", "onCreate: $it")
//            }
//        }
//
//    }

    fun signUP(phone : String, navController: NavController)
    {
        if(phone.isEmpty())
        {
            handleException(customMessage = "Please Fill All fields")
            return
        }
        viewModelScope.launch {
           authenticationRepository.sendOtp("https://flashcall.vercel.app/api/v1/send-otp", phone).collect{
               Log.d("send OTP", "onresponse: $it")
                       navController.navigate(ScreenRoutes.SignUpOTP.route)
               }
        }
    }

    fun resendOTP(phone : String)
    {
        if(phone.isEmpty())
        {
            return
        }

        inProcess.value = true

        viewModelScope.launch {
            authenticationRepository.resendOtp("https://flashcall.vercel.app/api/v1/resend-otp", phone)
                .collect {
                    Log.d("OTP", "onCreate: $it")
                }
        }
    }

    fun verifyOTP(phone : String, otp : String, token:String)
    {
        viewModelScope.launch {
            authenticationRepository.verifyOtp("https://flashcall.vercel.app/api/v1/verify-otp", phone, otp, token).collect{
                Log.d("Verified OTP", "onCreate: $it")
            }
        }
    }

    fun handleException(exception: Exception?=null, customMessage: String="")
    {
        Log.e("SignUpError","Exception Occured",exception)
        exception?.printStackTrace()

        val errorMessage = exception?.localizedMessage?:""

        val message = if(customMessage.isNullOrEmpty()) errorMessage else customMessage

        eventMutableState.value = Events(message)
        inProcess.value = false

    }

}

//@HiltViewModel
//class AuthenticationViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {
//
//    var inProcess by mutableStateOf(false)
//        private set
//    private val _eventFlow = MutableSharedFlow<UIEvent>()
//    val eventFlow = _eventFlow.asSharedFlow()
//
//    // ... your UIEvent sealed class ...
//
//    sealed class UIEvent {
//        data class ShowError(val message: String) : UIEvent()
//        object NavigateToOtpScreen : UIEvent()
//        object NavigateToSuccessScreen : UIEvent() // Or handle success differently
//    }
//
//
//    fun signUp(phone: String) {
//        if (phone.isEmpty()) {
//            _eventFlow.emit(UIEvent.ShowError("Please Fill All fields"))
//            return
//        }
//
//        viewModelScope.launch {
//            inProcess = true
//
//            authRepository.sendOtp("https://flashcall.vercel.app/api/v1/resend-otp",phone).fold(
//                { response ->
//                    if (response.success) {
//
//                    } else {
//                        _eventFlow.emit(UIEvent.ShowError(response.message ?: "Failed to send OTP"))
//                    }
//                },
//                {
//                    _eventFlow.emit(UIEvent.ShowError(it.message ?: "Unknown error occurred"))
//                }
//            )
//            inProcess = false
//        }
//    }
//
//    fun resendOTP(phone: String) {
//        if (phone.isEmpty()) {
//            _eventFlow.emit(UIEvent.ShowError("Please Fill All fields"))
//            return
//        }
//
//        viewModelScope.launch {
//            inProcess = true
//            authRepository.resendOtp(phone).fold(
//                { response ->
//                    if (response.success) {
//                        _eventFlow.emit(UIEvent.ShowSuccess(response.message ?: "OTP resent successfully."))
//                    } else {
//                        _eventFlow.emit(UIEvent.ShowError(response.message ?: "Failed to resend OTP"))
//                    }
//                },
//                {
//                    _eventFlow.emit(UIEvent.ShowError(it.message ?: "Unknown error occurred"))
//                }
//            )
//
//            inProcess = false
//        }
//    }
//}
//
//
