package com.example.myapplication.myapplication.flashcall.Screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.Data.VideoCallRoute
import com.example.myapplication.myapplication.flashcall.Data.model.SDKResponseState
import com.example.myapplication.myapplication.flashcall.R
import com.example.myapplication.myapplication.flashcall.ViewModel.VideoCallViewModel
import io.getstream.video.android.compose.theme.VideoTheme
import io.getstream.video.android.core.Call
import io.getstream.video.android.core.StreamVideo

@Composable
fun IncomingCallScreen(
    call: Call, navController: NavController, videoCallViewModel: VideoCallViewModel
) {
    CompositionLocalProvider(
        androidx.lifecycle.compose.LocalLifecycleOwner provides androidx.compose.ui.platform.LocalLifecycleOwner.current,
    ) {
        val state = videoCallViewModel.state.sdkResponseState
        if (state == SDKResponseState.Loading) {
            VideoTheme {
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
                    IconButton(
                        onClick = {
                            navController.navigate(ScreenRoutes.MainScreen.route)
                        }, modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Incoming ${videoCallViewModel.state.callType} Call",
                            color = Color.White,
                            style = TextStyle(
                                fontWeight = FontWeight.Medium, fontSize = 22.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(54.dp))

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
                            // Replace with actual profile picture logic
                            Image(
                                painter = painterResource(id = R.drawable.vector), // Placeholder
                                contentDescription = "Profile Picture",
                                modifier = Modifier
                                    .size(160.dp)
                                    .align(Alignment.Center)
                                    .clip(CircleShape)
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

                        Text(
                            text = "Call from",
                            color = Color.White,
                            style = TextStyle(fontSize = 16.sp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = call.user.type.toString(),
                            color = Color.White,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold, fontSize = 25.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(200.dp))

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
                                        videoCallViewModel.rejectCall {
                                            videoCallViewModel.resetCallState()
                                            navController.navigate(ScreenRoutes.MainScreen.route) {
                                                popUpTo(ScreenRoutes.IncomingCallScreen.route) {
                                                    inclusive = true
                                                }
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .size(72.dp)
                                        .background(Color.Red, CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CallEnd,
                                        contentDescription = "Reject Call",
                                        tint = Color.White,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = "Decline", color = Color.White)
                            }

                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                IconButton(
                                    onClick = {
                                        navController.navigate(VideoCallRoute.OngoingVideoCall.videoCallRoute) {
                                            popUpTo(ScreenRoutes.IncomingCallScreen.route) {
                                                inclusive = true
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .size(72.dp)
                                        .background(Color.Green, CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Call,
                                        contentDescription = "Accept Call",
                                        tint = Color.White,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = "Accept", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}
