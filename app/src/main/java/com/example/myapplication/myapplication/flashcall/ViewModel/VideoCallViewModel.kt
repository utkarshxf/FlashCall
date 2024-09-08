package com.example.myapplication.myapplication.flashcall.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.myapplication.flashcall.Data.model.SDKResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.video.android.core.Call
import io.getstream.video.android.core.StreamVideo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoCallViewModel @Inject constructor() : ViewModel() {
    private val streamVideo = StreamVideo.instance()

    private val _videoMutableUiState = MutableStateFlow<SDKResponseState>(SDKResponseState.Loading)
    val videoMutableUiState : StateFlow<SDKResponseState> = _videoMutableUiState

    private val _incomingCall: MutableStateFlow<Call?> = MutableStateFlow(null)
    val incomingCall: StateFlow<Call?> = _incomingCall

    private val _activeCall: MutableStateFlow<Call?> = MutableStateFlow(null)
    val activeCall: StateFlow<Call?> = _activeCall

    init {
        viewModelScope.launch {
            Log.d("streamVideo.user", "streamVideo.user: ${streamVideo.user}")
            launch { collectRingingCalls() }
            launch { collectActiveCalls() }
            launch { collectConnectionState() }
        }
    }
    private suspend fun collectRingingCalls() {
        streamVideo.state.ringingCall.collectLatest { call ->
            _incomingCall.value = call
        }
    }

    private suspend fun collectActiveCalls() {
        streamVideo.state.activeCall.collectLatest { call ->
            _activeCall.value = call
        }
    }
    private suspend fun collectConnectionState() {
        streamVideo.state.connection.collectLatest { connection ->
            Log.d("ConnectionState", "Connection state updated: $connection")
        }
    }

    fun joinCall()
    {
        viewModelScope.launch {
            val streamVideo = StreamVideo.instance()
            val ringingCall = streamVideo.state.ringingCall
            val call = ringingCall.value
            if (call != null) {
                call.accept()
            }
            val result = call?.join()
            result?.onSuccess {
                _videoMutableUiState.value = SDKResponseState.Success(call)
                Log.d("VideoCallViewModel", "joinCall: ${call.id.toString()}")
            }?.onError {
                _videoMutableUiState.value = SDKResponseState.Error
            }
        }
    }
}