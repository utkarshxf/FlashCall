package com.example.myapplication.myapplication.flashcall.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.myapplication.flashcall.Data.model.SDKResponseState
import com.example.myapplication.myapplication.flashcall.domain.GetPendingChatRequestUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.video.android.core.Call
import io.getstream.video.android.core.StreamVideo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoCallViewModel @Inject constructor(): ViewModel() {

    init {
        viewModelScope.launch {
            val streamVideo = StreamVideo.instance()
            streamVideo.state.ringingCall
                .collectLatest { call ->
                    if (call != null) {
                        _videoCall.value = call
                    }
                    Log.d("RingingCallState", "collectLatest: $call")
                }
            streamVideo.state.activeCall
                .collectLatest { call ->
                    Log.d("ActiveCallState", "collectLatest: $call")
                }
            streamVideo.state.connection
                .collectLatest { connection ->
                    Log.d("ConnectionState", "collectLatest: $connection")
                }
        }
    }

    private val _videoMutableUiState = MutableStateFlow<SDKResponseState>(SDKResponseState.Loading)
    val videoMutableUiState : StateFlow<SDKResponseState> = _videoMutableUiState

    private val _videoCall: MutableStateFlow<Call?> = MutableStateFlow(null)
    var videoCall : StateFlow<Call?> = _videoCall

    fun joinCall()
    {
        viewModelScope.launch {

            Log.d("StreamVideo", "joinCall: Start")

            val streamVideo = StreamVideo.instance()
            val ringingCall = streamVideo.state.ringingCall
            val call = ringingCall.value



            if (call != null) {
                call.accept()
            }

            val result = call?.join()

            result?.onSuccess {
                _videoMutableUiState.value = SDKResponseState.Success(call)
                Log.d("VideoCallViewModel", "joinCall: Success")
            }?.onError {
                _videoMutableUiState.value = SDKResponseState.Error
            }
        }
    }
}