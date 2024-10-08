package com.example.myapplication.myapplication.flashcall.Screens.chats

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.myapplication.flashcall.Screens.IncomingChatScreen
import com.example.myapplication.myapplication.flashcall.ViewModel.chats.ChatRequestViewModel
import com.example.myapplication.myapplication.flashcall.repository.ChatRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IncomingChatActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val firestore = FirebaseFirestore.getInstance()
        val storage = FirebaseStorage.getInstance()
        val chatRepository = ChatRepository(firestore , storage)
        val chatRequestViewModel = ChatRequestViewModel(chatRepository , firestore , this)
        setContent {
            CompositionLocalProvider(
                androidx.lifecycle.compose.LocalLifecycleOwner provides androidx.compose.ui.platform.LocalLifecycleOwner.current,
            ) {
                val chatRequestCreated by chatRequestViewModel.chatRequestCreated.collectAsState()
                Log.d("Inside Activity", chatRequestCreated.toString())
                val chatRequestData by chatRequestViewModel.incomingChatRequest.collectAsState()
                if (chatRequestCreated) {
                    IncomingChatScreen(chatRequestData?.clientName,
                        onDecline = {
                            chatRequestViewModel.rejectChatRequest(chatRequestViewModel.pendingChatRequestDocId.toString())
                            finishAndRemoveTask()
                        }, onAccept = {
                            chatRequestViewModel.acceptChatRequest(chatRequestViewModel.pendingChatRequestDocId.toString())
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
}