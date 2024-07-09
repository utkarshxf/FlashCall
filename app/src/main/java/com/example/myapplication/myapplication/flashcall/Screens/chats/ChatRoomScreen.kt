package com.example.myapplication.myapplication.flashcall.Screens.chats

import ReceiverMessageCard
import SenderMessageCard
import android.net.Uri
import android.Manifest
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
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import com.example.myapplication.myapplication.flashcall.R
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.layout.ModifierInfo
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import coil.compose.AsyncImage
import com.example.myapplication.myapplication.flashcall.Data.AudioPlayerState
import com.example.myapplication.myapplication.flashcall.Data.AudioRecorderState
import com.example.myapplication.myapplication.flashcall.Data.MessageType
import com.example.myapplication.myapplication.flashcall.Data.model.Resource
import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.MessageDataClass
import com.example.myapplication.myapplication.flashcall.Screens.uriImg
import com.example.myapplication.myapplication.flashcall.ViewModel.chats.ChatViewModel
import com.example.myapplication.myapplication.flashcall.ui.theme.ChatBackground
import com.example.myapplication.myapplication.flashcall.ui.theme.MainColor
import com.example.myapplication.myapplication.flashcall.ui.theme.SecondaryText
import com.example.myapplication.myapplication.flashcall.ui.theme.TimerBackground
import com.example.myapplication.myapplication.flashcall.ui.theme.arimoFontFamily
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatRoomScreen(
    chatViewModel: ChatViewModel = hiltViewModel()
){

    val messages = chatViewModel.messages.collectAsState().value
    Log.d("ChatRoomScreen", "ChatRoomScreen: $messages")

    var messageText by remember {
        mutableStateOf("")
    }

    var imagePicker by remember {
        mutableStateOf(false)
    }

    var chatImageUri by rememberSaveable {
        mutableStateOf("")
    }


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->

        uri?.let {
            uriImg = it
            chatImageUri = it.toString()
        }

        chatViewModel.onImageSelected(uri)
    }

   Surface(
       color = ChatBackground
   ) {
       Column(
           modifier = Modifier.wrapContentSize()
       ) {

           Box(
               modifier = Modifier
                   .fillMaxWidth()
                   .wrapContentHeight()
           ){
               Row(
                   modifier = Modifier
                       .wrapContentSize()
                       .padding(16.dp)
               ) {
                   Image(
                       modifier = Modifier.size(50.dp),
                       painter = painterResource(id = com.example.myapplication.myapplication.flashcall.R.drawable.client_chat_image),
                       contentDescription = "Chat Client Image")

                   Column(
                       modifier = Modifier
                           .wrapContentSize()
                           .padding(start = 16.dp)
                   ){
                       Text(
                           text ="Ongoing Chat with",
                           style = TextStyle(
                               color = Color.White,
                               fontSize = 14.sp,
                               fontFamily = arimoFontFamily,
                               fontWeight = FontWeight.Light
                           )
                       )
                       Spacer(modifier = Modifier.size(8.dp))
                       Text(
                           text ="Naina Talwar",
                           style = TextStyle(
                               color = Color.White,
                               fontSize = 22.sp,
                               fontFamily = arimoFontFamily,
                               fontWeight = FontWeight.Bold
                           )
                       )
                   }

                   Spacer(modifier = Modifier.weight(1f))

                   Button(
                       onClick = {  },
                       modifier = Modifier
                           .width(120.dp)
                           .height(50.dp),
                       shape = RoundedCornerShape(10.dp),
                       colors = ButtonDefaults.buttonColors(
                           containerColor = Color.Red,
                           contentColor = Color.White
                       )
                   ) {
                       Text(text = "End Chat",
                           style = TextStyle(
                               color = Color.White,
                               fontFamily = arimoFontFamily,
                               fontWeight = FontWeight.Bold,
                               fontSize = 16.sp,
                           )
                       )
                   }
               }
           }

           Box(
               modifier = Modifier
                   .fillMaxWidth()
                   .wrapContentHeight()
                   .background(TimerBackground.copy(alpha = 0.1f)),
               contentAlignment = Alignment.Center
           ){
               Row(
                   modifier = Modifier
                       .fillMaxWidth()
                       .background(TimerBackground.copy(alpha = 0.7f))
                       .padding(8.dp),
                   horizontalArrangement = Arrangement.Center,
               ) {
                   Text(text = "Time Left : 01:59",
                       style = TextStyle(
                           fontSize = 16.sp,
                           color = Color.Red,
                           fontFamily = arimoFontFamily,
                           fontWeight = FontWeight.Bold
                       )
                   )
               }
           }

           Column(
               modifier = Modifier.fillMaxSize()
           ) {
               Column(
                   modifier = Modifier.weight(1f)
               ) {
                   LazyColumn(
                       modifier = Modifier
                           .fillMaxWidth(),
//                       reverseLayout = true,
                   ){
                       when(messages){

                           is Resource.Success<*> ->{
                               
                               val messageList = messages.data as?
                                       List<MessageDataClass> ?: emptyList()
                               
                               items(messageList){
                                   Log.d("MessageItemChat", "ChatRoomScreen: ${it.text}")
                                   MessageItem(message = it, creatorId = "6687f55f290500fb85b7ace0",chatViewModel::markMessageAsSeen)
                               }
                           }

                           is Resource.Error -> {
                               Log.d("ChatRoomScreen", "ChatRoomScreen: Error")
                           }
                           is Resource.Loading -> {
                               Log.d("ChatRoomScreen", "ChatRoomScreen: Loading")
                           }
                       }
//                       item {
//                           ReceiverMessageCard("Hello this is testing")
//                       }
//                       item {
//                           SenderMessageCard("My Name is Sujit")
//                       }
//                       item{
//                           ReceiverMessageCard("What is your Name")
//                       }
                   }
               }

               var progress by remember {
                   mutableStateOf(false)
               }

               Row(
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(10.dp),
                   verticalAlignment = Alignment.Bottom,
                   horizontalArrangement = Arrangement.SpaceBetween
               ) {
                   OutlinedTextField(
                       modifier = Modifier
                           .height(IntrinsicSize.Max)
                           .width(285.dp),
                       value = messageText,
                       keyboardOptions = KeyboardOptions(
                           imeAction = ImeAction.Send
                       ),
                       keyboardActions = KeyboardActions(
                           onSend = {
                               if(messageText.isNotEmpty())
                                   chatViewModel.sendMessage(messageText,MessageType.TEXT)
                               messageText = ""
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
                           Text(text = if(progress) "Recording..." else "Message",
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
                                       .clickable {
                                           launcher.launch("image/*")
                                       },
                                   painter = painterResource(id = R.drawable.attach_file_chat),
                                   contentDescription = "Attach File"
                               )
                               Icon(
                                   modifier = Modifier
                                       .size(24.dp)
                                       .clickable {
                                           launcher.launch("camera/*")
                                       },
                                   painter = painterResource(id = R.drawable.camera_icon_chat),
                                   contentDescription = "Camera"
                               )
                           }
                       }
                   )

//                   AudioRecorderButton(
//                       onRecordingStarted = { chatViewModel::startRecording },
//                       onRecordingCanceled = { chatViewModel.cancelRecording() },
//                       onSendClick = {
//                           chatViewModel.sendMessage(messageText,MessageType.TEXT)
//                           messageText = ""
//                                     },
//                       onStopRecording = {
//                                         chatViewModel::stopRecording
//                       },
//                       isRecording = chatViewModel.isRecording.value,
//                       viewModel = chatViewModel
//                   )

                   val recordingState by chatViewModel.isRecording.collectAsState()


                   Button(
                       modifier = Modifier
                           .padding(bottom = 2.dp)
                           .clip(CircleShape)
                           .size(50.dp),
                       onClick = {
                                 recordingState.let {
                                     if(it == false) {
                                         chatViewModel.startRecording()
                                         progress = true
                                     }
                                     else{
                                         chatViewModel.stopRecording()
                                         progress = false
                                     }
                                 }
                       },
                       shape = CircleShape,
                       contentPadding = PaddingValues(0.dp),
                       colors = ButtonDefaults.buttonColors(
                           containerColor = MainColor,
                           contentColor = Color.Black
                       )
                   ) {
                       Icon(
                           modifier = Modifier
                               .size(34.dp)
                               .clip(CircleShape),
                           painter = painterResource(id = R.drawable.mic_chat),
                           contentDescription = "Send"
                       )
                   }
               }
           }
       }
   }
}

