package com.example.myapplication.myapplication.flashcall.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.myapplication.flashcall.preferenceStore.userPref
import com.example.myapplication.myapplication.flashcall.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userPref: userPref,
    private val authenticationRepository: AuthRepository
) : ViewModel(){

    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            delay(4000L)
            _isReady.value = true

            try{
                    userToken.stateIn(
                        viewModelScope,
                        SharingStarted.WhileSubscribed(),
                        userPref.getToken().firstOrNull() ?: ""
                    ).collect {
                        Log.d("validateUserData", "validateUser: $it")
                        try {
                            Log.d("validateUserData2", "validateUser: $it")
                            if (it != null) {
                                authenticationRepository.validateUser(
                                    "https://flashcall.vercel.app/api/v1/validate",
                                    it
                                ).collect {
                                    if (it.message == "Token validated successfully") {
                                        _navigationEvent.emit(NavigationEvent.NavigateToHome)
                                    } else {
                                        _navigationEvent.emit(NavigationEvent.NavigateToRegistration)
                                    }
                                    Log.e("validateUser", "validateUser: $it")
                                }
                            }
                        } catch (e: Exception) {
                            Log.e("validateUser", "validateUser: $e")
                        }
                    }


            }catch (e:Exception){
                Log.e("validateUser", "validateUser: $e")

            }

        }
    }

    sealed class NavigationEvent {
        object NavigateToHome : NavigationEvent()
        object NavigateToRegistration : NavigationEvent()
    }


    val userToken = userPref.getToken()

    fun saveToken(token: String) {
        viewModelScope.launch {
            userPref.saveToken(token)
        }
    }
}