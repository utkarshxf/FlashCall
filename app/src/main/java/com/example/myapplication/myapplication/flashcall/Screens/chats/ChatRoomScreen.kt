package com.example.myapplication.myapplication.flashcall.Screens.chats

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import com.example.myapplication.myapplication.flashcall.R
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
//import androidx.compose.ui.R
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import coil.compose.AsyncImage
import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.audio.AudioRecorderState
import com.example.myapplication.myapplication.flashcall.Data.MessageType
import com.example.myapplication.myapplication.flashcall.Data.model.Resource
import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.MessageDataClass
import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.audio.AudioPlayerState
import com.example.myapplication.myapplication.flashcall.ViewModel.AuthenticationViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.chats.ChatRequestViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.chats.ChatViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.chats.WakeLockManager
import com.example.myapplication.myapplication.flashcall.ui.theme.ChatBackground
import com.example.myapplication.myapplication.flashcall.ui.theme.MainColor
import com.example.myapplication.myapplication.flashcall.ui.theme.SecondaryText
import com.example.myapplication.myapplication.flashcall.ui.theme.TimerBackground
import com.example.myapplication.myapplication.flashcall.ui.theme.arimoFontFamily
import kotlinx.coroutines.delay
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


private const val REQUEST_RECORD_AUDIO_PERMISSION = 200
@Composable
fun ChatRoomScreen(
    chatViewModel: ChatViewModel = hiltViewModel(),
    chatRequestViewModel: ChatRequestViewModel = hiltViewModel(),
    authenticationViewModel: AuthenticationViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val userData = authenticationViewModel.getUserFromPreferences(context)
    val uid = userData?._id
    val name = userData?.fullName
    val messages = chatViewModel.messages.collectAsState().value

    var messageText by remember { mutableStateOf("") }
    var imagePicker by remember { mutableStateOf(false) }
    var chatImageUri by rememberSaveable { mutableStateOf("") }
    val isRecording by chatViewModel.isRecording.collectAsState()
    val recordedAudioUri by chatViewModel.recordedAudioUri.collectAsState()
    var recorder: MediaRecorder? = null
    var currentRecordedFile: File? = null
    val chatData by chatViewModel.chatData.collectAsState()

    if (isRecording) {
        Text(text = "Recording is active...")
    } else {
        Text(text = "No recording in progress.")
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            chatImageUri = it.toString()
        }
        chatViewModel.onImageSelected(uri)
    }


    Surface(
        color = ChatBackground
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Image(
                        modifier = Modifier.size(50.dp),
                        painter = painterResource(id = com.example.myapplication.myapplication.flashcall.R.drawable.profile_picture_holder),
                        contentDescription = "Chat Client Image"
                    )
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp)
                    ) {
                        Text(
                            text = "Ongoing Chat with",
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 14.sp,
                                fontFamily = arimoFontFamily,
                                fontWeight = FontWeight.Light
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = chatData?.clientName.toString(),
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 22.sp,
                                fontFamily = arimoFontFamily,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    Button(
                        onClick = { chatViewModel.endChat(chatId = chatData?.clientId.toString()) },
                        modifier = Modifier
                            .width(120.dp)
                            .height(50.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "End Chat",
                            style = TextStyle(
                                color = Color.White,
                                fontFamily = arimoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        )
                    }
                }
            }

            // Timer Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(TimerBackground.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(TimerBackground.copy(alpha = 0.7f))
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Time Left: ${chatData?.timeLeft.toString()} ",
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = Color.Red,
                            fontFamily = arimoFontFamily,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            // Messages List
            Column(
                modifier = Modifier.weight(1f)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    reverseLayout = false // Ensures latest messages are at the bottom
                ) {
                    when (messages) {
                        is Resource.Success<*> -> {
                            val messageList = messages.data as? List<MessageDataClass> ?: emptyList()
                            val sortedMessages = messageList.sortedBy { it.createdAt ?: 0 } // Sort messages by timestamp
                            items(sortedMessages) { message ->
                                MessageItem(
                                    message = message,
                                    creatorId = uid.toString(),
                                    onMessageSeen = chatViewModel::markMessageAsSeen,
                                    viewModel = chatViewModel
                                )
                            }
                        }
                        is Resource.Error -> {
                            // Handle error
                        }
                        is Resource.Loading -> {
                            // Handle loading state
                        }
                    }
                }
            }

            // Message Input and Actions
            var progress by remember { mutableStateOf(false) }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .background(Color.White)
                    .clip(RoundedCornerShape(24.dp)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    value = messageText,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            if (messageText.isNotEmpty()) {
                                chatViewModel.sendMessage(messageText, MessageType.TEXT)
                                messageText = ""
                            }
                        }
                    ),
                    onValueChange = {
                        messageText = it
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.3f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.3f),
                        focusedTextColor = SecondaryText,
                        unfocusedTextColor = SecondaryText,
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent
                    ),
                    placeholder = {
                        Text(
                            text = if (progress) "Recording..." else "Message",
                            style = TextStyle(
                                color = SecondaryText,
                                fontSize = 16.sp,
                                fontFamily = arimoFontFamily,
                                fontWeight = FontWeight.Black
                            )
                        )
                    },
                    shape = RoundedCornerShape(32.dp),
                    trailingIcon = {
                        Row(
                            modifier = Modifier.width(70.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable { launcher.launch("image/*") },
                                painter = painterResource(id = R.drawable.attach_file_chat),
                                contentDescription = "Attach File"
                            )
                            Icon(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable { launcher.launch("camera/*") },
                                painter = painterResource(id = R.drawable.camera_icon_chat),
                                contentDescription = "Camera"
                            )
                        }
                    }
                )

                AudioRecorderButton(chatViewModel = chatViewModel)
                if (isRecording) {
                    Text(text = "Recording...")
                }

                recordedAudioUri?.let {
                    // Play or send audio button
                    Button(onClick = {
                        chatViewModel.onAudioRecorded(it)
                    }) {
                        Text("Send Audio")
                    }
                }
            }
        }
    }
}

