package com.example.myapplication.myapplication.flashcall.repository

import android.net.Uri
import com.example.myapplication.myapplication.flashcall.Data.model.Resource
import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.ChatDataClass
import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.ChatRequestDataClass
import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.MessageDataClass
//import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.toMap
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val firestore : FirebaseFirestore,
    private val storage : FirebaseStorage
) {

    fun getPendingChatRequest(userId: String):
            Flow<Resource<ChatRequestDataClass>> = callbackFlow {

                val listener = firestore.collection("chatRequests")
                    .whereEqualTo("creatorId", userId)
                    .whereEqualTo("status", "pending")
                    .addSnapshotListener{ snapshot, error ->

                        if(error != null) {
                            trySend(Resource.Error(error.localizedMessage ?: "Failed to get pending chat requests"))
                            return@addSnapshotListener
                        }

                        if(snapshot != null){
                            for(documentChange in snapshot.documentChanges) {
                                if(documentChange.type == DocumentChange.Type.ADDED) {
                                    val chatRequest = documentChange.document.toObject(ChatRequestDataClass::class.java)
                                    trySend(Resource.Success(chatRequest))
                                }
                            }
                        }
                    }
        awaitClose {
            listener.remove()
        }
    }.flowOn(Dispatchers.IO)

    fun createChat(chatId : String, clientId : String) {

        val chatData = hashMapOf(
            "clientId" to clientId,
            "createdAt" to FieldValue.serverTimestamp(),
            "messages" to listOf<MessageDataClass>(),
            "status" to "active"
        )

        firestore.collection("chats").document(chatId).set(chatData)

        firestore.collection("chatRequests").document(chatId).update("status", "accepted")

    }

    private suspend fun uploadMedia(mediaUri: Uri, isImage: Boolean) : String {

        val fileName = if(isImage) "images/${UUID.randomUUID()}" else "audio/${UUID.randomUUID()}"

        val ref = storage.reference.child(fileName)

        val uploadTask = ref.putFile(mediaUri)

        return uploadTask.await().storage.downloadUrl.await().toString()

    }

    suspend fun sendMessage(chatId: String, message: MessageDataClass) {

        var updatedMessage = message

        if(message.img != null){

            val imageUrl = uploadMedia(Uri.parse(message.img), isImage = true)
            updatedMessage = message.copy(img = imageUrl)

        } else if (message.audio != null) {

            val audioUrl = uploadMedia(Uri.parse(message.audio), isImage = false)
            updatedMessage = message.copy(audio = audioUrl)
        }

        firestore.collection("chats")
            .document(chatId)
            .update("messages", FieldValue.arrayUnion(updatedMessage)).await()

    }

    fun getMessages(chatId: String) :
            Flow<Resource<List<MessageDataClass>>> = callbackFlow<Resource<List<MessageDataClass>>> {

        val messageListener = firestore.collection("chats").document(chatId)
            .addSnapshotListener{ snapshot, error ->

                if(error != null) {

                    trySend(Resource.Error(error.localizedMessage ?: "Failed to Get Messages"))
                    return@addSnapshotListener
                }

                if(snapshot != null && snapshot.exists()) {

                    val messages = snapshot.toObject(ChatDataClass::class.java)?.messages ?: emptyList()
                    trySend(Resource.Success(messages))

                } else {
                    trySend(Resource.Error("Chat Not Found"))
                }

            }
        awaitClose{
            messageListener.remove()
        }

    }.flowOn(Dispatchers.IO)

    fun markMessageAsSeen(chatId: String, messageToMarkSeen: MessageDataClass) {

        firestore.collection("chats").document(chatId)
            .get()
            .addOnSuccessListener { document ->

                if(document != null && document.exists()) {

                    val messages = document.toObject(ChatDataClass::class.java)?.messages
                    val updatedMessages = messages?.map {message ->

                        if(message == messageToMarkSeen) message.copy(seen = true) else message
                    }

                    firestore.collection("chats").document(chatId).update("messages", updatedMessages)

                }

            }
            .addOnFailureListener {

            }

    }

    fun updateChatRequestStatus(chatRequestId: String, status: String) {
        firestore.collection("chatRequest").document(chatRequestId)
            .update("status", status)
            .addOnSuccessListener { /* Handle success if needed */ }
            .addOnFailureListener {
//                try {
//
//                }catch (e:Exception){
//
//                }
            }
            .addOnFailureListener { exception -> /* Handle error */ }
    }

}