package com.example.myapplication.myapplication.flashcall.ViewModel

import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.Data.model.APIResponse
import com.example.myapplication.myapplication.flashcall.Data.model.CreateUser
import com.example.myapplication.myapplication.flashcall.Data.model.CreateUserResponse
import com.example.myapplication.myapplication.flashcall.Data.model.VerifyOTPResponse
import com.example.myapplication.myapplication.flashcall.repository.CreateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(private val repository: CreateRepository) : ViewModel() {

    private val _createUserState = MutableStateFlow<APIResponse<CreateUserResponse>>(APIResponse.Empty)
    val createUserState : StateFlow<APIResponse<CreateUserResponse>> = _createUserState

    fun createUser(
        username: String,
        phone: String,
        fullName: String,
        firstName: String,
        lastName: String,
        photo: String,
        profession: String,
        themeSelected: String,
        videoRate: String,
        audioRate: String,
        chatRate: String,
        gender: String,
        dob: String,
        bio: String,
        kyc_status: String,
        navController: NavController
    ){


        viewModelScope.launch {

            _createUserState.value = APIResponse.Loading

            try{

                repository.createUser("https://flashcall.vercel.app/api/v1/creator/createUser",
                    "123@156",
                    "999937",
                    fullName,
                    firstName,
                    lastName,
                    "https://cdn.britannica.com/72/223172-131-C3F72804/astrology-horoscope-circle.jpg",
                    profession,
                    themeSelected,
                    videoRate,
                    audioRate,
                    chatRate,
                    gender,
                    dob,
                    bio,
                    kyc_status
                ).collect {
                    _createUserState.value = APIResponse.Success(it)
                    Log.d("User", "UserCreated")
                    navController.navigate(ScreenRoutes.HomeScreen.route)
                }
            }catch (e: Exception){
                Log.d("error", "UserCreatedNot")
                _createUserState.value = APIResponse.Error(e.message?:"USER CREATION ERROR")
            }
        }
    }
}