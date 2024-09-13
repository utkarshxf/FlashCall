package com.example.myapplication.myapplication.flashcall.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.myapplication.flashcall.Data.model.SDKResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.video.android.core.Call
import io.getstream.video.android.core.ConnectionState
import io.getstream.video.android.core.StreamVideo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoCallViewModel @Inject constructor() : ViewModel() {
    private val streamVideo = StreamVideo.instance()

    private val _videoMutableUiState = MutableStateFlow<SDKResponseState>(SDKResponseState.Loading)
    val videoMutableUiState: StateFlow<SDKResponseState> = _videoMutableUiState

    private val _incomingCall: MutableStateFlow<Call?> = MutableStateFlow(null)
    val incomingCall: StateFlow<Call?> = _incomingCall

    private val _activeCall: MutableStateFlow<Call?> = MutableStateFlow(null)
    val activeCall: StateFlow<Call?> = _activeCall

    private val _callerLeft: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val callerLeft: StateFlow<Boolean> = _callerLeft

    private var _leaveCall: MutableStateFlow<Call?> = MutableStateFlow(null)
    val leaveCall: StateFlow<Call?> = _leaveCall

    private val _callAccepted: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val callAccepted: StateFlow<Boolean> = _callAccepted


    init {
        viewModelScope.launch {
            Log.d("qwerty", "streamVideo.user: ${streamVideo.user}")
            launch { collectRingingCalls() }
            launch { collectActiveCalls() }
            launch { collectConnectionState() }
        }
    }


    private suspend fun collectRingingCalls() {
        streamVideo.state.ringingCall.collectLatest { call ->
            Log.d("VideoCall", "Ringing call detected: ${call?.id}")
            _incomingCall.value = call
        }
    }

    private suspend fun collectActiveCalls() {
        streamVideo.state.activeCall.collectLatest { call ->
            _activeCall.value = call
            if (call == null) {
                _callAccepted.value = false
                _incomingCall.value = null
            }
        }
    }

    private suspend fun collectConnectionState() {
        streamVideo.state.connection.collectLatest { connectionState ->
            when (connectionState) {
                ConnectionState.Loading -> {
                    Log.d("qwerty3", "Loading: {ConnectionState.Loading}")
                }

                ConnectionState.Connected -> {
                    Log.d("qwerty4", "Connected: {ConnectionState.Connected}")
                }

                ConnectionState.Disconnected -> {
                    Log.d("qwerty5", "Connected: {ConnectionState.Connected}")
                }

                is ConnectionState.Failed -> {
                    Log.d("qwerty6", "Failed: {ConnectionState.Failed}")
                }

                ConnectionState.PreConnect -> {
                    Log.d("qwerty7", "PreConnect: {ConnectionState.PreConnect}")
                }

                ConnectionState.Reconnecting -> {
                    Log.d("qwerty8", "Reconnecting: {ConnectionState.Reconnecting}")
                }
            }
        }
    }
    fun joinCall() {
        _callAccepted.value = true
        viewModelScope.launch {
            val streamVideo = StreamVideo.instance()
            val ringingCall = streamVideo.state.ringingCall
            val call = ringingCall.value
            if (call != null) {
                call.accept()
                _callAccepted.value = true
            }
            val result = call?.join()
            result?.onSuccess {
                _videoMutableUiState.value = SDKResponseState.Success(call)
                _callAccepted.value = true
            }?.onError {
                _videoMutableUiState.value = SDKResponseState.Error
                _callAccepted.value = false
            }
        }
    }

    fun leaveCall(call: Call, onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                call.leave()
                _leaveCall.value = call
                _callAccepted.value = false
                _incomingCall.value = null
                _activeCall.value = null

                Log.d("VideoCall", "Call left: ${call.id}")
                onComplete()
            } catch (e: Exception) {
                Log.e("VideoCall", "Error leaving call: ${e.message}")
            }
        }
    }
}