package com.example.myapplication.myapplication.flashcall.repository

import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.ChatDataClass
import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.ChatRequestDataClass
import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.MessageDataClass
import kotlinx.coroutines.flow.Flow

interface IChatRepo {

    fun getPendingChatRequest() : Flow<ChatRequestDataClass?>

    suspend fun startChat(chatRequest : ChatRequestDataClass)

    fun sendMessage(message : MessageDataClass, chatId : String)

    fun listenToChatUpdates(chatId : String) : Flow<ChatDataClass>

    fun listenToNewChatRequest() : Flow<ChatRequestDataClass>

//    fun getDraftMessage(message: MessageDataClass, chatI) :

}