@Composable
fun MessageItem(
    message : MessageDataClass,
    creatorId : String,
    onMessageSeen : (MessageDataClass) -> Unit
){

    val isOwnMessage = message.senderId == creatorId
    val formattedTime = message.createdAt?.let {
        SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(it))
    } ?: ""

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if(isOwnMessage) Arrangement.End else Arrangement.Start,
    ){

        if(message.text != null)
        {
            Card(
                modifier = Modifier
                    .padding(10.dp)
                    .height(IntrinsicSize.Min)
                    .width(IntrinsicSize.Max),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                ),
                shape = if(isOwnMessage)
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 0.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 8.dp
                    )
                else
                    RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 16.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 8.dp
                    )
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 8.dp, end = 10.dp, top = 10.dp)
                        .height(IntrinsicSize.Max)
                        .wrapContentWidth(),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                    text = message.text,
                    maxLines = 3,
                    modifier = Modifier.padding(start = 8.dp)
                    )
                    Text(
                        text = "$formattedTime",
                        fontSize = 8.sp,
                        color = Color.DarkGray,
//                        modifier = Modifier.padding(top = 4.dp)
                    )
//                    Icon(
//                        modifier = Modifier
//                            .size(18.dp)
//                            .padding(start = 4.dp),
//                        imageVector = Icons.Default.Done,
//                        tint = if (messageStatus == MessageStatus.READ) Color.Blue
//                        else Color.Gray,
//                        contentDescription = "messageStatus"
//                    )
                    if (isOwnMessage && message.seen == true) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "Seen",
                            tint = Color.Blue, // You can customize the color
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                    Log.d("MessageItemChat", "MessageItem: ${message.text}")
                }
            }
        }
        if(message.img != null)
        {
            Card(
                modifier = Modifier
                    .padding(10.dp)
                    .height(IntrinsicSize.Min)
                    .width(IntrinsicSize.Min),
                elevation = CardDefaults.cardElevation(
                ),
                shape = if(isOwnMessage)
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 0.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 8.dp
                    )
                else
                    RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 16.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 8.dp
                    )
            ) {
                Column(
                    modifier = Modifier
                        .padding(0.dp)
                        .height(IntrinsicSize.Min),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Top
                ) {
                    AsyncImage(
                        model = message.img,
                        contentDescription = "" ,
                        modifier = Modifier
                            .size(120.dp)
                            .rotate(90f)
                            .padding(0.dp)
                    )
                    Text(
                        text = "$formattedTime",
                        fontSize = 12.sp,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }



        LaunchedEffect(message) {
            if (!isOwnMessage && !message.seen!!) {
                onMessageSeen(message)
            }
        }


    }
}


