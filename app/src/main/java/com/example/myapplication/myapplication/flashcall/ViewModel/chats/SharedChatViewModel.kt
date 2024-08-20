package com.example.myapplication.myapplication.flashcall.ViewModel.chats

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class SharedChatViewModel @Inject constructor() : ViewModel() {

    // LiveData or StateFlow to hold chatId
    private val _chatId = MutableStateFlow<String?>(null)
    val chatId: StateFlow<String?> = _chatId.asStateFlow()

    // Function to update chatId
    fun setChatId(id: String) {
        _chatId.value = id
    }
}