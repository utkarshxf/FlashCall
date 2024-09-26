package com.example.myapplication.myapplication.flashcall.Screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.Data.model.Resource
import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.ChatRequestDataClass
import com.example.myapplication.myapplication.flashcall.R
import com.example.myapplication.myapplication.flashcall.ViewModel.chats.ChatRequestViewModel

@Composable
fun IncomingChatScreen(
    chatRequestViewModel: ChatRequestViewModel = hiltViewModel(), // Obtain ViewModel
    navController: NavController
) {
    val chatRequestData by chatRequestViewModel.incomingChatRequest.collectAsState()

//    if(chatRequestCreated) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF232323), // Top color (semi-transparent dark gray)
                            Color.Gray.copy(alpha = 0.85f), // Middle gradient color
                            Color(0xFF232323) // Bottom color (semi-transparent dark gray)
                        )
                    )
                )
        ) {
            ChatRequestContent(
                callerName = chatRequestData?.clientName.toString(),
                navController = navController,
                onAccept = { chatRequestViewModel.acceptChatRequest( chatRequestViewModel.pendingChatRequestDocId.toString())},
                onReject = { chatRequestViewModel.rejectChatRequest( chatRequestViewModel.pendingChatRequestDocId.toString())})



        }
//    }

}



@Composable
fun ChatRequestContent(
    callerName: String,
    navController: NavController,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Incoming Chat Request",
            color = Color.White,
            style = TextStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 22.sp
            )
        )

        Spacer(modifier = Modifier.height(54.dp))

        // Profile picture and other UI elements
        Box(
            modifier = Modifier
                .size(180.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF606060), // Darker gray at the top
                            Color(0xFF707070)  // Lighter gray at the bottom
                        )
                    )
                )
                .padding(4.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.vector), // Placeholder
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(160.dp)
                    .align(Alignment.Center)
                    .clip(CircleShape) // Clip the image to a circular shape
            )
            Spacer(modifier = Modifier.height(2.dp))
            Image(
                painter = painterResource(id = R.drawable.vector_1), // Placeholder
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.Center)
                    .clip(CircleShape) // Clip the image to a circular shape
            )
        }

        Spacer(modifier = Modifier.height(35.dp))

        // Caller Name
        Text(
            text = "Chat from",
            color = Color.White,
            style = TextStyle(
                fontSize = 16.sp
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Call from text
        Text(
            text = callerName,
            color = Color.White,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp
            )
        )

        Spacer(modifier = Modifier.height(200.dp))

        // Accept and Reject buttons
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = {
                        onReject()
                        navController.navigate(ScreenRoutes.MainScreen.route)
                    },
                    modifier = Modifier
                        .size(122.dp)
//                        .background(Color.Red, CircleShape)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.group98), // Placeholder for icon
                        contentDescription = "Decline",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Text(text = "Decline", color = Color.White)
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = {
                        onAccept()
                        navController.navigate(ScreenRoutes.ChatRoomScreen.route)
                    },
                    modifier = Modifier
                        .size(122.dp)
//                        .background(Color.Green, CircleShape)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.group99), // Placeholder for icon
                        contentDescription = "Accept",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Text(text = "Accept", color = Color.White)
            }
        }
    }
}



@Preview
@Composable
fun IncomingChatScreenPreview(){

}