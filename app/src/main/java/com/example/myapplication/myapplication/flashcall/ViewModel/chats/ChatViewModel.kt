package com.example.myapplication.myapplication.flashcall.ViewModel.chats

import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.Manifest
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.myapplication.myapplication.flashcall.Data.AudioPlayerState
import com.example.myapplication.myapplication.flashcall.Data.MessageType
import com.example.myapplication.myapplication.flashcall.Data.model.Resource
import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.MessageDataClass
//import com.example.myapplicationication.myapplicationcation.flashcall.Manifest
//import com.example.myapplication.myapplicationication.flashcall.Manifest
import com.example.myapplication.myapplication.flashcall.domain.GetMessageUseCase
import com.example.myapplication.myapplication.flashcall.domain.MarkMessageAsSeenUseCase
import com.example.myapplication.myapplication.flashcall.domain.SendMessageUseCase
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    getMessageUseCase: GetMessageUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val markMessageAsSeenUseCase: MarkMessageAsSeenUseCase,
    private val context: Application,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val chatId = savedStateHandle.get<String>("chatId") ?: ""

    val senderId = savedStateHandle.get<String>("userId") ?: ""

    private val _messages = MutableStateFlow<Resource<List<MessageDataClass>>>(Resource.Loading())
    val messages: StateFlow<Resource<List<MessageDataClass>>> = _messages.asStateFlow()

    private val _messageText = MutableStateFlow("")
    val messageText: StateFlow<String> = _messageText.asStateFlow()

    private val _selectedMediaUri = MutableStateFlow<Uri?>(null)
    val selectedMediaUri: StateFlow<Uri?> = _selectedMediaUri.asStateFlow()

    private val _audioPlayerStates = mutableStateMapOf<String?, AudioPlayerState?>()
    val audioPlayerStates: Map<String?, AudioPlayerState?> = _audioPlayerStates





    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    private val _recordedAudioUri = MutableStateFlow<Uri?>(null)
    val recordedAudioUri: StateFlow<Uri?> = _recordedAudioUri.asStateFlow()

    private var recorder: MediaRecorder? = null
    private var outputFile: String? = null
    private var currentRecordedFile: File? = null


    init {

        viewModelScope.launch {

            getMessageUseCase("6JfenuvcVQvfZkdUldUR").collectLatest { result ->
                _messages.value = result

                if(result is Resource.Success) {
                    result.data?.let{
                        markAllMessagesAsSeen(it)
                        _messages.value = Resource.Success(it)
                        Log.d("ChatViewModelMessageList", "messages: $it")
                    }
                }
            }
        }
    }

    fun onMessageTextChange(text: String) {
        _messageText.value = text
    }

    fun onSelectMedia(uri: Uri, messageType: MessageType) {
        _selectedMediaUri.value = uri
        sendMessage(uri.toString(), messageType)
    }

    fun sendMessage(messageContent: String, messageType: MessageType) {
        viewModelScope.launch {
//            try {
//                val content = when (messageType) {
//                    MessageType.TEXT -> messageText.value
//                    MessageType.IMAGE, MessageType.AUDIO -> selectedMediaUri.value?.toString() ?: ""
//                }
//                if (content.isNotBlank()) {
//                    sendMessageUseCase(chatId, content, messageType, userId)
//                    _messageText.value = ""
//                    _selectedMediaUri.value = null
//                }
//            } catch (e : Exception) {
//                e.printStackTrace()
//            }

            try{
                if(messageContent.isNotBlank() || messageType == MessageType.IMAGE){
                    sendMessageUseCase("6JfenuvcVQvfZkdUldUR", messageContent, messageType, "6687f55f290500fb85b7ace0")
                    _messageText.value = ""
                    _selectedMediaUri.value = null
                }
            } catch (e : Exception){
                e.printStackTrace()
            }
        }
    }

