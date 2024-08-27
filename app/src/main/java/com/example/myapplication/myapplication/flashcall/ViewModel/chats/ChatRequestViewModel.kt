package com.example.myapplication.myapplication.flashcall.ViewModel.chats

//import com.example.myapplication.myapplication.flashcall.Screens.uid
//import com.example.myapplication.myapplication.flashcall.Screens.userId
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.myapplication.flashcall.BaseClass
import com.example.myapplication.myapplication.flashcall.Data.model.Resource
import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.ChatRequestDataClass
import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.MessageDataClass
import com.example.myapplication.myapplication.flashcall.R
import com.example.myapplication.myapplication.flashcall.domain.CreateChatUseCase
import com.example.myapplication.myapplication.flashcall.domain.GetMessageUseCase
import com.example.myapplication.myapplication.flashcall.domain.GetPendingChatRequestUseCase
import com.example.myapplication.myapplication.flashcall.domain.RejectChatRequestUseCase
import com.example.myapplication.myapplication.flashcall.repository.ChatRepository
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatRequestViewModel @Inject constructor(
    getMessageUseCase: GetMessageUseCase,
    private val chatRepository: ChatRepository,
    private val getChatRequestUseCase: GetPendingChatRequestUseCase,
    private val createChatRequestUseCase: CreateChatUseCase,
    private val rejectChatRequestUseCase: RejectChatRequestUseCase,
    private val firestore : FirebaseFirestore,
    @ApplicationContext private val context: Context

) : ViewModel() {

    private val _pendingChatRequest = MutableStateFlow<Resource<ChatRequestDataClass>>(Resource.Loading())
    val pendingChatRequest : StateFlow<Resource<ChatRequestDataClass>> = _pendingChatRequest.asStateFlow()
    private val _chatRequestCreated = MutableStateFlow(false)
    val chatRequestCreated: StateFlow<Boolean> = _chatRequestCreated.asStateFlow()

    private var chatRequestListener: ListenerRegistration? = null

    // LiveData or State to observe chat requests
    val _incomingChatRequest = MutableStateFlow<ChatRequestDataClass?>(null)
    val incomingChatRequest: StateFlow<ChatRequestDataClass?> = _incomingChatRequest.asStateFlow()

    private val _chatMessages = MutableStateFlow<Resource<List<MessageDataClass>>>(Resource.Loading())
    val chatMessages: StateFlow<Resource<List<MessageDataClass>>> = _chatMessages.asStateFlow()

    private var pendingChatRequestDoc: DocumentSnapshot? = null
    var pendingChatRequestDocId: String? = null

//    init {
//
//        viewModelScope.launch {
//            getChatRequestUseCase("6675197dc56dfe13b3ccabd3").collect{ result->
//                _pendingChatRequest.value = result
//            }
//        }
//
//    }


    // Other properties and initializations...

    fun createChatRequest(chatId: String, clientId: String, creatorId: String) {
        val chatRequestData = hashMapOf(
            "chatId" to chatId,
            "clientId" to clientId,
            "creatorId" to creatorId,
            "status" to "pending",
            "createdAt" to FieldValue.serverTimestamp()
        )

        firestore.collection("chatRequests").add(chatRequestData)
            .addOnSuccessListener { documentReference ->
                Log.d("Firestore", "Chat request created successfully with ID: ${documentReference.id}")
                _chatRequestCreated.value = true  // Update the state to notify the UI
                sendNotification("Chat Request Created", "A new chat request has been created.")
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreError", "Failed to create chat request: ${e.message}")
            }
    }
    fun listenForChatRequests(clientId: String) {
        chatRequestListener = firestore.collection("chatRequests")
            .whereEqualTo("creatorId", clientId)
            .whereEqualTo("status", "pending")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.e("FirestoreError", "Error fetching chat requests: ${e.message}")
                    return@addSnapshotListener
                }

                if (snapshots != null && !snapshots.isEmpty) {
                    Log.d("FirestoreSnapshot", "Received snapshot with ${snapshots.documentChanges.size} changes")
                    for (doc in snapshots.documentChanges) {
                        when (doc.type) {
                            DocumentChange.Type.ADDED -> {
                                Log.d("FirestoreSnapshot", "Document added with ID: ${doc.document.id}")
                            }
                            DocumentChange.Type.MODIFIED -> {
                                Log.d("FirestoreSnapshot", "Document modified with ID: ${doc.document.id}")
                            }
                            DocumentChange.Type.REMOVED -> {
                                Log.d("FirestoreSnapshot", "Document removed with ID: ${doc.document.id}")
                            }
                        }

                        try {
                            val chatRequest = doc.document.toObject(ChatRequestDataClass::class.java)
                            Log.d("ChatRequest", "Chat Request: $chatRequest")


                            if (chatRequest.status == "pending") {
                                // Handle the pending request
                                _incomingChatRequest.value = chatRequest
                                _chatRequestCreated.value = true
                                pendingChatRequestDocId = doc.document.id
                                Log.d("Document", "Document Id: $pendingChatRequestDocId")
                                Log.d("ChatRequest", "Chat Request: $_incomingChatRequest")
                                chatRequest.chatId?.let { saveChatIdToPreferences(context, chatRequest.chatId) }
                            } else if (chatRequest.status == "accepted") {
                                // Handle the accepted request and remove the listener or stop further actions for pending requests
                                Log.d("ChatRequest", "Chat Request has been accepted: ${chatRequest.chatId}")

                                // Fetch messages related to the chat request
                                chatRequest.chatId?.let { fetchMessagesForChat(it) }


                                // Optionally stop listening for pending requests
                                chatRequestListener?.remove()
                            }

                        } catch (ex: Exception) {
                            Log.e("FirestoreError", "Error deserializing chat request: ${ex.message}")
                        }
                    }
                } else {
                    Log.d("FirestoreSnapshot", "No pending chat requests found.")
                    _chatRequestCreated.value = false
                }
            }
    }

    private fun fetchMessagesForChat(chatId: String) {
        viewModelScope.launch {
            firestore.collection("chats")
                .document(chatId)
                .collection("messages")
                .orderBy("createdAt", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val messages = querySnapshot.documents.mapNotNull { it.toObject(MessageDataClass::class.java) }
                    _chatMessages.value = Resource.Success(messages)
                    Log.d("Firestore", "Fetched ${messages.size} messages for chatId: $chatId")
                    Log.d("Firestore", "Fetched $messages messages for chatId: $chatId")

                }
                .addOnFailureListener { e ->
                    _chatMessages.value = Resource.Error(e.message ?: "Failed to fetch messages")
                    Log.e("FirestoreError", "Error fetching messages for chatId: $chatId, ${e.message}")
                }
        }
    }
    fun saveChatIdToPreferences(context: Context, chatId: String) {
        val sharedPreferences = context.getSharedPreferences("ChatPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("chatId", chatId).apply()
    }

    fun getChatIdFromPreferences(): String? {
        val sharedPreferences = context.getSharedPreferences("ChatPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("chatId", null)
    }

    fun acceptChatRequest(chatRequestId: String) {
        val chatRequestRef = firestore.collection("chatRequests").document(chatRequestId)
        chatRequestRef.update("status", "accepted")
            .addOnSuccessListener {
                Log.d("FirestoreSuccess", "Chat request accepted successfully.")
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreError", "Error accepting chat request: ${e.message}")
            }
    }
    fun rejectChatRequest(chatRequestId: String) {
        val chatRequestRef = firestore.collection("chatRequests").document(chatRequestId)
        chatRequestRef.update("status", "ended")
            .addOnSuccessListener {
                Log.d("FirestoreSuccess", "Chat request accepted successfully.")
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreError", "Error accepting chat request: ${e.message}")
            }
    }

    // Function to fetch messages based on chatId

    override fun onCleared() {
        super.onCleared()
        chatRequestListener?.remove()
    }
    private fun sendNotification(title: String, message: String) {
        val builder = NotificationCompat.Builder(context, BaseClass.CHANNEL_ID)
            .setSmallIcon(R.drawable.splash_final) // Set your notification icon
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        )

        notificationManager?.notify(1, builder.build()) // You can use a unique ID for each notification
        Log.d("Inside Notification", "Notification sent")
    }

    fun clearPendingChatRequest() {
        _pendingChatRequest.value = Resource.Loading() // Or any other appropriate initial state
    }

//    fun acceptChatRequest(chatRequestDataClass: ChatRequestDataClass){
//
//        viewModelScope.launch {
//            try {
//
//                createChatRequestUseCase(
//                    chatId = chatRequestDataClass.chatId!!,
//                    clientId = chatRequestDataClass.clientId!!,
//                    creatorId = chatRequestDataClass.creatorId!!
//                    )
//                Log.d("ChatRoomCreated", "acceptChatRequest: ${chatRequestDataClass.chatId}")
//            } catch (e : Exception){
//                Log.d("ChatRoomCreateError", "$e")
//            }
//        }
//    }

//    fun rejectChatRequest(chatRequestId: String) {
//        viewModelScope.launch {
//            try {
//                rejectChatRequestUseCase(chatRequestId)
//                // Update UI state to indicate success or remove the request
//                Log.d("RejectedChat", "RejectChatRequest")
//
//            } catch (e: Exception) {
//                // Handle error (e.g., show error message)
//                Log.d("ChatRejectionError", "$e")
//            }
//        }
//    }
}