package com.example.myapplication.myapplication.flashcall.Screens.chats

//import androidx.compose.ui.R
//import com.example.myapplication.myapplication.flashcall.ViewModel.chats.WakeLockManager
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.myapplication.flashcall.Data.MessageType
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.Data.model.Resource
import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.MessageDataClass
import com.example.myapplication.myapplication.flashcall.R
import com.example.myapplication.myapplication.flashcall.ViewModel.AuthenticationViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.chats.ChatViewModel
import com.example.myapplication.myapplication.flashcall.ui.theme.arimoFontFamily
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.delay
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit

@Composable
fun ChatRoomScreen(
    navController: NavController,
    chatViewModel: ChatViewModel = hiltViewModel(),
    authenticationViewModel: AuthenticationViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val userData = authenticationViewModel.getUserFromPreferences(context)
    val uid = userData?._id
    val messages = chatViewModel.messages.collectAsState().value
    var messageText by remember { mutableStateOf("") }
    val chatData by chatViewModel.chatData.collectAsState()
    val callEnded by chatViewModel.endedCall.collectAsState()
    val listState = rememberLazyListState()
    var previousSize = listState.layoutInfo.totalItemsCount
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }
    var micRecordingUri by remember { mutableStateOf<Uri?>(null) }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageUri = cameraImageUri
        }
    }
    val isRecording = chatViewModel.isRecording.value
    LaunchedEffect(messages) {
        if (listState.layoutInfo.totalItemsCount > previousSize) listState.animateScrollToItem(
            listState.layoutInfo.totalItemsCount - 1
        )
        previousSize = listState.layoutInfo.totalItemsCount
    }
    LaunchedEffect(callEnded) {
        delay(3000)
        if (callEnded) {
            navController.navigate(ScreenRoutes.MainScreen.route) {
                popUpTo(ScreenRoutes.ChatRoomScreen.route) { inclusive = true }
            }
        }
    }
    DisposableEffect(Unit) {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                chatViewModel.endChat {
                    navController.navigate(ScreenRoutes.MainScreen.route) {
                        popUpTo(ScreenRoutes.ChatRoomScreen.route) { inclusive = true }
                    }
                }
            }
        }
        val dispatcher = (context as? ComponentActivity)?.onBackPressedDispatcher
        dispatcher?.addCallback(callback)
        onDispose {
            callback.remove()
        }
    }

    Surface(
        color = Color(0xFF1E1E1E)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painter = painterResource(id = R.drawable.bg_chat_image),
                    contentScale = ContentScale.Crop
                )
                .background(Color(0xff232323).copy(alpha = 0.5f))
        )
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2C2C2C))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier.size(40.dp),
                        painter = painterResource(id = R.drawable.profile_picture_holder),
                        contentDescription = "Chat Client Image"
                    )
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 10.dp)
                    ) {
                        Text(
                            text = "On going chat with", style = TextStyle(
                                color = Color.White,
                                fontSize = 12.sp,
                                fontFamily = arimoFontFamily,
                                fontWeight = FontWeight.Normal
                            )
                        )
                        Text(
                            text = chatData?.clientName ?: "Naina Talwar", style = TextStyle(
                                color = Color.White,
                                fontSize = 18.sp,
                                fontFamily = arimoFontFamily,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    Button(
                        onClick = {
                            chatViewModel.endChat {
                                navController.navigate(ScreenRoutes.MainScreen.route) {
                                    popUpTo(ScreenRoutes.ChatRoomScreen.route) { inclusive = true }
                                }
                            }
                        },
                        modifier = Modifier
                            .width(100.dp)
                            .height(40.dp),
                        shape = RoundedCornerShape(5.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF4D4F), contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "End", style = TextStyle(
                                color = Color.White,
                                fontFamily = arimoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        )
                    }
                }
            }

            // Timer Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF800000)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Time Left: ${formatTimeLeft(chatData?.timeLeft ?: 0.0)}",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.White,
                        fontFamily = arimoFontFamily,
                        fontWeight = FontWeight.Normal
                    ),
                    modifier = Modifier.padding(vertical = 5.dp)
                )
            }

            // Messages List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                reverseLayout = false
            ) {
                item {
                    Text(
                        text = "07 Dec 2024",
                        style = TextStyle(
                            color = Color.Gray, fontSize = 12.sp, fontFamily = arimoFontFamily
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        textAlign = TextAlign.Center
                    )
                }
                when (messages) {
                    is Resource.Success<*> -> {
                        val messageList = messages.data ?: emptyList()
                        val sortedMessages = messageList.sortedBy { it.createdAt ?: 0 }
                        items(sortedMessages) { message ->
                            MessageItem(
                                message = message,
                                creatorId = uid.toString(),
                                onMessageSeen = chatViewModel::markMessageAsSeen,
                            )
                        }
                    }

                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                }
            }

            Column {
                if (imageUri != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF3E3E3E))
                            .padding(8.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(imageUri),
                            contentDescription = "Selected Image",
                            modifier = Modifier
                                .height(100.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        IconButton(
                            onClick = { imageUri = null },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .background(Color(0x80000000), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove Image",
                                tint = Color.White
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!isRecording)
                    {
                        OutlinedTextField(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp),
                            value = messageText,
                            onValueChange = { messageText = it },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF3E3E3E),
                                unfocusedContainerColor = Color(0xFF3E3E3E),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = Color.Transparent
                            ),
                            placeholder = {
                                Text(
                                    text = "Message", style = TextStyle(
                                        color = Color.Gray,
                                        fontSize = 16.sp,
                                        fontFamily = arimoFontFamily
                                    )
                                )
                            },
                            shape = RoundedCornerShape(20.dp)
                        )
                        Icon(painter = painterResource(id = R.drawable.attach_file_chat),
                            contentDescription = "Attach File",
                            tint = Color.Gray,
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { imagePickerLauncher.launch("image/*") })
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    if (messageText.isNotBlank() || imageUri != null) {
                        IconButton(
                            onClick = {
                                if (imageUri != null) {
                                    chatViewModel.onImageSelected(imageUri!!, messageText)
                                } else {
                                    chatViewModel.sendMessage(
                                        messageContent = messageText,
                                        messageType = MessageType.TEXT,
                                        text = null
                                    )
                                }
                                messageText = ""
                                imageUri = null
                            },
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color(0xFF25D366), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Send Message",
                                tint = Color.White
                            )
                        }
                    } else {
                        AudioRecorderButton(chatViewModel)
                    }
                }
            }
        }
    }
}

