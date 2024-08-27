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
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    getMessageUseCase: GetMessageUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val chatRepository: ChatRepository,
    private val markMessageAsSeenUseCase: MarkMessageAsSeenUseCase,
    private val firestore : FirebaseFirestore,
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

//    private val _audioPlayerStates = mutableStateMapOf<String?, AudioPlayerState?>()
//    val audioPlayerStates: Map<String?, AudioPlayerState?> = _audioPlayerStates

    // Audio Playback States
    private val _audioPlayerStates = mutableStateMapOf<String?, AudioPlayerState?>()
    val audioPlayerStates: Map<String?, AudioPlayerState?> = _audioPlayerStates

    // helper function to format time
    private fun formatTime(timeInMillis: Long): String {
        val totalSeconds = timeInMillis / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    // Add new state for current playing audio message duration
    private val _currentPlayingAudioMessageDuration = MutableStateFlow<String?>(null)
    val currentPlayingAudioMessageDuration: StateFlow<String?> = _currentPlayingAudioMessageDuration.asStateFlow()






    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    private val _recordedAudioUri = MutableStateFlow<Uri?>(null)
    val recordedAudioUri: StateFlow<Uri?> = _recordedAudioUri.asStateFlow()

    private var recorder: MediaRecorder? = null
    private var outputFile: String? = null
    private var currentRecordedFile: File? = null
    private val _chatData = MutableStateFlow<ChatDataClass?>(null)
    val chatData: StateFlow<ChatDataClass?> = _chatData



    init {

        viewModelScope.launch {
            var chatId = getChatIdFromPreferences()
            while (chatId == null) {
                delay(1000)  // Small delay to wait for preferences to be populated
                chatId = getChatIdFromPreferences()
                Log.d("ChatViewModelMessageList", "chatId $chatId")

            }
            fetchChatData(chatId)

            Log.d("ChatViewModelMessageList", "chatId $chatId")

            getMessageUseCase(chatId).collectLatest { result ->
                _messages.value = result

                if(result is Resource.Success) {
                    result.data?.let{
                        markAllMessagesAsSeen(it)
                        _messages.value = Resource.Success(it)
                        Log.d("ChatViewModelMessageList1", "messages: $it")
                    }
                }
            }
        }
    }
    fun endChat(chatId: String) {
        viewModelScope.launch {
            try {
                firestore.collection("chats").document(chatId).update(
                    mapOf(
                        "status" to "ended",
                        "endedAt" to System.currentTimeMillis()
                    )
                ).addOnSuccessListener {
                    // Optionally handle success (e.g., notify user)
                }.addOnFailureListener { e ->
                    // Optionally handle failure (e.g., show error message)
                    Log.e("ChatViewModel", "Error updating chat status", e)
                }
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error updating chat status", e)
            }
        }
    }


    private suspend fun fetchChatData(chatId: String) {
        firestore.collection("chats").document(chatId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val data = document.data ?: return@addOnSuccessListener
                    val clientId = data["clientId"] as? String ?: return@addOnSuccessListener
                    val clientName = data["clientName"] as? String
                    val clientBalance = data["clientBalance"] as? Double
                    val creatorId = data["creatorId"] as? String
                    val endedAt = data["endedAt"] as? Long
                    val maxChatDuration = data["maxChatDuration"] as? Int
                    val startedAt = data["startedAt"] as? Long
                    val status = data["status"] as? String
                    val timeLeft = data["timeLeft"] as? Double
                    val timeUtilized = data["timeUtilized"] as? Double
                    val messagesData = data["messages"] as? List<Map<String, Any>> ?: emptyList()

                    val messages = messagesData.map { messageData ->
                        MessageDataClass(
                            text = messageData["text"] as? String,
                            audio = messageData["audio"] as? String,
                            img = messageData["img"] as? String,
                            createdAt = messageData["createdAt"] as? Long,
                            seen = messageData["seen"] as? Boolean ?: false,
                            senderId = messageData["senderId"] as? String, // Add clientName to each message if needed
                        )
                    }

                    val chat = ChatDataClass(
                        clientId = clientId,
                        clientName = clientName,
                        clientBalance = clientBalance,
                        creatorId = creatorId ?: "",
                        endedAt = endedAt,
                        maxChatDuration = maxChatDuration,
                        startedAt = startedAt,
                        status = status,
                        timeLeft = timeLeft,
                        timeUtilized = timeUtilized,
                        messages = messages
                    )

                    _chatData.value = chat
                }
            }
            .addOnFailureListener { exception ->
                // Handle failure
            }
    }

    fun onMessageTextChange(text: String) {
        _messageText.value = text
    }
    fun getChatIdFromPreferences(): String? {
        val sharedPreferences = context.getSharedPreferences("ChatPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("chatId", null)
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
                    var chatId = getChatIdFromPreferences()
                    while (chatId == null) {
                        delay(1000)  // Small delay to wait for preferences to be populated
                        chatId = getChatIdFromPreferences()
                    }
                    Log.d("Inside message created", "Got the error first")
                    sendMessageUseCase(chatId, messageContent, messageType, "6687f55f290500fb85b7ace0")
                    Log.d("Inside message created", "Got the error")
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
                }
                _isRecording.value = true  // Update the state to indicate recording has started
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error starting recording: ${e.message}")
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
                    uploadAudioToFirebase(file) // Upload audio after stopping recording
                    currentRecordedFile = null
                }
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error stopping recording: ${e.message}", e)
            } finally {
                _isRecording.value = false
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
                        // Handle the URL of the uploaded audio
                        Log.d("ChatViewModel", "Audio uploaded successfully: $uri")
                        saveAudioMetadata(uri.toString(), file.name)
                    }.addOnFailureListener {
                        Log.e("ChatViewModel", "Failed to get download URL", it)
                    }
                }.addOnFailureListener {
                    Log.e("ChatViewModel", "Failed to upload audio", it)
                }
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error uploading audio: ${e.message}", e)
            }
        }
    }

    private fun saveAudioMetadata(downloadUrl: String, fileName: String) {
        viewModelScope.launch {
            try {
                val firestore = FirebaseFirestore.getInstance()
                val audioMetadata = hashMapOf(
                    "url" to downloadUrl,
                    "fileName" to fileName,
                    "timestamp" to FieldValue.serverTimestamp() // Optionally add a timestamp
                )

                // Save metadata to Firestore
                firestore.collection("audio")
                    .add(audioMetadata)
                    .addOnSuccessListener {
                        Log.d("ChatViewModel", "Audio metadata saved successfully")
                    }
                    .addOnFailureListener { e ->
                        Log.e("ChatViewModel", "Failed to save audio metadata", e)
                    }
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error saving audio metadata: ${e.message}", e)
            }
        }
    }



    private fun saveAudioMetadata(uri: Uri) {
        viewModelScope.launch {
            try {
                val audioMetadata = mapOf(
                    "uri" to uri.toString(),
                    "timestamp" to System.currentTimeMillis()
                )
                FirebaseFirestore.getInstance().collection("audioMessages")
                    .add(audioMetadata)
                    .addOnSuccessListener {
                        Log.d("ChatViewModel", "Audio metadata saved successfully")
                    }.addOnFailureListener {
                        Log.e("ChatViewModel", "Failed to save audio metadata", it)
                    }
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error saving audio metadata: ${e.message}", e)
            }
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
            wakeLock = null // Nullify to avoid releasing it again
        }
    }
}
