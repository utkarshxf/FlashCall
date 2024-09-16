package com.example.myapplication.myapplication.flashcall.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.R

@Composable
fun IncomingAudioCallScreen(
    callerName: String,
    navController: NavController
) {
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
    )  {
        IconButton(
            onClick = {
                navController.navigate(ScreenRoutes.MainScreen.route)
            },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Incoming Audio Call",
                color = Color.White,
                style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 22.sp
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
                text = "Call from",
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
                Column(verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {


                    IconButton(
                        onClick = {
                            navController.navigate(ScreenRoutes.MainScreen.route)
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
                Column (
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){


                    IconButton(
                        onClick = {

                            navController.navigate(ScreenRoutes.InCallScreen.route)
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

        // Close Icon

    }
}

@Composable
fun InCallScreen(
    callerName: String,
    timeLeft: String,
    callDuration: String,
    navController: NavController
) {
    val isTimeLeft = true
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xFF232323)
            )
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        IconButton(
            onClick = {
                navController.navigate(ScreenRoutes.MainScreen.route)
            },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Time Left
            Box(
                modifier = Modifier
                    .background(
                        color = if (isTimeLeft) {
                            Color.Red.copy(alpha = 0.3f)
                        } else {
                            Color.Gray
                        },
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Time Left : $timeLeft",
                    color = if (isTimeLeft){
                        Color.Red
                    }else{
                        Color.White
                    },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(112.dp))

            // Caller Images and Audio Wave Icon
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {


                Image(
                    painter = painterResource(id = R.drawable.home_image), // Placeholder image
                    contentDescription = "Caller Image",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                )

                Icon(
                    painter = painterResource(id = R.drawable.voice1), // Placeholder for audio wave icon
                    contentDescription = "Audio Wave",
                    tint = Color.White,
                    modifier = Modifier.size(64.dp)
                )

                Image(
                    painter = painterResource(id = R.drawable.vector_1), // Placeholder image
                    contentDescription = "Caller Image",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
            }

            Spacer(modifier = Modifier.height(114.dp))

            // In Call Text
            Text(
                text = "In Call with",
                color = Color.Green,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Caller Name
            Text(
                text = callerName,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Call Duration
            Text(
                text = callDuration,
                color = Color.Gray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(228.dp))

            // Bottom Call Controls
            Divider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Speaker Button
                IconButton(
                    onClick = {
                        // Handle speaker action
                    },
                    modifier = Modifier
                        .size(92.dp)

                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.speaker), // Placeholder for speaker icon
                        contentDescription = "Speaker",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }



                // End Call Button
                IconButton(
                    onClick = {
                        navController.navigate(ScreenRoutes.MainScreen.route)
                    },
                    modifier = Modifier
                        .size(68.dp)
                        .background(Color.Red, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.CallEnd, // Use the default CallEnd icon
                        contentDescription = "End Call",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }




                // Mute Button
                IconButton(
                    onClick = {
                        // Handle mute action
                    },
                    modifier = Modifier
                        .size(92.dp)

                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.mic), // Placeholder for mute icon
                        contentDescription = "Mute",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AudioCallPreview(){
    InCallScreen(callerName = "Ram", timeLeft = "19:11", callDuration ="12:29" , navController = rememberNavController() )
    //IncomingAudioCallScreen(callerName = "Mohd Gauri", navController = rememberNavController())
}