fun formatTimeLeft(timeLeftInSeconds: Double): String {
    val minutes = (timeLeftInSeconds / 60).toInt()
    val seconds = (timeLeftInSeconds % 60).toInt().toString().padStart(2, '0')
    return "$minutes:$seconds"
}


@OptIn(UnstableApi::class)
@Composable
fun MessageItem(
    message: MessageDataClass, creatorId: String, onMessageSeen: (MessageDataClass) -> Unit
) {
    val isOwnMessage = message.senderId == creatorId
    val formattedTime = message.createdAt?.let {
        SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(it))
    } ?: ""

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = if (isOwnMessage) Alignment.End else Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = if (isOwnMessage) Color(0xFF25D366) else Color.White,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(8.dp)
        ) {
            Column {
                if (message.text != null && message.img == null && message.audio == null) {
                    Text(
                        text = message.text,
                        color = if (isOwnMessage) Color.White else Color.Black,
                        style = LocalTextStyle.current.copy(
                            fontSize = 16.sp, fontFamily = arimoFontFamily
                        )
                    )
                }
                if (message.img != null && message.text == null && message.audio == null) {
                    Image(
                        painter = rememberAsyncImagePainter(model = message.img),
                        contentDescription = "Sent image",
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center,
                    )
                }
                if (message.img != null && message.text != null) {
                    Card {
                        Box(
                            modifier = Modifier
                                .size(300.dp, 400.dp) // Set maximum size for the box
                                .clip(RoundedCornerShape(10.dp)) // Optional: Rounded corners
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(model = message.img),
                                contentDescription = "Sent image",
                                modifier = Modifier.fillMaxSize(), // Fill the box
                                contentScale = ContentScale.Fit, // Maintain aspect ratio
                                alignment = Alignment.Center,
                            )
                        }
                        if (message.text != "") {
                            Text(
                                text = message.text,
                                color = if (isOwnMessage) Color.White else Color.Black,
                                style = LocalTextStyle.current.copy(
                                    fontSize = 16.sp, fontFamily = arimoFontFamily
                                ),
                                modifier = Modifier.padding(8.dp),
                            )
                        }

                    }
                }
                if (message.audio != null) {
                    AudioPlayerComponent(
                        audioUrl = message.audio,
                        isOwnMessage = isOwnMessage
                    )
                }
                Row(
                    modifier = Modifier.align(Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formattedTime,
                        color = if (isOwnMessage) Color.White.copy(alpha = 0.7f) else Color.Gray,
                        style = LocalTextStyle.current.copy(
                            fontSize = 10.sp, fontFamily = arimoFontFamily
                        )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (message.seen == true) "✓✓" else "✓",
                        color = if (isOwnMessage) Color.White.copy(alpha = 0.7f) else Color.Gray,
                        style = LocalTextStyle.current.copy(
                            fontSize = 10.sp, fontFamily = arimoFontFamily
                        )
                    )
                }
            }
        }
    }

    LaunchedEffect(message) {
        if (!isOwnMessage && message.seen != true) {
            onMessageSeen(message)
        }
    }
}