@Composable
fun AudioRecorderButton(
    modifier: Modifier = Modifier,
    onRecordingStarted: () -> Unit,
    onRecordingCanceled: () -> Unit,
    onSendClick: () -> Unit,
    onStopRecording: (Uri?) -> Unit,
    isRecording: Boolean,
    viewModel: ChatViewModel
) {
    val context = LocalContext.current
    var offsetX by remember { mutableStateOf(0f) }

    val icon = if (isRecording){
        painterResource(id = R.drawable.mic_chat)
     }
    else {
        painterResource(id = R.drawable.mic_chat)

    }

    val transition = updateTransition(
        targetState = if (isRecording) AudioRecorderState.Recording else AudioRecorderState.Idle,
        label = "audioRecorderTransition"
    )

    val buttonBackgroundColor by transition.animateColor(label = "backgroundColor") { state ->
        when (state) {
            AudioRecorderState.Idle -> Color.LightGray
            AudioRecorderState.Recording -> Color.Red
            AudioRecorderState.Locked -> TODO()
        }
    }

    val buttonSize by transition.animateDp(label = "buttonSize") { state ->
        when (state) {
            AudioRecorderState.Idle -> 56.dp
            AudioRecorderState.Recording -> 90.dp
            AudioRecorderState.Locked -> TODO()
        }
    }

    val buttonIcon by transition.animateFloat(label = "buttonIcon") { state ->
        when (state) {
            AudioRecorderState.Idle -> 0f // Send icon
            AudioRecorderState.Recording -> 1f // Stop icon
            AudioRecorderState.Locked -> TODO()
        }
    }

    Box(
        modifier = modifier
            .size(buttonSize)
            .clip(CircleShape)
            .background(buttonBackgroundColor)
            .offset(x = offsetX.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { if (!isRecording) onRecordingStarted() },
                    onDrag = { change, dragAmount ->
                        offsetX += dragAmount.x

                        // Update the background color based on drag direction
                        if (offsetX > 100) {
                            // User is trying to cancel
                        }
                    },
                    onDragCancel = {
                        if (!isRecording) {
                            offsetX = 0f
                        }
                    },
                    onDragEnd = {
                        if (offsetX < -100 && isRecording) {
                            // User wants to cancel
                            onRecordingCanceled()
                        } else if (isRecording) {
                            // User wants to send
                            onStopRecording(viewModel.recordedAudioUri.value!!)
                        }
                        offsetX = 0f
                    }
                )
            }
    ) {
        IconButton(
            onClick = {
                if (isRecording) {
                    onStopRecording(null)
                } else {
                    onSendClick()
                }
            }
        ) {
            // Button Content
            if (isRecording) {
                // Recording Animation (Circular Progress Bar)
                CircularProgressIndicator(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                )
            } else {
                // Send/Mic icon
                Icon(
                    painter = icon,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}



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

//@Composable
//fun AudioMessageItem(message: MessageDataClass, viewModel: ChatViewModel) {
//    val audioPlayerState = viewModel.audioPlayerStates[message.senderId] ?: AudioPlayerState(
//        ExoPlayer.Builder(LocalContext.current).build()
//    )
//    var isPlaying by remember { mutableStateOf(audioPlayerState.isPlaying) }
//
//    LaunchedEffect(message.senderId) { // Initialize player with the audio URI
//        audioPlayerState.player.setMediaItem(MediaItem.fromUri(message.audio!!))
//        audioPlayerState.player.prepare()
//    }
//
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp),
//        horizontalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        // Play/Pause Button
//        IconButton(onClick = {
//            viewModel.onPlayPause(message, !isPlaying)
//            isPlaying = !isPlaying
//        }) {
//            Icon(
//                imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
//                contentDescription = if (isPlaying) "Pause" else "Play",
//                tint = Color.White
//            )
//        }
//
//        // Audio Visualizer or Progress Bar (Replace with your implementation)
//        // You can use the audioPlayerState.player to get playback position and duration
//        Box(
//            modifier = Modifier
//                .weight(1f)
//                .height(48.dp)
//                .background(Color.LightGray, RoundedCornerShape(8.dp))
//        ) {
//            // Implement audio visualizer or progress bar here
//        }
//    }
//}