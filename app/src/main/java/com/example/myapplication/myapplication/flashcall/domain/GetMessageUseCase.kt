package com.example.myapplication.myapplication.flashcall.domain

import com.example.myapplication.myapplication.flashcall.Data.model.Resource
import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.MessageDataClass
import com.example.myapplication.myapplication.flashcall.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMessageUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {

    operator fun invoke(chatId: String): Flow<Resource<List<MessageDataClass>>> =
        chatRepository.getMessages(chatId)
}