// UI Button logic
@kotlin.OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AudioRecorderButton(
    chatViewModel: ChatViewModel
) {
    val recordAudioPermissionState = rememberPermissionState(Manifest.permission.RECORD_AUDIO)
    var showRecorder by remember { mutableStateOf(false) }
    val isRecording = chatViewModel.isRecording.value

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!recordAudioPermissionState.status.isGranted) {
            LaunchedEffect(Unit) {
                recordAudioPermissionState.launchPermissionRequest()
            }
            Text(
                "Please grant the RECORD_AUDIO permission to use the audio recorder.",
                modifier = Modifier.padding(8.dp),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium // Use body style for better readability
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (isRecording) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(0.9f),
                        color = Color.Red,
                    )
                }
                IconButton(
                    onClick = {
                        showRecorder = !showRecorder
                        if (showRecorder) {
                            chatViewModel.startRecording()
                        } else {
                            chatViewModel.stopRecording()
                        }
                    },
                    modifier = Modifier
                        .background(
                            if (isRecording) Color.Red else Color(0xFF25D366),
                            CircleShape
                        )
                ) {
                    Icon(
                        imageVector = if (isRecording) Icons.Default.MicOff else Icons.Default.Mic,
                        contentDescription = if (isRecording) "Stop Recording" else "Start Recording",
                        tint = Color.White
                    )
                }
            }
        }
    }
}


