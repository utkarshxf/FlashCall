package com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel

data class SendMessageDto(
    val to: String?,
    val notification: NotificationBody
)

data class NotificationBody(
    val title: String,
    val body: String
)
