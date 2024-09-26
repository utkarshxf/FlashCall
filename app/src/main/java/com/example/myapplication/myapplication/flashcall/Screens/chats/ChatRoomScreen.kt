package com.example.myapplication.myapplication.flashcall.Screens.chats

//import androidx.compose.ui.R
//import com.example.myapplication.myapplication.flashcall.ViewModel.chats.WakeLockManager
import android.Manifest
import android.media.MediaPlayer
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@kotlin.OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
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
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val isRecording = chatViewModel.isRecording.value
    var previousSize = listState.layoutInfo.totalItemsCount
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }
    val isKeyboardVisible by rememberUpdatedState(WindowInsets.isImeVisible)
    val density = LocalDensity.current
    val navigationBarHeight = WindowInsets.navigationBars.getBottom(density)
    var showChatCallConfirmation by remember { mutableStateOf(false) }

    LaunchedEffect(messages) {
        if (listState.layoutInfo.totalItemsCount > previousSize) listState.animateScrollToItem(0)
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
    BackHandler {
        showChatCallConfirmation = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape),
                            painter = painterResource(id = R.drawable.profile_picture_holder),
                            contentDescription = "Chat Client Image"
                        )
                        Column(
                            modifier = Modifier.padding(start = 10.dp)
                        ) {
                            Text(
                                text = chatData?.clientName ?: "Naina Talwar",
                                style = TextStyle(
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontFamily = arimoFontFamily,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = "${formatTimeLeft(chatData?.timeLeft ?: 0.0)} mins",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    color = Color.White,
                                    fontFamily = arimoFontFamily,
                                    fontWeight = FontWeight.Normal
                                )
                            )
                            Text(
                                text = "Ongoing Chat",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    color = Color.Green,
                                    fontFamily = arimoFontFamily,
                                    fontWeight = FontWeight.Normal
                                )
                            )
                        }
                    }
                },
                actions = {
                    Button(
                        onClick = {
                            showChatCallConfirmation = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFff5151),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text("End")
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFF6b7280)
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
                .fillMaxSize()
                .paint(
                    painter = painterResource(id = R.drawable.bg_chat_image),
                    contentScale = ContentScale.Crop
                )
                .background(Color(0xffe5ddd5).copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    reverseLayout = true
                ) {
                    when (messages) {
                        is Resource.Success<*> -> {
                            val messageList = messages.data ?: emptyList()
                            val sortedMessages = messageList.sortedByDescending { it.createdAt ?: 0 }
                            var LastMessageDate:LocalDate? = null
                            itemsIndexed(sortedMessages) { index, message ->
                                val currentMessageDate = Instant.ofEpochMilli(sortedMessages[index].createdAt ?: 0)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                                MessageItem(
                                    message = message,
                                    creatorId = uid.toString(),
                                    onMessageSeen = chatViewModel::markMessageAsSeen,
                                )
                                if (index < sortedMessages.size - 1) {
                                    val nextMessageDate = Instant.ofEpochMilli(sortedMessages[index + 1].createdAt ?: 0)
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDate()

                                    if (nextMessageDate != currentMessageDate) {
                                        DateSeparator(date = currentMessageDate)
                                    }
                                } else if (index == sortedMessages.size - 1) {
                                    DateSeparator(date = currentMessageDate)
                                }
                            }
                        }
                        is Resource.Error -> {}
                        is Resource.Loading -> {}
                    }
                }

                // Message Input Section
                Column {
                    if (imageUri != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF6b7280))
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
                        if (!isRecording) {
                            OutlinedTextField(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 8.dp),
                                value = messageText,
                                onValueChange = { messageText = it },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFFffffff),
                                    unfocusedContainerColor = Color(0xFFffffff),
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black,
                                    unfocusedBorderColor = Color.Transparent,
                                    focusedBorderColor = Color.Transparent
                                ),
                                placeholder = {
                                    Text(
                                        text = "Message",
                                        style = TextStyle(
                                            color = Color.Gray,
                                            fontSize = 16.sp,
                                            fontFamily = arimoFontFamily
                                        )
                                    )
                                },
                                shape = RoundedCornerShape(32.dp),
                                trailingIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.attach_file_chat),
                                        contentDescription = "Attach File",
                                        tint = Color.Gray,
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clickable { imagePickerLauncher.launch("image/*") }
                                    )
                                }
                            )
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
    if (showChatCallConfirmation) {
        AlertDialog(
            onDismissRequest = { showChatCallConfirmation = false },
            title = { Text("Are you sure?") },
            text = { Text("Proceeding further will End the Ongoing Chat.") },
            confirmButton = {
                Button(
                    onClick = {
                        showChatCallConfirmation = false
                        chatViewModel.endChat {
                            navController.navigate(ScreenRoutes.MainScreen.route) {
                                popUpTo(ScreenRoutes.ChatRoomScreen.route) { inclusive = true }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Proceed")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showChatCallConfirmation = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
fun formatTimeLeft(timeLeftInSeconds: Double): String {
    val minutes = (timeLeftInSeconds / 60).toInt()
    val seconds = (timeLeftInSeconds % 60).toInt().toString().padStart(2, '0')
    return "$minutes:$seconds"
}
@Composable
fun DateSeparator(date: LocalDate) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Surface(
            color = Color(0xFFE1F3FB),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                text = formatDate(date),
                style = TextStyle(
                    color = Color.Gray,
                    fontSize = 12.sp,
                    fontFamily = arimoFontFamily
                ),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }
    }
}

fun formatDate(date: LocalDate): String {
    val today = LocalDate.now()
    val yesterday = today.minusDays(1)

    return when (date) {
        today -> "Today"
        yesterday -> "Yesterday"
        else -> date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
    }
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
                .drawBehind {
                    val bubblePath = Path().apply {
                        val cornerRadius = 16.dp.toPx()
                        val triangleHeight = 8.dp.toPx()
                        val triangleWidth = 12.dp.toPx()

                        if (isOwnMessage) {
                            moveTo(size.width, cornerRadius)
                            lineTo(size.width, triangleHeight)
                            lineTo(size.width + triangleWidth, 0f)
                            lineTo(size.width, 0f)
                            lineTo(size.width - cornerRadius, 0f)
                        } else {
                            moveTo(0f, cornerRadius)
                            lineTo(0f, triangleHeight)
                            lineTo(-triangleWidth, 0f)
                            lineTo(0f, 0f)
                            lineTo(size.width - cornerRadius, 0f)
                        }

                        arcTo(
                            rect = Rect(
                                size.width - 2 * cornerRadius,
                                0f,
                                size.width,
                                2 * cornerRadius
                            ),
                            startAngleDegrees = 270f,
                            sweepAngleDegrees = 90f,
                            forceMoveTo = false
                        )

                        lineTo(size.width, size.height - cornerRadius)
                        arcTo(
                            rect = Rect(
                                size.width - 2 * cornerRadius,
                                size.height - 2 * cornerRadius,
                                size.width,
                                size.height
                            ),
                            startAngleDegrees = 0f,
                            sweepAngleDegrees = 90f,
                            forceMoveTo = false
                        )

                        lineTo(cornerRadius, size.height)

                        arcTo(
                            rect = Rect(
                                0f,
                                size.height - 2 * cornerRadius,
                                2 * cornerRadius,
                                size.height
                            ),
                            startAngleDegrees = 90f,
                            sweepAngleDegrees = 90f,
                            forceMoveTo = false
                        )

                        lineTo(0f, cornerRadius)
                        close()
                    }

                    drawPath(
                        path = bubblePath,
                        color = if (isOwnMessage) Color(0xFF25D366) else Color.White,
                    )
                }
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