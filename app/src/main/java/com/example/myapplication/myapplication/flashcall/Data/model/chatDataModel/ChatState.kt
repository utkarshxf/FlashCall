package com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel

data class ChatState(
    val isEnteringToken: Boolean = true,
    val remoteToken: String = "",
    val messageText: String = ""
)
