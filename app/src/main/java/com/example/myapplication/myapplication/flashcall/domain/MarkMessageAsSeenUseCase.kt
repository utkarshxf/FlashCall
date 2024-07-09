package com.example.myapplication.myapplication.flashcall.domain

import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.MessageDataClass
import com.example.myapplication.myapplication.flashcall.repository.ChatRepository
import javax.inject.Inject

class MarkMessageAsSeenUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(chatId: String, message: MessageDataClass) {
        chatRepository.markMessageAsSeen(chatId, message)
    }
}