package com.example.myapplication.myapplication.flashcall.ViewModel.chats

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.net.Uri
import android.os.PowerManager
import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.audio.AudioPlayerState
import com.example.myapplication.myapplication.flashcall.Data.MessageType
import com.example.myapplication.myapplication.flashcall.Data.model.Resource
import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.ChatDataClass
import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.MessageDataClass
//import com.example.myapplicationication.myapplicationcation.flashcall.Manifest
//import com.example.myapplication.myapplicationication.flashcall.Manifest
import com.example.myapplication.myapplication.flashcall.domain.GetMessageUseCase
import com.example.myapplication.myapplication.flashcall.domain.MarkMessageAsSeenUseCase
import com.example.myapplication.myapplication.flashcall.domain.SendMessageUseCase
import com.example.myapplication.myapplication.flashcall.repository.ChatRepository
import com.example.myapplication.myapplication.flashcall.repository.UserPreferencesRepository
import com.example.myapplication.myapplication.flashcall.utils.PreferencesKey
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getMessageUseCase: GetMessageUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val chatRepository: ChatRepository,
    private val markMessageAsSeenUseCase: MarkMessageAsSeenUseCase,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val firestore: FirebaseFirestore,
    @ApplicationContext private val context: Context,
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

    private val _currentPlayingAudioMessageDuration = MutableStateFlow<String?>(null)
    val currentPlayingAudioMessageDuration: StateFlow<String?> = _currentPlayingAudioMessageDuration.asStateFlow()

    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    private val _recordedAudioUri = MutableStateFlow<Uri?>(null)
    val recordedAudioUri: StateFlow<Uri?> = _recordedAudioUri.asStateFlow()

    private var recorder: MediaRecorder? = null
    private var currentRecordedFile: File? = null

    private val _chatData = MutableStateFlow<ChatDataClass?>(null)
    val chatData: StateFlow<ChatDataClass?> = _chatData

    init {
        viewModelScope.launch {
            if (chatId.isNotBlank()) {
                fetchChatData(chatId)
                observeMessages(chatId)
            } else {
                Log.e("ChatViewModel", "Invalid chatId")
            }
        }
    }

    private suspend fun observeMessages(chatId: String) {
        getMessageUseCase(chatId).collectLatest { result ->
            _messages.value = result
            if (result is Resource.Success) {
                result.data?.let { messages ->
                    markAllMessagesAsSeen(messages)
                    _messages.value = Resource.Success(messages)
                }
            }
        }
    }

    fun endChat(chatId: String) {
        viewModelScope.launch {
            try {
                val updateData = mapOf(
                    "status" to "ended",
                    "endedAt" to System.currentTimeMillis()
                )
                firestore.collection("chats").document(chatId).update(updateData)
                    .addOnFailureListener { e ->
                        Log.e("ChatViewModel", "Error updating chat status", e)
                    }
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error updating chat status", e)
            }
        }
    }

    private suspend fun fetchChatData(chatId: String) {
        try {
            val document = firestore.collection("chats").document(chatId).get().await()
            if (document.exists()) {
                val chat = document.toObject(ChatDataClass::class.java)
                _chatData.value = chat
            }
        } catch (e: Exception) {
            Log.e("ChatViewModel", "Error fetching chat data", e)
        }
    }

    fun onMessageTextChange(text: String) {
        _messageText.value = text
    }

    private fun getChatIdFromPreferences(): String? {
        val sharedPreferences = context.getSharedPreferences("ChatPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("chatId", null)
    }

    fun onSelectMedia(uri: Uri, messageType: MessageType) {
        _selectedMediaUri.value = uri
        sendMessage(uri.toString(), messageType)
    }

    fun sendMessage(messageContent: String, messageType: MessageType) {
        viewModelScope.launch {
            try {
                if (messageContent.isNotBlank() || messageType == MessageType.IMAGE) {
                    val chatId = getChatIdFromPreferences() ?: return@launch
                    val userId = userPreferencesRepository.getStoredUserData(PreferencesKey.UserId.key) ?: return@launch
                    sendMessageUseCase(chatId, messageContent, messageType, userId)
                    _messageText.value = ""
                    _selectedMediaUri.value = null
                }
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error sending message", e)
            }
        }
    }

    private fun markAllMessagesAsSeen(messages: List<MessageDataClass>) {
        viewModelScope.launch {
            for (message in messages) {
                if (message.senderId != senderId && message.seen == false) {
                    markMessageAsSeen(message)
                }
            }
        }
    }

    fun markMessageAsSeen(message: MessageDataClass) {
        viewModelScope.launch {
            markMessageAsSeenUseCase(chatId, message)
        }
    }

    fun startRecording() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val file = File.createTempFile("audio_record_", ".mp3", context.cacheDir)
                recorder = MediaRecorder().apply {
                    setAudioSource(MediaRecorder.AudioSource.MIC)
                    setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                    setOutputFile(file.absolutePath)
                    setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                    prepare()
                    start()
                }
                currentRecordedFile = file
                _isRecording.value = true
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error starting recording", e)
            }
        }
    }

    fun stopRecording() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                recorder?.apply {
                    stop()
                    release()
                }
                recorder = null

                currentRecordedFile?.let { file ->
                    val uri = Uri.fromFile(file)
                    _recordedAudioUri.value = uri
                    uploadAudioToFirebase(file)
                    currentRecordedFile = null
                }
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error stopping recording", e)
            } finally {
                _isRecording.value = false
            }
        }
    }

    private fun uploadAudioToFirebase(file: File) {
        viewModelScope.launch {
            try {
                val audioRef = FirebaseStorage.getInstance().reference.child("audio/${file.name}")
                val uploadTask = audioRef.putFile(Uri.fromFile(file))

                uploadTask.addOnSuccessListener {
                    audioRef.downloadUrl.addOnSuccessListener { uri ->
                        saveAudioMetadata(uri.toString(), file.name)
                    }
                }.addOnFailureListener {
                    Log.e("ChatViewModel", "Failed to upload audio", it)
                }
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error uploading audio", e)
            }
        }
    }

    private fun saveAudioMetadata(downloadUrl: String, fileName: String) {
        viewModelScope.launch {
            try {
                val audioMetadata = hashMapOf(
                    "url" to downloadUrl,
                    "fileName" to fileName,
                    "timestamp" to FieldValue.serverTimestamp()
                )
                firestore.collection("audio")
                    .add(audioMetadata)
                    .addOnFailureListener { e ->
                        Log.e("ChatViewModel", "Failed to save audio metadata", e)
                    }
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error saving audio metadata", e)
            }
        }
    }

    fun onAudioRecorded(uri: Uri?) {
        uri?.let { onSelectMedia(it, MessageType.AUDIO) }
    }

    fun cancelRecording() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                recorder?.apply {
                    stop()
                    release()
                }
                recorder = null
                currentRecordedFile?.delete()
                currentRecordedFile = null
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error cancelling recording", e)
            } finally {
                _isRecording.value = false
            }
        }
    }

    fun onImageSelected(uri: Uri?) {
        uri?.let { onSelectMedia(it, MessageType.IMAGE) }
    }

    fun playAudio(message: MessageDataClass, isPlaying: Boolean) {
        viewModelScope.launch {
            val audioPlayerState = _audioPlayerStates[message.senderId]
            if (audioPlayerState == null) {
                val player = ExoPlayer.Builder(context).build()
                message.audio?.let { audioUrl ->
                    val mediaItem = MediaItem.fromUri(audioUrl)
                    player.setMediaItem(mediaItem)
                    player.prepare()
                    _audioPlayerStates[message.senderId] = AudioPlayerState(player, isPlaying)
                    if (isPlaying) player.play()
                }
            } else {
                if (isPlaying) audioPlayerState.player.play() else audioPlayerState.player.pause()
                _audioPlayerStates[message.senderId] = audioPlayerState.copy(isPlaying = isPlaying)
            }
        }
    }

    fun updateAudioProgress(messageId: String, progress: Long) {
        _audioPlayerStates[messageId] = _audioPlayerStates[messageId]?.copy(currentPosition = progress)
    }

    fun stopAudio(messageId: String? = null) {
        if (messageId != null) {
            _audioPlayerStates[messageId]?.player?.stop()
        } else {
            _audioPlayerStates.values.forEach { it?.player?.stop() }
        }
    }

    override fun onCleared() {
        super.onCleared()
        _audioPlayerStates.values.forEach { it?.player?.release() }
        _audioPlayerStates.clear()
        WakeLockManager.releaseWakeLock()
    }
}

data class AudioPlayerState(
    val player: ExoPlayer,
    val isPlaying: Boolean = false,
    val currentPosition: Long = 0
)

object WakeLockManager {
    private var wakeLock: PowerManager.WakeLock? = null

    fun acquireWakeLock(context: Context) {
        if (wakeLock == null) {
            val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::WakeLockTag")
            wakeLock?.acquire()
        }
    }

    fun releaseWakeLock() {
        wakeLock?.let {
            if (it.isHeld) {
                it.release()
            }
            wakeLock = null
        }
    }
}