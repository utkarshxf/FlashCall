package com.example.myapplication.myapplication.flashcall.domain

import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.ChatRequestDataClass
import com.example.myapplication.myapplication.flashcall.repository.ChatRepository
import javax.inject.Inject


class AcceptChatRequestUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(chatRequest: ChatRequestDataClass) {
        chatRepository.createChat(chatRequest.chatId!!, chatRequest.clientId!!)
    }
}