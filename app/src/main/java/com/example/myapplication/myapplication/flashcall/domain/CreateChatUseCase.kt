package com.example.myapplication.myapplication.flashcall.domain

import com.example.myapplication.myapplication.flashcall.repository.ChatRepository
import javax.inject.Inject

class CreateChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {
    suspend operator fun invoke(chatId : String, clientId : String, creatorId : String) =
        chatRepository.createChat(chatId, clientId)
}