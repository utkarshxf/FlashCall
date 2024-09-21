package com.example.myapplication.myapplication.flashcall.Screens.chats

//import androidx.compose.ui.R
//import com.example.myapplication.myapplication.flashcall.ViewModel.chats.WakeLockManager
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.myapplication.flashcall.Data.MessageType
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.Data.model.Resource
import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.MessageDataClass
import com.example.myapplication.myapplication.flashcall.R
import com.example.myapplication.myapplication.flashcall.ViewModel.AuthenticationViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.chats.ChatViewModel
import com.example.myapplication.myapplication.flashcall.ui.theme.arimoFontFamily
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
                .background(Color(0xff232323).copy(alpha = 0.8f))
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
                    text = "Time Left: ${formatTimeLeft(chatData?.timeLeft ?: 0.0)}", style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.White,
                        fontFamily = arimoFontFamily,
                        fontWeight = FontWeight.Normal
                    ), modifier = Modifier.padding(vertical = 5.dp)
                )
            }

            // Messages List
            LazyColumn(
                state = listState, // Use the LazyListState
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

            // Message Input and Actions
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2C2C2C))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
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
                                color = Color.Gray, fontSize = 16.sp, fontFamily = arimoFontFamily
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
                        .clickable { /* Handle attachment */ })
                Spacer(modifier = Modifier.width(8.dp))
                Icon(painter = painterResource(id = R.drawable.camera_icon_chat),
                    contentDescription = "Camera",
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { /* Handle camera */ })
                Spacer(modifier = Modifier.width(8.dp))
                if (messageText.isNotBlank()) {
                    IconButton(
                        onClick = {
                            chatViewModel.sendMessage(
                                messageContent = messageText, messageType = MessageType.TEXT
                            )
                            messageText = ""
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
                    AudioRecorderButton(chatViewModel = chatViewModel)
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

@Composable
fun MessageItem(
    message: MessageDataClass,
    creatorId: String,
    onMessageSeen: (MessageDataClass) -> Unit,
    viewModel: ChatViewModel
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
                Text(
                    text = message.text ?: "",
                    color = if (isOwnMessage) Color.White else Color.Black,
                    style = TextStyle(fontSize = 16.sp, fontFamily = arimoFontFamily)
                )
                Text(
                    text = "$formattedTime ✓✓",
                    color = if (isOwnMessage) Color.White.copy(alpha = 0.7f) else Color.Gray,
                    style = TextStyle(fontSize = 10.sp, fontFamily = arimoFontFamily),
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

@Composable
fun AudioRecorderButton(
    chatViewModel: ChatViewModel
) {
    val isRecording by chatViewModel.isRecording.collectAsState()

    IconButton(
        onClick = {
            if (isRecording) {
                chatViewModel.stopRecording()
            } else {
                chatViewModel.startRecording()
            }
        }, modifier = Modifier
            .size(40.dp)
            .background(Color(0xFF25D366), CircleShape)
    ) {
        Icon(
            imageVector = Icons.Default.Mic,
            contentDescription = if (isRecording) "Stop Recording" else "Start Recording",
            tint = Color.White
        )
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

