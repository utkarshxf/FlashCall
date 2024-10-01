package com.example.myapplication.myapplication.flashcall.ViewModel.chats

//import com.example.myapplicationication.myapplicationcation.flashcall.Manifest
//import com.example.myapplication.myapplicationication.flashcall.Manifest
import android.app.Application
import android.content.Context
import android.media.MediaRecorder
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.myapplication.myapplication.flashcall.Data.MessageType
import com.example.myapplication.myapplication.flashcall.Data.model.Resource
import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.ChatDataClass
import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.MessageDataClass
import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.audio.AudioPlayerState
import com.example.myapplication.myapplication.flashcall.domain.GetMessageUseCase
import com.example.myapplication.myapplication.flashcall.domain.MarkMessageAsSeenUseCase
import com.example.myapplication.myapplication.flashcall.domain.SendMessageUseCase
import com.example.myapplication.myapplication.flashcall.repository.ChatRepository
import com.example.myapplication.myapplication.flashcall.repository.UserPreferencesRepository
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getMessageUseCase: GetMessageUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val chatRepository: ChatRepository,
    private val markMessageAsSeenUseCase: MarkMessageAsSeenUseCase,
    private val firestore: FirebaseFirestore,
    private val context: Application,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val chatId = getChatIdFromPreferences()
    var userId = userPreferencesRepository.getUser()?._id

    private val _messages = MutableStateFlow<Resource<List<MessageDataClass>>>(Resource.Loading())
    val messages: StateFlow<Resource<List<MessageDataClass>>> = _messages.asStateFlow()

    private val _messageText = MutableStateFlow("")
    val messageText: StateFlow<String> = _messageText.asStateFlow()

    private val _selectedMediaUri = MutableStateFlow<Uri?>(null)
    val selectedMediaUri: StateFlow<Uri?> = _selectedMediaUri.asStateFlow()

    private val _audioPlayerStates = mutableStateMapOf<String?, AudioPlayerState?>()
    val audioPlayerStates: Map<String?, AudioPlayerState?> = _audioPlayerStates

    private val _currentPlayingAudioMessageDuration = MutableStateFlow<String?>(null)
    val currentPlayingAudioMessageDuration: StateFlow<String?> =
        _currentPlayingAudioMessageDuration.asStateFlow()
    var isRecording = mutableStateOf(false)
    private val _recordedAudioUri = MutableStateFlow<Uri?>(null)
    val recordedAudioUri: StateFlow<Uri?> = _recordedAudioUri.asStateFlow()

    private var recorder: MediaRecorder? = null
    private var currentRecordedFile: File? = null

    private val _chatData = MutableStateFlow<ChatDataClass?>(null)
    val chatData: StateFlow<ChatDataClass?> = _chatData.asStateFlow()

    private val _endedCall = MutableStateFlow(false)
    val endedCall: StateFlow<Boolean> = _endedCall.asStateFlow()

    init {
        fetchChatData()
        getMessages()
    }

    private fun getMessages() {
        viewModelScope.launch {
            var chatId = getChatIdFromPreferences()
            while (chatId == null) {
                delay(1000)
                chatId = getChatIdFromPreferences()
            }
            getMessageUseCase(chatId).collectLatest { result ->
                _messages.value = result
            }
        }
    }

    private fun fetchChatData() {
        viewModelScope.launch {
            var chatId = getChatIdFromPreferences()
            while (chatId == null) {
                delay(1000)
                chatId = getChatIdFromPreferences()
            }
            chatRepository.fetchChatData(chatId).collectLatest {
                _chatData.value = it
                _endedCall.value = it?.status == "ended"
            }
        }
    }

    fun endChat(onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                if (chatId != null) {
                    firestore.collection("chats").document(chatId).update(
                        mapOf(
                            "status" to "ended", "endedAt" to System.currentTimeMillis()
                        )
                    ).addOnSuccessListener {
                        onComplete()
                    }.addOnFailureListener { e ->
                        Log.e("ChatViewModel", "Error updating chat status", e)
                    }
                }
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error updating chat status", e)
            }
        }
    }


    fun getChatIdFromPreferences(): String? {
        val sharedPreferences = context.getSharedPreferences("ChatPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("chatId", null)
    }

    fun onSelectMedia(uri: Uri, text:String, messageType: MessageType) {
        _selectedMediaUri.value = uri
        sendMessage(uri.toString(),text, messageType)
    }
    fun getImageUri(context: Context): Uri {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageName = "JPEG_${timeStamp}_"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File.createTempFile(imageName, ".jpg", storageDir)
        return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", imageFile)
    }

    fun sendMessage(messageContent: String, text: String?, messageType: MessageType) {
        viewModelScope.launch {
            try {
                if (messageContent.isNotBlank() || messageType == MessageType.IMAGE || messageType == MessageType.AUDIO) {
                    val chatId = getChatIdFromPreferences() ?: return@launch
                    userId?.let {
                        sendMessageUseCase(
                            chatId = chatId,
                            messageContent = messageContent,
                            messageType = messageType,
                            senderId = it,
                            text = text
                        )
                    }
                    _messageText.value = ""
                    _selectedMediaUri.value = null
                    _recordedAudioUri.value = null
                }
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error sending message", e)
            }
        }
    }

    private fun markAllMessagesAsSeen(messages: List<MessageDataClass>) {
        for (message in messages) {
            if (message.senderId != userId && message.seen != true) {
                markMessageAsSeen(message)
            }
        }
    }

    fun markMessageAsSeen(message: MessageDataClass) {
        viewModelScope.launch {
            try {
                val chatId = getChatIdFromPreferences() ?: return@launch
                markMessageAsSeenUseCase(chatId, message)
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error marking message as seen", e)
            }
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
                }
                isRecording.value = true
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
                    Log.d("ChatViewModel", "Recorded audio URI: $uri")
                    uploadAudioToFirebase(file)
                    currentRecordedFile = null
                }
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error stopping recording", e)
            } finally {
                isRecording.value = false
            }
        }
    }

    private fun uploadAudioToFirebase(file: File) {
        viewModelScope.launch {
            try {
                val storageRef = FirebaseStorage.getInstance().reference
                val audioRef = storageRef.child("audio/${file.name}")
                val uploadTask = audioRef.putFile(Uri.fromFile(file))

                uploadTask.addOnSuccessListener {
                    audioRef.downloadUrl.addOnSuccessListener { uri ->
                        Log.d("ChatViewModel", "Audio uploaded successfully: $uri")
                        saveAudioMetadata(uri, file.name)

                    }.addOnFailureListener { e ->
                        Log.e("ChatViewModel", "Failed to get download URL", e)
                    }
                }.addOnFailureListener { e ->
                    Log.e("ChatViewModel", "Failed to upload audio", e)
                }
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error uploading audio", e)
            }
        }
    }

    private fun saveAudioMetadata(downloadUrl: Uri, fileName: String) {
        viewModelScope.launch {
            try {
                val audioMetadata = hashMapOf(
                    "url" to downloadUrl.toString(),
                    "fileName" to fileName,
                    "timestamp" to FieldValue.serverTimestamp()
                )

                firestore.collection("audio").add(audioMetadata).addOnSuccessListener {
                    Log.v("audioFlowFirebase" , downloadUrl.toString())
                    onAudioRecorded(downloadUrl ,"")
                    }.addOnFailureListener { e ->
                        Log.e("ChatViewModel", "Failed to save audio metadata", e)
                    }
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error saving audio metadata", e)
            }
        }
    }

    fun onAudioRecorded(uri: Uri? , text: String) {
        _selectedMediaUri.value = uri
        uri?.let { onSelectMedia(it, text, MessageType.AUDIO) }
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
                isRecording.value = false
            }
        }
    }

    fun onImageSelected(uri: Uri? , text: String) {
        viewModelScope.launch {
            uri?.let { onSelectMedia(it,text, MessageType.IMAGE) }
        }
    }

    fun playAudio(message: MessageDataClass, isPlaying: Boolean) {
        viewModelScope.launch {
            val audioPlayerState = _audioPlayerStates[message.senderId]
            if (audioPlayerState == null) {
                val player = ExoPlayer.Builder(getApplication(context)).build()
                val mediaItem = MediaItem.fromUri(message.audio ?: return@launch)
                player.setMediaItem(mediaItem)
                player.prepare()
                _audioPlayerStates[message.senderId] = AudioPlayerState(player)
                if (isPlaying) {
                    player.play()
                }
            } else {
                if (isPlaying) {
                    audioPlayerState.player.play()
                } else {
                    audioPlayerState.player.pause()
                }
            }
            _audioPlayerStates[message.senderId] = audioPlayerState?.copy(isPlaying = isPlaying)
        }
    }

    fun updateAudioProgress(messageId: String, progress: Long) {
        _audioPlayerStates[messageId] =
            audioPlayerStates[messageId]?.copy(currentPosition = progress)
    }

    fun stopAudio(messageId: String? = null) {
        if (messageId != null) {
            _audioPlayerStates[messageId]?.player?.stop()
        } else {
            _audioPlayerStates.values.forEach { it?.player?.stop() }
        }
    }

    private fun setAudioPlayingState(messageId: String, isPlaying: Boolean) {
        _audioPlayerStates[messageId] = _audioPlayerStates[messageId]?.copy(isPlaying = isPlaying)
    }

    override fun onCleared() {
        super.onCleared()
        _audioPlayerStates.values.forEach { it?.player?.release() }
        _audioPlayerStates.clear()
    }
}