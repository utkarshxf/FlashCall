package com.example.myapplication.myapplication.flashcall.Screens.chats

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import com.example.myapplication.myapplication.flashcall.Screens.IncomingChatScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IncomingChatActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CompositionLocalProvider(
                androidx.lifecycle.compose.LocalLifecycleOwner provides androidx.compose.ui.platform.LocalLifecycleOwner.current,
            ) {
                    IncomingChat()
            }
        }
    }
}