@Composable
fun AudioRecorderButton(
    chatViewModel: ChatViewModel
) {
    val isRecording by chatViewModel.isRecording.collectAsState()
    val context = LocalContext.current
    val activity = LocalContext.current as? Activity

    IconButton(
        onClick = {
            if (isRecording) {
                chatViewModel.stopRecording()
                WakeLockManager.releaseWakeLock()
            } else {
                // Check for permission to record audio
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                        activity!!,
                        arrayOf(Manifest.permission.RECORD_AUDIO),
                        REQUEST_RECORD_AUDIO_PERMISSION
                    )
                } else {
                    // Start recording if permission is granted
                    WakeLockManager.acquireWakeLock(context)
                    chatViewModel.startRecording()
                }
            }
        }
    ) {
        Icon(
            imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.Mic,
            contentDescription = if (isRecording) "Stop Recording" else "Start Recording",
            tint = if (isRecording) Color.Red else Color.Black
        )
    }
}

@Composable
fun MessageItem(
    message: MessageDataClass,
    creatorId: String,
    onMessageSeen: (MessageDataClass) -> Unit,
    viewModel: ChatViewModel
) {
    val isOwnMessage = message.senderId == creatorId
    val formattedTime = message.createdAt?.let {
        SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(it))
    } ?: ""

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (!isOwnMessage) Arrangement.End else Arrangement.Start
    ) {
        if (message.text != null) {
            // Handle text message
            Card(
                modifier = Modifier
                    .padding(10.dp)
                    .background(Color.White)
                    .clip(RoundedCornerShape(if (isOwnMessage) 16.dp else 0.dp)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = message.text,
                        style = TextStyle(
                            color = if (isOwnMessage) Color.Black else Color.White,
                            fontSize = 16.sp,
                            fontFamily = arimoFontFamily
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = formattedTime,
                        style = TextStyle(
                            color = Color.Gray,
                            fontSize = 12.sp,
                            fontFamily = arimoFontFamily
                        )
                    )
                }
            }
        } else if (message.audio != null) {
            // Handle audio message
            Card(
                modifier = Modifier
                    .padding(10.dp)
                    .background(Color.White)
                    .clip(RoundedCornerShape(if (isOwnMessage) 16.dp else 0.dp)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    AudioPlayer(
                        audioUrl = message.audio
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = formattedTime,
                        style = TextStyle(
                            color = Color.Gray,
                            fontSize = 12.sp,
                            fontFamily = arimoFontFamily
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun AudioPlayer(audioUrl: String) {
    // Placeholder for audio player UI
    // You might use a button to start playing the audio, or integrate a more complex audio player component.
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clickable {
                // Handle audio play logic here
            }
    ) {
        Icon(
            imageVector = Icons.Filled.PlayArrow,
            contentDescription = "Play Audio",
            tint = Color.Black
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Audio Message",
            style = TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
                fontFamily = arimoFontFamily
            )
        )
    }
}

//@Composable
//fun AudioRecorderButton(
//    modifier: Modifier = Modifier,
//    onRecordingStarted: () -> Unit,
//    onRecordingCanceled: () -> Unit,
//    onSendClick: () -> Unit,
//    onStopRecording: (Uri?) -> Unit,
//    isRecording: Boolean,
//    viewModel: ChatViewModel
//) {
//    var offsetX by remember { mutableStateOf(0f) }
//
//    // Transition state for animation
//    val transition = updateTransition(targetState = isRecording, label = "audioRecorderTransition")
//
//    // Button background color and size transitions based on recording state
//    val buttonBackgroundColor by transition.animateColor(label = "backgroundColor") { state ->
//        if (state) Color.Red else Color.LightGray
//    }
//
//    val buttonSize by transition.animateDp(label = "buttonSize") { state ->
//        if (state) 90.dp else 56.dp
//    }
//
//    Box(
//        modifier = modifier
//            .size(buttonSize)
//            .clip(CircleShape)
//            .background(buttonBackgroundColor)
//            .offset(x = offsetX.dp)
//            .pointerInput(Unit) {
//                detectDragGestures(
//                    onDragStart = { if (!isRecording) onRecordingStarted() },
//                    onDrag = { change, dragAmount ->
//                        offsetX += dragAmount.x
//
//                        // User is dragging too far right, cancel the recording
//                        if (offsetX > 100f && isRecording) {
//                            onRecordingCanceled()
//                        }
//                    },
//                    onDragEnd = {
//                        if (offsetX < -100f && isRecording) {
//                            // Send the recorded audio
//                            onStopRecording(viewModel.recordedAudioUri.value!!)
//                        } else if (isRecording) {
//                            // User wants to send without dragging far
//                            onStopRecording(viewModel.recordedAudioUri.value!!)
//                        }
//                        offsetX = 0f
//                    },
//                    onDragCancel = {
//                        offsetX = 0f
//                    }
//                )
//            }
//    ) {
//        IconButton(
//            onClick = {
//                if (isRecording) {
//                    // If recording, stop the recording
//                    onStopRecording(viewModel.recordedAudioUri.value!!)
//                } else {
//                    // If not recording, send the text message
//                    onSendClick()
//                }
//            }
//        ) {
//            if (isRecording) {
//                // Show progress indicator while recording
//                CircularProgressIndicator(
//                    modifier = Modifier.fillMaxSize(),
//                    color = Color.White
//                )
//            } else {
//                // Show microphone icon for starting recording
//                Icon(
//                    painter = painterResource(id = R.drawable.mic_chat),
//                    contentDescription = "Record Audio",
//                    tint = Color.White
//                )
//            }
//        }
//    }
//}


//@Composable
//fun AudioRecorder(
//    modifier: Modifier = Modifier,
//    onRecordingStarted: () -> Unit,
//    onRecordingFinished: (Uri) -> Unit,
//    onRecordingCanceled: () -> Unit,
//    isRecording: Boolean,
//    viewModel: ChatViewModel
//) {
//
//    var offsetX by remember { mutableStateOf(0f) }
//    val transition = updateTransition(
//        targetState = if (isRecording) {
//            AudioRecorderState.Recording // No separate locked state, simplify logic
//        } else {
//            AudioRecorderState.Idle
//        }, label = "audioRecorderTransition"
//    )
//
//    val buttonIcon by transition.animateFloat(label = "buttonIcon") { state ->
//        when (state) {
//            AudioRecorderState.Idle -> {
//                0f
//            } // Send icon
//            AudioRecorderState.Recording -> {
//                1f
//            } // Stop icon
//            AudioRecorderState.Locked -> {
//                0f
//            }
//        }
//    }
//
//
//    Box(
//        modifier = modifier
//            .size(50.dp)
//            .clip(CircleShape)
//            .background(MainColor)
//            .offset(offsetX.dp)
//            .pointerInput(Unit) {
//                detectDragGestures(
//                    onDragStart = { if (!isRecording) viewModel.startRecording() },
//                    onDrag = { change, dragAmount ->
//                        offsetX += dragAmount.x
//
//                        // Update the background color based on drag direction
//                        if (offsetX > 0) {
//                            // User is trying to cancel
//                            // You can add a visual cue here, like changing the background color
//                        }
//                    },
//                    onDragCancel = {
//                        if (!isRecording) {
//                            offsetX = 0f
//                        }
//                    },
//                    onDragEnd = {
//                        if (offsetX < -100 && isRecording) {
//                            // User wants to cancel
//                            viewModel.cancelRecording()
//                        } else if (isRecording) {
//                            // User wants to send
//                            viewModel.stopRecording()
//                        }
//                        offsetX = 0f
//                    }
//                )
//            }
//    ) {
//
//        IconButton(
//            onClick = {
//                if (isRecording) {
//                    viewModel.stopRecording()
//                } else {
//                    onSendClick()
//                }
//            }
//        ) {
//            Icon(
//                imageVector = when (buttonIcon.toInt()) {
//                    0 -> Icons.Filled.Send
//                    else -> Icons.Filled.Stop
//                },
//                contentDescription = if (isRecording) "Stop Recording" else "Send",
//                tint = Color.White
//            )
//        }
//
//    }
//}

@Composable
fun AudioMessageItem(message: MessageDataClass, viewModel: ChatViewModel) {
    val audioPlayerState = viewModel.audioPlayerStates[message.senderId] ?: AudioPlayerState(
        ExoPlayer.Builder(LocalContext.current).build()
    )
    var isPlaying by remember { mutableStateOf(audioPlayerState.isPlaying) }

    LaunchedEffect(message.senderId) { // Initialize player with the audio URI
        audioPlayerState.player.setMediaItem(MediaItem.fromUri(message.audio!!))
        audioPlayerState.player.prepare()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Play/Pause Button
        IconButton(onClick = {
            viewModel.playAudio(message, !isPlaying)
            isPlaying = !isPlaying
        }) {
            Icon(
                imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                contentDescription = if (isPlaying) "Pause" else "Play",
                tint = Color.White
            )
        }

        // Audio Visualizer or Progress Bar (Replace with your implementation)
        // You can use the audioPlayerState.player to get playback position and duration
        Box(
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .background(Color.LightGray, RoundedCornerShape(8.dp))
        ) {
            // Implement audio visualizer or progress bar here
        }
    }
}

//@Composable
//fun AudioRecorder(onStopRecording: (Uri) -> Unit) {
//    val context = LocalContext.current
//    var isRecording by remember { mutableStateOf(false) }
//    var recorder: MediaRecorder? by remember { mutableStateOf(null) }
//
//    DisposableEffect(Unit) {
//        onDispose {
//            recorder?.release()
//            recorder = null
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Button(onClick = {
//                if (!isRecording) {
//                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
//                        // Request permission if not granted
//                        // ... (Use ActivityResultLauncher or other permission handling mechanism)
//                        return@Button // Don't proceed if permission isn't granted yet
//                    }
//
//                    // Start recording
//                    recorder = MediaRecorder().apply {
//                        setAudioSource(MediaRecorder.AudioSource.MIC)
//                        setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
//                        setOutputFile(getAudioFilePath(context))
//                        setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
//
//                        try {
//                            prepare()
//                            start()
//                            isRecording = true
//                        } catch (e: IOException) {
//                            Log.e("AudioRecorder", "Prepare or start recording failed: ${e.message}")
//                        }
//                    }
//                } else {
//                    // Stop recording
//                    recorder?.stop()
//                    recorder?.release()
//                    recorder = null
//                    isRecording = false
//
//                    // Notify the ChatScreen with the recorded audio URI
//                    onStopRecording(Uri.fromFile(File(getAudioFilePath(context))))
//                }
//            }) {
//                Text(if (isRecording) "Stop Recording" else "Start Recording")
//            }
//            Spacer(modifier = Modifier.width(16.dp))
//            // ... (Optionally add a visual indicator like a recording animation or timer)
//        }
//        if (isRecording) {
//            // You can add a progress bar or visualizer here while recording
//            LinearProgressIndicator(
//                modifier = Modifier.fillMaxWidth()
//            )
//        }
//    }
//}
//
//private fun getAudioFilePath(context: Context): String {
//    // ... your existing implementation for getAudioFilePath ...
//    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
//    val fileName = "audio_record_$timestamp.mp3" // You can use other formats like .aac, .wav, etc.
//
//    // Get a suitable directory for storing audio files
//    val directory = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//        // On Android 10 (Q) and above, use scoped storage
//        context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
//    } else {
//        // On older Android versions, use external storage
//        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
//    }
//
//    // Create the directory if it doesn't exist
//    if (directory != null) {
//        if (!directory.exists()) {
//            directory.mkdirs()
//        }
//    }
//
//    // Create the full file path
//    return File(directory, fileName).absolutePath
//}

