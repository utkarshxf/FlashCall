package com.example.myapplication.myapplication.flashcall.ViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.myapplication.flashcall.Data.model.APIResponse
import com.example.myapplication.myapplication.flashcall.repository.KycRepository
import com.example.myapplication.myapplication.flashcall.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KycViewModel @Inject constructor(private val repository: KycRepository,
                                       private val userPreferencesRepository: UserPreferencesRepository,
                                       @ApplicationContext private val context: Context
): ViewModel() {


    private val _panState = MutableStateFlow<APIResponse<String>>(APIResponse.Empty)
    val panState: StateFlow<APIResponse<String>> = _panState





    fun makeStateEmpty() {
        _panState.value = APIResponse.Empty
    }

    fun makePanStateLoading(){
        _panState.value = APIResponse.Loading
    }

    fun makePanStateError(){
        _panState.value = APIResponse.Error<String>("Error Message")
    }

    fun makePanStateSuccess(pan: String, loading: (Boolean) -> Unit) {
        viewModelScope.launch {
            loading(true)
            delay(5000L)
            repository.verifyPan(pan).collect{
                loading(false)
                _panState.value = APIResponse.Success<String>("Success")
            }

        }
    }


    fun panVerification(panNumber: String, loading: (Boolean) -> Unit) {
        viewModelScope.launch {
            loading(true)
            repository.verifyPan("").collect{ value ->
                Log.d("print","value: $value")
                _panState.value = APIResponse.Success(value)
                loading(false)
            }
        }
    }

}