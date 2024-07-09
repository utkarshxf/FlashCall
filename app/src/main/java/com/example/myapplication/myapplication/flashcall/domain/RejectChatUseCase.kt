package com.example.myapplication.myapplication.flashcall.domain

import com.example.myapplication.myapplication.flashcall.repository.ChatRepository
import javax.inject.Inject

class RejectChatRequestUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(chatRequestId: String) {
        chatRepository.updateChatRequestStatus(chatRequestId, "rejected")
    }
}