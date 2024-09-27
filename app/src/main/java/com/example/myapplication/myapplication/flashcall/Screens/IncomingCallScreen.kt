package com.example.myapplication.myapplication.flashcall.Screens

import android.util.Log
import androidx.compose.foundation.Canvas
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.Data.VideoCallRoute
import com.example.myapplication.myapplication.flashcall.Data.model.SDKResponseState
import com.example.myapplication.myapplication.flashcall.R
import com.example.myapplication.myapplication.flashcall.Screens.common.maskIfPhoneNumber
import com.example.myapplication.myapplication.flashcall.ViewModel.VideoCallViewModel
import io.getstream.video.android.core.Call

@Composable
fun IncomingCallScreen(
    call: Call,
    navController: NavController,
    videoCallViewModel: VideoCallViewModel
) {
    val state = videoCallViewModel.state.sdkResponseState
    val created =call.state.createdBy.collectAsState()
    if (state == SDKResponseState.Loading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1E1E1E),  // Top color
                            Color(0xFF444444),  // Middle color
                            Color(0xFF121212)   // Bottom color
                        ),
                        startY = 0f,
                        endY = 3000f
                    )
                )
        ) {
            // Close button
            IconButton(
                onClick = { navController.navigate(ScreenRoutes.MainScreen.route) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White
                )
            }

            // Main content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Call type
                Text(
                    text = "Incoming ${videoCallViewModel.state.callType} Call",
                    color = Color.White,
                    style = TextStyle(fontWeight = FontWeight.Normal, fontSize = 18.sp),
                    modifier = Modifier.padding(top = 56.dp)
                )

                // Profile picture with glow effect
                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier.size(300.dp),
                        contentAlignment = Alignment.Center){
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawCircle(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xff595a59),
                                        Color.Transparent
                                    ),
                                    center = center,
                                    radius = size.minDimension / 2.5f
                                )
                            )

                            drawCircle(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xff474847),
                                        Color.Transparent
                                    ),
                                    center = center,
                                    radius = size.minDimension / 3f
                                )

                            )

                            drawCircle(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xff4b4b4b),
                                        Color.Transparent
                                    ),
                                    center = center,
                                    radius = size.minDimension / 6f
                                )
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(92.dp)
                                .background(Color(0xFFf8f8f8), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = if(created.value?.image!= "/images/defaultProfile.png"){
                                    rememberAsyncImagePainter(model =created.value?.image)
                                }else{
                                    painterResource(id = R.drawable.vector_1)
                                },
                                contentDescription = "Profile Picture",
                                modifier = Modifier
                                    .size(82.dp)
                                    .align(Alignment.Center)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "Call from",
                            color = Color.White.copy(alpha = 0.7f),
                            style = TextStyle(fontSize = 16.sp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = maskIfPhoneNumber(created.value?.id?: "Unknown"),
                            color = Color.White,
                            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp)
                        )
                    }
                }

                Spacer(modifier = Modifier.size(120.dp))

                // Call actions
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    CallActionButton(
                        icon = Icons.Default.CallEnd,
                        color = Color.Red,
                        onClick = {
                            videoCallViewModel.rejectCall {
                                videoCallViewModel.resetCallState()
                                navController.navigate(ScreenRoutes.MainScreen.route) {
                                    popUpTo(ScreenRoutes.IncomingCallScreen.route) { inclusive = true }
                                }
                            }
                        },
                        label = "Decline"
                    )
                    CallActionButton(
                        icon = Icons.Default.Call,
                        color = Color(0xFF4CAF50),
                        onClick = {
                            navController.navigate(VideoCallRoute.OngoingVideoCall.videoCallRoute) {
                                popUpTo(ScreenRoutes.IncomingCallScreen.route) { inclusive = true }
                            }
                        },
                        label = "Accept"
                    )
                }
            }
        }
    }
}


@Composable
fun CallActionButton(
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(color, CircleShape)
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                color.copy(alpha = 0.3f),
                                Color.Transparent
                            ),
                            center = Offset(size.width / 2, size.height / 2),
                            radius = size.width * 0.8f
                        ),
                        radius = size.width * 0.8f
                    )
                }
        ) {
            IconButton(
                onClick = onClick,
                modifier = Modifier.matchParentSize()
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = label, color = Color.White, fontSize = 14.sp)
    }
}