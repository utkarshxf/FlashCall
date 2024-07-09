package com.example.myapplication.myapplication.flashcall.ViewModel.chats

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.myapplication.flashcall.Data.model.Resource
import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.ChatRequestDataClass
import com.example.myapplication.myapplication.flashcall.Screens.creatorUid
//import com.example.myapplication.myapplication.flashcall.Screens.uid
//import com.example.myapplication.myapplication.flashcall.Screens.userId
import com.example.myapplication.myapplication.flashcall.domain.CreateChatUseCase
import com.example.myapplication.myapplication.flashcall.domain.GetPendingChatRequestUseCase
import com.example.myapplication.myapplication.flashcall.domain.RejectChatRequestUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatRequestViewModel @Inject constructor(
    private val getChatRequestUseCase: GetPendingChatRequestUseCase,
    private val createChatRequestUseCase: CreateChatUseCase,
    private val rejectChatRequestUseCase: RejectChatRequestUseCase
) : ViewModel() {

    private val _pendingChatRequest = MutableStateFlow<Resource<ChatRequestDataClass>>(Resource.Loading())
    val pendingChatRequest : StateFlow<Resource<ChatRequestDataClass>> = _pendingChatRequest.asStateFlow()

    init {

        viewModelScope.launch {
            getChatRequestUseCase("6675197dc56dfe13b3ccabd3").collect{ result->
                _pendingChatRequest.value = result
            }
        }

    }

    fun clearPendingChatRequest() {
        _pendingChatRequest.value = Resource.Loading() // Or any other appropriate initial state
    }

    fun acceptChatRequest(chatRequestDataClass: ChatRequestDataClass){

        viewModelScope.launch {
            try {

                createChatRequestUseCase(
                    chatId = chatRequestDataClass.chatId!!,
                    clientId = chatRequestDataClass.clientId!!,
                    creatorId = chatRequestDataClass.creatorId!!
                    )
                Log.d("ChatRoomCreated", "acceptChatRequest: ${chatRequestDataClass.chatId}")
            } catch (e : Exception){
                Log.d("ChatRoomCreateError", "$e")
            }
        }
    }

    fun rejectChatRequest(chatRequestId: String) {
        viewModelScope.launch {
            try {
                rejectChatRequestUseCase(chatRequestId)
                // Update UI state to indicate success or remove the request
                Log.d("RejectedChat", "RejectChatRequest")

            } catch (e: Exception) {
                // Handle error (e.g., show error message)
                Log.d("ChatRejectionError", "$e")
            }
        }
    }
}