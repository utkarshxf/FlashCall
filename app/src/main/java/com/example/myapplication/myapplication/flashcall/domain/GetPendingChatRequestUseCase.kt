package com.example.myapplication.myapplication.flashcall.domain

import com.example.myapplication.myapplication.flashcall.Data.model.Resource
import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.ChatRequestDataClass
import com.example.myapplication.myapplication.flashcall.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPendingChatRequestUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(creatorId : String) : Flow<Resource<ChatRequestDataClass>> =
        chatRepository.getPendingChatRequest(creatorId)

}