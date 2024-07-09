package com.example.myapplication.myapplication.flashcall.domain

import com.example.myapplication.myapplication.flashcall.Data.MessageType
import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.MessageDataClass
import com.example.myapplication.myapplication.flashcall.repository.ChatRepository
import com.google.firebase.firestore.FieldValue
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(chatId: String, messageContent: String, messageType: MessageType, senderId: String) {
        val newMessage = MessageDataClass(
            createdAt = System.currentTimeMillis(),
            senderId = senderId, // You'll need to implement this function
            seen = false,
            // Set the appropriate content based on messageType
            text = if (messageType == MessageType.TEXT) messageContent else null,
            img = if (messageType == MessageType.IMAGE) messageContent else null,
            audio = if (messageType == MessageType.AUDIO) messageContent else null
        )

//        val newMessage = MessageDataClass(
//            senderId = userId,
//            seen = false,
//            text = if (messageType == MessageType.TEXT) messageContent else null,
//            img = if (messageType == MessageType.IMAGE) messageContent else null,
//            audio = if (messageType == MessageType.AUDIO) messageContent else null
//        )

        val messageMap = hashMapOf(
            "createdAt" to FieldValue.serverTimestamp(),
            "senderId" to newMessage.senderId,
            "seen" to newMessage.seen,
            "text" to newMessage.text,
            "img" to newMessage.img,
            "audio" to newMessage.audio
        )
        chatRepository.sendMessage(chatId, newMessage)
    }
}
