package com.example.myapplication.myapplication.flashcall.ViewModel

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.myapplication.flashcall.Data.model.SDKResponseState
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.result.flatMap
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
class VideoCallViewModel @Inject constructor(private val firestore: FirebaseFirestore) : ViewModel() {
    private val streamVideo = StreamVideo.instance()
    var timeLeft by mutableStateOf(0.0)
        private set
    var state by mutableStateOf(VideoCallScreenState())
        private set

    init {
        viewModelScope.launch {
            launch { collectRingingCalls() }
            launch { collectActiveCalls() }
            launch { collectConnectionState() }
        }
    }

    private suspend fun collectRingingCalls() {
        streamVideo.state.ringingCall.collectLatest { call ->
            state = state.copy( incomingCall = call,
//                callType = if (streamVideo == true) "Video" else "Audio"
            )
        }
    }


    private suspend fun collectActiveCalls() {
        streamVideo.state.activeCall.collectLatest { call ->
            state = state.copy(
                activeCall = call,
                callAccepted = call != null
            )
            if (call == null) {
                state = state.copy(incomingCall = null)
            }
        }
    }

    private suspend fun collectConnectionState() {
        streamVideo.state.connection.collectLatest { connectionState ->
            state = state.copy(connectionState = connectionState)
            when (connectionState) {
                ConnectionState.Loading -> Log.d("VideoCall", "Loading: {ConnectionState.Loading}")
                ConnectionState.Connected -> Log.d("VideoCall", "Connected: {ConnectionState.Connected}")
                ConnectionState.Disconnected -> Log.d("VideoCall", "Disconnected: {ConnectionState.Disconnected}")
                is ConnectionState.Failed -> Log.d("VideoCall", "Failed: {ConnectionState.Failed}")
                ConnectionState.PreConnect -> Log.d("VideoCall", "PreConnect: {ConnectionState.PreConnect}")
                ConnectionState.Reconnecting -> Log.d("VideoCall", "Reconnecting: {ConnectionState.Reconnecting}")
            }
        }
    }

    fun joinCall() {
        viewModelScope.launch {
            val call = streamVideo.state.ringingCall.value
            if (call != null) {
                state = state.copy(isLoading = true)
                val result = call.accept().flatMap { call.join() }
                result.onSuccess {
                    state = state.copy(
                        sdkResponseState = SDKResponseState.Success(call),
                        callAccepted = true,
                        isLoading = false
                    )
                }.onError {
                    state = state.copy(
                        sdkResponseState = SDKResponseState.Error,
                        callAccepted = false,
                        isLoading = false
                    )
                }
            }
        }
    }
    fun observeTimeLeft(callId: String) {
        viewModelScope.launch {
            val documentRef = FirebaseFirestore.getInstance()
                .collection("calls")
                .document(callId)

            documentRef.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("Firestore", "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val newTimeLeft = snapshot.getDouble("timeLeft")
                    newTimeLeft?.let {
                        timeLeft = it
                    }
                }
            }
        }
    }

    fun leaveCall(onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                state.activeCall?.let { call ->
                    call.leave()
                    state = state.copy(
                        leaveCall = call,
                        callAccepted = false,
                        incomingCall = null,
                        activeCall = null
                    )
                    Log.d("VideoCall", "Call left: ${call.id}")
                    onComplete()
                }
            } catch (e: Exception) {
                Log.e("VideoCall", "Error leaving call: ${e.message}")
            }
        }
    }
    fun endCall(onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                state.activeCall?.let { call ->
                    call.end()  // Ends the call
                    state = state.copy(
                        activeCall = null,
                        incomingCall = null,
                        callAccepted = false
                    )
                    Log.d("VideoCall", "Call ended: ${call.id}")
                    onComplete()
                }
            } catch (e: Exception) {
                Log.e("VideoCall", "Error ending call: ${e.message}")
            }
        }
    }
    fun rejectCall(onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                state.incomingCall?.let { call ->
                    call.reject()  // Reject the incoming call
                    state = state.copy(
                        incomingCall = null,  // Clear the incoming call since it was rejected
                        callAccepted = false,
                        activeCall = null
                    )
                    Log.d("VideoCall", "Call rejected: ${call.id}")
                    onComplete()
                }
            } catch (e: Exception) {
                Log.e("VideoCall", "Error rejecting call: ${e.message}")
            }
        }
    }
    fun toggleCamera(enable: Boolean) {
        viewModelScope.launch {
            try {
                state.activeCall?.let { call ->
                    call.camera.setEnabled(enable)  // Enable or disable the camera
                    Log.d("VideoCall", "Camera toggled: ${call.id}, enabled: $enable")
                }
            } catch (e: Exception) {
                Log.e("VideoCall", "Error toggling camera: ${e.message}")
            }
        }
    }
    fun toggleMicrophone(enable: Boolean) {
        viewModelScope.launch {
            try {
                state.activeCall?.let { call ->
                    call.microphone.setEnabled(enable)  // Enable or disable the microphone
                    Log.d("VideoCall", "Microphone toggled: ${call.id}, enabled: $enable")
                }
            } catch (e: Exception) {
                Log.e("VideoCall", "Error toggling microphone: ${e.message}")
            }
        }
    }
    fun flipCamera() {
        viewModelScope.launch {
            try {
                state.activeCall?.let { call ->
                    call.camera.flip()  // Flip the camera
                    Log.d("VideoCall", "Camera flipped: ${call.id}")
                }
            } catch (e: Exception) {
                Log.e("VideoCall", "Error flipping camera: ${e.message}")
            }
        }
    }
    fun resetCallState() {
        state = state.copy(
            callAccepted = false,
            activeCall = null,
            incomingCall = null
        )
    }

}


data class VideoCallScreenState(
    val isLoading: Boolean = false,
    val sdkResponseState: SDKResponseState = SDKResponseState.Loading,
    val incomingCall: Call? = null,
    val activeCall: Call? = null,
    val callerLeft: Boolean = false,
    val leaveCall: Call? = null,
    val callAccepted: Boolean = false,
    val connectionState: ConnectionState = ConnectionState.Loading,
    val callType: String = "Audio"
)
