package com.example.myapplication.myapplication.flashcall.repository

import android.net.Uri
import android.util.Log
import com.example.myapplication.myapplication.flashcall.Data.model.Resource
import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.ChatDataClass
import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.ChatRequestDataClass
import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.MessageDataClass
import com.google.firebase.Timestamp
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

    fun fetchChatData(chatId: String): Flow<ChatDataClass?> = callbackFlow {
        val listenerRegistration = firestore.collection("chats").document(chatId)
            .addSnapshotListener { documentSnapshot, e ->
                if (e != null) {
                    Log.e("ChatViewModel", "Error fetching chat data", e)
                    close(e) // Close the flow if there's an error
                    return@addSnapshotListener
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    val data = documentSnapshot.data ?: return@addSnapshotListener
                    val chat = ChatDataClass(
                        clientId = data["clientId"] as? String ?: "",
                        clientName = data["clientName"] as? String,
                        clientBalance = data["clientBalance"] as? Double,
                        creatorId = data["creatorId"] as? String ?: "",
                        endedAt = data["endedAt"] as? Long,
                        maxChatDuration = data["maxChatDuration"] as? Int,
                        startedAt = data["startedAt"] as? Long,
                        status = data["status"] as? String,
                        timeLeft = data["timeLeft"] as? Double,
                        timeUtilized = data["timeUtilized"] as? Double,
                        messages = (data["messages"] as? List<Map<String, Any>> ?: emptyList()).map { messageData ->
                            MessageDataClass(
                                text = messageData["text"] as? String,
                                audio = messageData["audio"] as? String,
                                img = messageData["img"] as? String,
                                createdAt = messageData["createdAt"] as? Long,
                                seen = messageData["seen"] as? Boolean ?: false,
                                senderId = messageData["senderId"] as? String
                            )
                        }
                    )
                    // Emit the chat data to the flow collector
                    trySend(chat)
                } else {
                    trySend(null) // Emit null if document doesn't exist
                }
            }

        // Clean up the listener when flow collection is done
        awaitClose { listenerRegistration.remove() }
    }
    fun createChat(chatId : String, clientId : String) {
        val chatData = hashMapOf(
            "clientId" to clientId,
            "createdAt" to FieldValue.serverTimestamp(),
            "messages" to listOf<MessageDataClass>(),
            "status" to "active"
        )
        Log.d("Firestore", "New Document Created")

        firestore.collection("chats").document(chatId).set(chatData)

        firestore.collection("chatRequests").document(chatId).update("status", "accepted")

    }

    private suspend fun uploadMedia(mediaUri: Uri, isImage: Boolean) : String {

        val fileName = if(isImage) "images/${UUID.randomUUID()}" else "audio/${UUID.randomUUID()}"
        val ref = storage.reference.child(fileName)
        val uploadTask = ref.putFile(mediaUri)


        val data = uploadTask.await().storage.downloadUrl.await().toString()

        Log.v("messagesend123" , data.toString())
        return data
    }
    suspend fun sendMessage(chatId: String, message: MessageDataClass) {
        var updatedMessage = message

        try {
            // Check if the document exists
            val document = firestore.collection("chats").document(chatId).get().await()
            if (!document.exists()) {
                // If the chat document doesn't exist, create it
                Log.d("Firestore", "Message sent successfully")
                createChat(chatId, message.senderId.toString())  // Ensure you pass the correct clientId here
                createChatRequest(chatId, message.senderId.toString(), message.senderId.toString())
            }
            // Handle image and audio media upload
            if (message.img != null) {
                val imageUrl = uploadMedia(Uri.parse(message.img), isImage = true)
                updatedMessage = message.copy(img = imageUrl)
            } else if (message.audio != null) {
                try {
                    val audioUrl = uploadMedia(Uri.parse(message.audio), isImage = false)
                    updatedMessage = message.copy(audio = audioUrl)
                } catch (e: Exception) {
                    //needed to be fixed.
                    firestore.collection("chats")
                        .document(chatId)
                        .update("messages", FieldValue.arrayUnion(updatedMessage)).await()
                }
            }
            firestore.collection("chats")
                .document(chatId)
                .update("messages", FieldValue.arrayUnion(updatedMessage)).await()

        } catch (e: Exception) {
            Log.v("messagesend123",updatedMessage.toString())
            Log.e("messagesend123", "Failed to send message: ${e.message}")
        }
    }
    fun createChatRequest(chatId: String, clientId: String, creatorId: String) {
        val chatRequestData = hashMapOf(
            "chatId" to chatId,
            "clientId" to clientId,
            "creatorId" to creatorId,
            "status" to "pending",
            "createdAt" to FieldValue.serverTimestamp()
        )

        firestore.collection("chatRequests").document(chatId).set(chatRequestData)
            .addOnSuccessListener {
                Log.d("Firestore", "Chat request created successfully")
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreError", "Failed to create chat request: ${e.message}")
            }
    }
    suspend fun logAllDocumentsInCollection(collectionName: String) {
        try {
            val documentsSnapshot = firestore.collection(collectionName).get().await()

            // Check if there are any documents in the collection
            if (!documentsSnapshot.isEmpty) {
                for (document in documentsSnapshot.documents) {
                    Log.d("FirestoreDocument", "Document ID: ${document.id}")
                    Log.d("FirestoreDocument", "Document Data: ${document.data}")
                }
            } else {
                Log.d("FirestoreDocument", "No documents found in the collection.")
            }

        } catch (e: Exception) {
            Log.e("FirestoreError", "Failed to retrieve documents: ${e.message}")
        }
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