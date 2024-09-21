package com.example.myapplication.myapplication.flashcall.ViewModel.chats

//import com.example.myapplication.myapplication.flashcall.Screens.uid
//import com.example.myapplication.myapplication.flashcall.Screens.userId
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.example.myapplication.myapplication.flashcall.BaseClass
import com.example.myapplication.myapplication.flashcall.Data.model.Resource
import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.ChatRequestDataClass
import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.MessageDataClass
import com.example.myapplication.myapplication.flashcall.R
import com.example.myapplication.myapplication.flashcall.repository.ChatRepository
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class ChatRequestViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val firestore: FirebaseFirestore,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _pendingChatRequest =
        MutableStateFlow<Resource<ChatRequestDataClass>>(Resource.Loading())
    val pendingChatRequest: StateFlow<Resource<ChatRequestDataClass>> =
        _pendingChatRequest.asStateFlow()

    private val _chatRequestCreated = MutableStateFlow(false)
    val chatRequestCreated: StateFlow<Boolean> = _chatRequestCreated.asStateFlow()

    private val _incomingChatRequest = MutableStateFlow<ChatRequestDataClass?>(null)
    val incomingChatRequest: StateFlow<ChatRequestDataClass?> = _incomingChatRequest.asStateFlow()

    private val _chatMessages =
        MutableStateFlow<Resource<List<MessageDataClass>>>(Resource.Loading())
    val chatMessages: StateFlow<Resource<List<MessageDataClass>>> = _chatMessages.asStateFlow()

    private var chatRequestListener: ListenerRegistration? = null
    var pendingChatRequestDocId: String? = null

    fun listenForChatRequests(creatorId: String) {
        chatRequestListener =
            firestore.collection("chatRequests").whereEqualTo("creatorId", creatorId)
                .whereEqualTo("status", "pending").addSnapshotListener { snapshots, e ->
                    if (e != null) {
                        return@addSnapshotListener
                    }
                    if (snapshots != null && !snapshots.isEmpty) {
                        for (doc in snapshots.documentChanges) {
                            when (doc.type) {
                                DocumentChange.Type.ADDED -> handleChatRequestChange(doc.document)
                                DocumentChange.Type.MODIFIED -> handleChatRequestChange(doc.document)
                                DocumentChange.Type.REMOVED -> {}
                            }
                        }
                    } else {
                        _chatRequestCreated.value = false
                    }
                }
    }

    private fun handleChatRequestChange(document: DocumentSnapshot) {
        try {
            val chatRequest = document.toObject(ChatRequestDataClass::class.java)
            Log.d("ChatRequest", "Chat Request: $chatRequest")

            when (chatRequest?.status) {
                "pending" -> handlePendingRequest(chatRequest, document.id)
                "accepted" -> handleAcceptedRequest(chatRequest)
                else -> {}
            }
        } catch (ex: Exception) {
        }
    }

    private fun handlePendingRequest(chatRequest: ChatRequestDataClass, documentId: String) {
        _incomingChatRequest.value = chatRequest
        _chatRequestCreated.value = true
        pendingChatRequestDocId = documentId
        chatRequest.chatId?.let { saveChatIdToPreferences(it) }
    }

    private fun handleAcceptedRequest(chatRequest: ChatRequestDataClass) {
        chatRequest.chatId?.let {
            updateUserChats(chatRequest)
            createOrUpdateChat(chatRequest)
        }
        chatRequestListener?.remove()
    }

    fun acceptChatRequest(chatRequestId: String) {
        updateChatRequestStatus(chatRequestId, "accepted")
    }

    private fun updateChatRequestStatus(chatRequestId: String, status: String) {
        val chatRequestRef = firestore.collection("chatRequests").document(chatRequestId)
        chatRequestRef.update("status", status).addOnSuccessListener {
                if (status == "accepted") {
                    chatRequestRef.get().addOnSuccessListener { document ->
                        document.toObject(ChatRequestDataClass::class.java)?.let {
                            handleAcceptedRequest(it)
                        }
                    }
                }
            }.addOnFailureListener { e ->

            }
    }

    private fun updateUserChats(chatRequest: ChatRequestDataClass) {
        val creatorChatsRef = firestore.collection("userchats").document(chatRequest.creatorId!!)
        val clientChatsRef = firestore.collection("userchats").document(chatRequest.clientId!!)

        val chatInfo = hashMapOf(
            "chatId" to chatRequest.chatId,
            "lastMessage" to "",
            "receiverId" to chatRequest.clientId,
            "updatedAt" to 1726930464549,
            "online" to false
        )

        creatorChatsRef.update("chats", FieldValue.arrayUnion(chatInfo))
        chatInfo["receiverId"] = chatRequest.creatorId
        clientChatsRef.update("chats", FieldValue.arrayUnion(chatInfo))
    }

    private fun createOrUpdateChat(chatRequest: ChatRequestDataClass) {
        val chatRef = firestore.collection("chats").document(chatRequest.chatId!!)

        chatRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                chatRef.update(
                    mapOf(
                        "status" to "active",
                        "maxChatDuration" to 3600,
                        "walletBalance" to chatRequest.clientBalance
                    )
                )
            } else {
                val chatData = hashMapOf(
                    "clientId" to chatRequest.clientId,
                    "clientName" to chatRequest.clientName,
                    "creatorId" to chatRequest.creatorId,
                    "status" to "active",
                    "messages" to listOf<String>(),
                    "maxChatDuration" to null,
                    "startedAt" to 1726935411944,
                    "endedAt" to null,
                    "walletBalance" to chatRequest.clientBalance,
                    "timeLeft" to null,
                    "timeUtilized" to 0
                )
                chatRef.set(chatData)
            }
        }
    }

    fun saveChatIdToPreferences(chatId: String) {
        val sharedPreferences = context.getSharedPreferences("ChatPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("chatId", chatId).apply()
    }

    fun getChatIdFromPreferences(): String? {
        val sharedPreferences = context.getSharedPreferences("ChatPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("chatId", null)
    }


    fun rejectChatRequest(chatRequestId: String) {
        updateChatRequestStatus(chatRequestId, "ended")
    }


    fun clearPendingChatRequest() {
        _pendingChatRequest.value = Resource.Loading()
    }

    private fun sendNotification(title: String, message: String) {
        val builder = NotificationCompat.Builder(context, BaseClass.CHANNEL_ID)
            .setSmallIcon(R.drawable.splash_final).setContentTitle(title).setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = ContextCompat.getSystemService(
            context, NotificationManager::class.java
        )

        notificationManager?.notify(1, builder.build())
        Log.d("Inside Notification", "Notification sent")
    }

    override fun onCleared() {
        super.onCleared()
        chatRequestListener?.remove()
    }
}