package com.example.myapplication.myapplication.flashcall.ViewModel

import android.content.Context
import android.content.SharedPreferences
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
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(private val repository: CreateRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    //sucess
    //APIResponseState = Sucess->CreateUserResponse Data
    private val _createUserState = MutableStateFlow<APIResponse<CreateUserResponse>>(APIResponse.Empty)

    //
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
                    username,
                    "0678349",
                    fullName,
                    firstName,
                    lastName,
                    photo,
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
                    Log.d("UserResponseValue1", "${_createUserState.value}")
                    Log.d("UserResponseValue","${createUserState.value}")

                    storeResponseInPreferences(it)
                    navController.navigate(ScreenRoutes.MainScreen.route)
                }
            }catch (e: Exception){
                Log.d("error", "UserCreatedNot")
                _createUserState.value = APIResponse.Error(e.message?:"USER CREATION ERROR")
            }
        }
    }
    private fun storeResponseInPreferences(response: CreateUserResponse) {
        sharedPreferences.edit().apply {
            putString("username", response.username)
            putString("phone", response.phone)
            putString("fullName", response.fullName)
            putString("firstName", response.firstName)
            putString("lastName", response.lastName)
            putString("photo", response.photo)
            putString("profession", response.profession)
            putString("themeSelected", response.themeSelected)
            putString("videoRate", response.videoRate)
            putString("audioRate", response.audioRate)
            putString("chatRate", response.chatRate)
            putString("gender", response.gender)
            putString("dob", response.dob)
            putString("bio", response.bio)
            putString("_id", response._id)
            putString("createdAt", response.createdAt)
            putString("updatedAt", response.updatedAt)
            putString("__v", response.__v)
            apply()
        }
    }
}