@Composable
fun AudioPlayerComponent(audioUrl: String, isOwnMessage: Boolean) {
    var isPlaying by remember { mutableStateOf(false) }
    var isPrepared by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0f) }
    var duration by remember { mutableStateOf(0) }
    var currentPosition by remember { mutableStateOf(0) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val mediaPlayer = remember { MediaPlayer() }

    var manualDuration by remember { mutableStateOf(0L) }
    var startTime by remember { mutableStateOf(0L) }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }

    fun prepareMediaPlayer() {
        isLoading = true
        errorMessage = null
        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(context, Uri.parse(audioUrl))
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener { mp ->
                isPrepared = true
                duration = mp.duration
                Log.d("AudioPlayerComponent", "Audio prepared. MediaPlayer Duration: $duration ms")
                if (duration <= 0) {
                    Log.w("AudioPlayerComponent", "Invalid duration from MediaPlayer, will use manual tracking")
                }
                isLoading = false
            }
            mediaPlayer.setOnErrorListener { _, what, extra ->
                Log.e("AudioPlayerComponent", "MediaPlayer error: what=$what, extra=$extra")
                errorMessage = "Error loading audio: MediaPlayer error $what"
                isPrepared = false
                isLoading = false
                true
            }
        } catch (e: Exception) {
            Log.e("AudioPlayerComponent", "Error preparing MediaPlayer", e)
            errorMessage = "Error loading audio: ${e.message}"
            isPrepared = false
            isLoading = false
        }
    }

    Column(modifier = Modifier.width(300.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(48.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = if (isOwnMessage) Color.White else MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    IconButton(
                        onClick = {
                            if (!isPrepared && !isLoading) {
                                prepareMediaPlayer()
                            } else if (isPrepared) {
                                if (isPlaying) {
                                    mediaPlayer.pause()
                                    isPlaying = false
                                    manualDuration += System.currentTimeMillis() - startTime
                                } else {
                                    mediaPlayer.seekTo(currentPosition)
                                    mediaPlayer.start()
                                    isPlaying = true
                                    startTime = System.currentTimeMillis()
                                }
                                Log.d("AudioPlayerComponent", "Play/Pause clicked. isPlaying: $isPlaying")
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = if (isOwnMessage) Color.White else Color.Black
                        )
                    }
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Slider(
                    value = progress,
                    onValueChange = { newProgress ->
                        if (isPrepared) {
                            progress = newProgress
                            val newPosition = (if (duration > 0) duration else manualDuration.toInt()) * progress
                            mediaPlayer.seekTo(newPosition.toInt())
                            currentPosition = newPosition.toInt()
                            Log.d("AudioPlayerComponent", "Slider moved. Progress: $progress, New position: $newPosition")
                        }
                    },
                    enabled = isPrepared && !isLoading,
                    colors = SliderDefaults.colors(
                        thumbColor = if (isOwnMessage) Color.White else MaterialTheme.colorScheme.primary,
                        activeTrackColor = if (isOwnMessage) Color.White else MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = if (isOwnMessage) Color.White.copy(alpha = 0.3f) else MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                    )
                )

                Text(
                    text = "${formatTime(currentPosition.toLong())} / ${formatTime(if (duration > 0) duration.toLong() else manualDuration.toLong())}",
                    color = if (isOwnMessage) Color.White else Color.Black,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }

        errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }

    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            delay(100)
            if (mediaPlayer.isPlaying) {
                if (duration > 0) {
                    currentPosition = mediaPlayer.currentPosition
                    progress = currentPosition.toFloat() / duration
                } else {
                    // Manual tracking
                    val elapsedTime = System.currentTimeMillis() - startTime
                    currentPosition = (manualDuration + elapsedTime).toInt()
                    progress = if (currentPosition > 0) currentPosition.toFloat() / (manualDuration + elapsedTime) else 0f
                }
                Log.d("AudioPlayerComponent", "Updated position: $currentPosition, Progress: $progress")
            } else {
                isPlaying = false
            }
        }
    }

    DisposableEffect(Unit) {
        mediaPlayer.setOnCompletionListener {
            isPlaying = false
            progress = 1f
            currentPosition = if (duration > 0) duration else manualDuration.toInt()
            Log.d("AudioPlayerComponent", "Playback completed")
        }
        onDispose {
            mediaPlayer.release()
        }
    }
}

private fun formatTime(timeMs: Long): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(timeMs)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(timeMs) % 60
    return String.format("%02d:%02d", minutes, seconds)
}