package com.example.myapplication.myapplication.flashcall.Screens.chats

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.ChatRequestDataClass
import com.example.myapplication.myapplication.flashcall.Screens.IncomingChatScreen
import com.example.myapplication.myapplication.flashcall.ViewModel.chats.ChatRequestViewModel
import com.example.myapplication.myapplication.flashcall.repository.ChatRepository
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@AndroidEntryPoint
class IncomingChatActivity : ComponentActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var chatRepository: ChatRepository
    private var chatRequestListener: ListenerRegistration? = null
    private var pendingChatRequestDocId: String? = null

    private val _incomingChatRequest = MutableStateFlow<ChatRequestDataClass?>(null)
    private val incomingChatRequest: StateFlow<ChatRequestDataClass?> = _incomingChatRequest.asStateFlow()

    private val _chatRequestCreated = MutableStateFlow(false)
    private val chatRequestCreated: StateFlow<Boolean> = _chatRequestCreated.asStateFlow()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        chatRepository = ChatRepository(firestore, storage)

//        val userId = intent.getStringExtra("userId")!!
        val userId = "67042546dd83ba8df5bc6e85"
        listenForChatRequests(userId)

        setContent {
            CompositionLocalProvider(
                androidx.lifecycle.compose.LocalLifecycleOwner provides androidx.compose.ui.platform.LocalLifecycleOwner.current,
            ) {
                val chatRequestCreatedState by chatRequestCreated.collectAsState()
                val chatRequestData by incomingChatRequest.collectAsState()

                if (true) {
                    IncomingChatScreen(
                        chatRequestData?.clientName,
                        onDecline = {
                            rejectChatRequest(pendingChatRequestDocId.toString())
                            finishAndRemoveTask()
                        },
                        onAccept = {
                            acceptChatRequest(pendingChatRequestDocId.toString())
                        }
                    )
                } else {
                    ChatRoomScreen {
                        finishAndRemoveTask()
                    }
                }
            }
        }
    }

    private fun listenForChatRequests(creatorId: String) {
        chatRequestListener = firestore.collection("chatRequests")
            .whereEqualTo("creatorId", creatorId)
            .whereEqualTo("status", "pending")
            .addSnapshotListener { snapshots, e ->
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
            Log.e("ChatRequest", "Error handling chat request change", ex)
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

    private fun acceptChatRequest(chatRequestId: String) {
        updateChatRequestStatus(chatRequestId, "accepted")
    }

    private fun rejectChatRequest(chatRequestId: String) {
        updateChatRequestStatus(chatRequestId, "ended")
    }

    private fun updateChatRequestStatus(chatRequestId: String, status: String) {
        val chatRequestRef = firestore.collection("chatRequests").document(chatRequestId)
        chatRequestRef.update("status", status)
            .addOnSuccessListener {
                if (status == "accepted") {
                    chatRequestRef.get().addOnSuccessListener { document ->
                        document.toObject(ChatRequestDataClass::class.java)?.let {
                            handleAcceptedRequest(it)
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("ChatRequest", "Error updating chat request status", e)
            }
    }

    private fun updateUserChats(chatRequest: ChatRequestDataClass) {
        val creatorChatsRef = firestore.collection("userchats").document(chatRequest.creatorId!!)
        val clientChatsRef = firestore.collection("userchats").document(chatRequest.clientId!!)

        val chatInfo = hashMapOf(
            "chatId" to chatRequest.chatId,
            "lastMessage" to "",
            "receiverId" to chatRequest.clientId,
            "updatedAt" to System.currentTimeMillis(),
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
                    "maxChatDuration" to 3600,
                    "startedAt" to System.currentTimeMillis(),
                    "endedAt" to null,
                    "walletBalance" to chatRequest.clientBalance,
                    "timeLeft" to null,
                    "timeUtilized" to 0
                )
                chatRef.set(chatData)
            }
        }
    }

    private fun saveChatIdToPreferences(chatId: String) {
        val sharedPreferences = getSharedPreferences("ChatPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("chatId", chatId).apply()
    }

    private fun getChatIdFromPreferences(): String? {
        val sharedPreferences = getSharedPreferences("ChatPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("chatId", null)
    }

    override fun onDestroy() {
        super.onDestroy()
        chatRequestListener?.remove()
    }
}