//    fun playAudio(message: MessageDataClass) {
//        // Check if the audio is already playing
//        val currentAudioState = audioPlayerStates[message.senderId]
//        if (currentAudioState?.isPlaying == true) {
//            // If already playing, pause it
//            currentAudioState.player.pause()
//            if (message.senderId != null){
//                _audioPlayerStates[message.senderId] = currentAudioState.copy(isPlaying = false)
//            }
//        } else {
//            // If not playing or paused, start playing
//            val player = ExoPlayer.Builder(getApplication(context)).build()
//            val mediaItem = MediaItem.fromUri(message.audio!!)
//            player.setMediaItem(mediaItem)
//            player.prepare()
//            player.play()
//
//            if (message.senderId != null){
//                _audioPlayerStates[message.senderId] = AudioPlayerState(player, true)
//            }
//        }
//    }
//
//    override fun onCleared() {
//        super.onCleared()
//        // Release all ExoPlayer instances when the ViewModel is cleared
//        audioPlayerStates.values.forEach { it.player.release() }
//    }

    private fun markAllMessagesAsSeen(messages: List<MessageDataClass>) {
        for (message in messages) {
            if (message.senderId != senderId && !message.seen!!) {
                markMessageAsSeen(message)
            }
        }
    }

    fun markMessageAsSeen(message: MessageDataClass) {
        viewModelScope.launch {
            markMessageAsSeenUseCase("6JfenuvcVQvfZkdUldUR", message)
        }
    }

    fun startRecording() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                recorder = MediaRecorder().apply {
                    setAudioSource(MediaRecorder.AudioSource.MIC)
                    setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                    val file = File.createTempFile("audio_record_", ".mp3", context.cacheDir)
                    currentRecordedFile = file
                    setOutputFile(file.absolutePath)
                    setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                    prepare()
                    start()
                    Log.e("ChatViewModelAudio", "Started recording:")
                }
                _isRecording.value = true
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error starting recording: ${e.message}")
            }
        }
    }


    fun stopRecording() {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED){
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    recorder?.stop()
                    recorder?.release()
                    recorder = null
                    currentRecordedFile?.let { file ->
                        val uri = Uri.fromFile(file)
                        _recordedAudioUri.value = uri
                        sendMessage(uri.toString(), MessageType.AUDIO) // Send audio message
                        currentRecordedFile = null // Reset the file reference
                    }
                } catch (e: Exception) {
                    Log.e("ChatViewModel", "Error stopping recording: ${e.message}")
                } finally {
                    _isRecording.value = false
                }
            }
        } else {
        }
    }


    fun onAudioRecorded(uri: Uri?) {
        if (uri != null) {
            onSelectMedia(uri, MessageType.AUDIO)
        }
    }

    fun cancelRecording() {
        viewModelScope.launch(Dispatchers.IO) {
            // ... (Cancel recording and cleanup resources) ...
            _isRecording.value = false
        }
    }

    fun onImageSelected(uri: Uri?) {
        viewModelScope.launch {
            uri?.let {
                onSelectMedia(it, MessageType.IMAGE)
            }
        }
    }

    fun playAudio(message: MessageDataClass, isPlaying: Boolean) {
        viewModelScope.launch {
            val audioPlayerState = _audioPlayerStates[message.senderId]
            if (audioPlayerState == null) {
                // If no player exists for this message, create a new one
                val player = ExoPlayer.Builder(getApplication(context)).build()
                val mediaItem = MediaItem.fromUri(message.audio!!)
                player.setMediaItem(mediaItem)
                player.prepare()
                _audioPlayerStates[message.senderId] = AudioPlayerState(player)
                if (isPlaying) {
                    player.play()
                }
            } else {
                // If a player exists, toggle play/pause
                if (isPlaying) {
                    audioPlayerState.player.play()
                } else {
                    audioPlayerState.player.pause()
                }
            }
            // Update the state in the map
            _audioPlayerStates[message.senderId] = audioPlayerState?.copy(isPlaying = isPlaying)
        }
    }

    fun updateAudioProgress(messageId: String, progress: Long) {
        _audioPlayerStates[messageId] = audioPlayerStates[messageId]?.copy(currentPosition = progress)
    }

    fun stopAudio(messageId: String? = null) {
        if (messageId != null) {
            _audioPlayerStates[messageId]?.player?.stop() // Stop only the specified player
        } else {
            _audioPlayerStates.values.forEach { it?.player?.stop() } // Stop all players
        }
    }

    // Update the audio player states
    private fun setAudioPlayingState(messageId: String, isPlaying: Boolean) {
        _audioPlayerStates[messageId] = _audioPlayerStates[messageId]?.copy(isPlaying = isPlaying)
    }

    override fun onCleared() {
        super.onCleared()
        // Release all ExoPlayer instances when the ViewModel is cleared
        _audioPlayerStates.values.forEach { it?.player?.release() }
        _audioPlayerStates.clear() // Optionally clear the map as well
    }
}