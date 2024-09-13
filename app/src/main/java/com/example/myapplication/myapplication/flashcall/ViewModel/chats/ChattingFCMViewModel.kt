package com.example.myapplication.myapplication.flashcall.ViewModel.chats

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.ChatState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChattingFCMViewModel @Inject constructor(): ViewModel() {
    var state by mutableStateOf(ChatState())
        private set

    fun onRemoteTokenChange(newToken:String){
        state = state.copy(
            remoteToken = newToken
        )
    }
    fun onSubmitChange(){
        state = state.copy(
            isEnteringToken = false
        )
    }
    fun onMessageChange(message: String){
        state = state.copy(
            messageText = message
        )
    }
}