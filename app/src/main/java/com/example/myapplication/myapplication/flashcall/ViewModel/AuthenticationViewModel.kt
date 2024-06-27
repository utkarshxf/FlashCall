package com.example.myapplication.myapplication.flashcall.ViewModel

import android.util.Log
import android.util.Log.d
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
import com.example.myapplication.myapplication.flashcall.Data.model.ResendOTPResponse
import com.example.myapplication.myapplication.flashcall.Data.model.SendOTPResponseX
import com.example.myapplication.myapplication.flashcall.Data.model.VerifyOTPResponse
import com.example.myapplication.myapplication.flashcall.preferenceStore.userPref
import com.example.myapplication.myapplication.flashcall.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authenticationRepository: AuthRepository,
    private val userPref: userPref
) : ViewModel() {

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

    private val _sendOTPState = MutableStateFlow<APIResponse<SendOTPResponseX>>(APIResponse.Empty)
    val sendOTPState : StateFlow<APIResponse<SendOTPResponseX>> = _sendOTPState

    private val _resendOTPState = MutableStateFlow<APIResponse<ResendOTPResponse>>(APIResponse.Empty)
    val resendOTPState : StateFlow<APIResponse<ResendOTPResponse>> = _resendOTPState

    private val _verifyOTPState = MutableStateFlow<APIResponse<VerifyOTPResponse>>(APIResponse.Empty)
    val verifyOTPState : StateFlow<APIResponse<VerifyOTPResponse>> = _verifyOTPState

    private val _phone : MutableState<String> = mutableStateOf("")
    val phoneNumber : State<String> = _phone


    fun signUP(phoneNumber : String, navController: NavController,sendToke:String?)
    {


        viewModelScope.launch {

            _sendOTPState.value = APIResponse.Loading
            try{

                authenticationRepository.sendOtp("https://flashcall.vercel.app/api/v1/send-otp", phoneNumber).collect{
                   if(it==null)
                   {
                       _sendOTPState.value=APIResponse.Error("Exception Occured")
//                       Log.e("SignUpError","Exception Occured")
                   }else{
                       _sendOTPState.value = APIResponse.Success(it)
                       _phone.value = phoneNumber
                       navController.navigate("SignUpOTP")
                   }
                }
            }
            catch (e:Exception)
            {
                _sendOTPState.value = APIResponse.Error(e.message?:"OTP SEND ERROR")
            }

        }
    }

    fun resendOTP(phone : String)
    {

        _resendOTPState.value = APIResponse.Loading
        viewModelScope.launch {
            try {
                authenticationRepository.resendOtp("https://flashcall.vercel.app/api/v1/resend-otp", phone)
                    .collect {
                        _resendOTPState.value = APIResponse.Success(it)
                    }
            }catch (e:Exception)
            {
                _resendOTPState.value = APIResponse.Error(e.message?:"OTP RESEND ERROR")
            }

        }
    }

    fun verifyOTP(phone : String, otp : String, token:String?, navController: NavController) {
        _verifyOTPState.value = APIResponse.Loading
        try {

            viewModelScope.launch {
                Log.e("token", "verifyOTP: $token")
                if (token != null) {
                    authenticationRepository.verifyOtp(
                        "https://flashcall.vercel.app/api/v1/verify-otp", phone, otp, token)
                        .collect {
                            _verifyOTPState.value = APIResponse.Success(it)
                            navController.navigate(ScreenRoutes.RegistrationScreen.route)
                        }
                }
            }
        } catch (e: Exception) {
            _verifyOTPState.value = APIResponse.Error(e.message ?: "OTP VERIFY ERROR")
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

    val userToken = userPref.getToken().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ""
    )


    fun saveToken(token: String) {
        viewModelScope.launch {
            userPref.saveToken(token)
        }
    }

}
