package com.example.myapplication.myapplication.flashcall.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Report
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.myapplication.myapplication.flashcall.Data.model.orderHistory.Call
import com.example.myapplication.myapplication.flashcall.Data.model.orderHistory.block.request.BlockUnblockRequestBody
import com.example.myapplication.myapplication.flashcall.Data.model.orderHistory.report.reportRequest.ReportRequestBody
import com.example.myapplication.myapplication.flashcall.Data.model.orderHistory.report.reportRequest.SubmittedBy
import com.example.myapplication.myapplication.flashcall.R
import com.example.myapplication.myapplication.flashcall.ViewModel.OrderHistoryViewModel
import com.example.myapplication.myapplication.flashcall.ui.theme.MainColor
import com.example.myapplication.myapplication.flashcall.ui.theme.PrimaryBackGround
import com.example.myapplication.myapplication.flashcall.ui.theme.PrimaryText
import com.example.myapplication.myapplication.flashcall.ui.theme.SecondaryText
import com.example.myapplication.myapplication.flashcall.ui.theme.helveticaFontFamily
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone


@Composable
fun OrderHistoryScreen(
    navController: NavController,
    viewModel: OrderHistoryViewModel = hiltViewModel()
) {
    val orderHistory by viewModel.orderHistory.collectAsState()
    var isCallsSelected by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = Unit) {
        viewModel.getOrderHistory(
            "66fd37a1735a8e07837d5d99",
            1,
            10
        ) // Replace "userId" with actual user ID
    }

    Surface(
        modifier = Modifier.fillMaxSize(), color = PrimaryBackGround
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            Spacer(modifier = Modifier.height(35.dp))

            Icon(Icons.Default.KeyboardArrowLeft,
                contentDescription = "Back",
                tint = Color.Black,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { navController.popBackStack() })

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Order History", color = Color.Black, style = TextStyle(
                    fontFamily = helveticaFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row {
                Button(modifier = Modifier,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCallsSelected) MainColor else SecondaryText,
                        contentColor = Color.White
                    ),
                    onClick = { isCallsSelected = true }) {
                    Text(
                        text = "Calls", style = TextStyle(
                            fontFamily = helveticaFontFamily,
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                        ), maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Button(modifier = Modifier,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!isCallsSelected) MainColor else SecondaryText,
                        contentColor = Color.White
                    ),
                    onClick = { isCallsSelected = false }) {
                    Text(
                        text = "Chats", style = TextStyle(
                            fontFamily = helveticaFontFamily,
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                        ), maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                if (isCallsSelected) {
                    orderHistory?.calls?.let { calls ->
                        items(calls) { call ->
                            CallCardView(call)
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                } else {
                    item {
                        Text("Chat history not implemented yet")
                    }
                }
            }
        }
    }
}

@Composable
fun CallCardView(call: Call, viewModel: OrderHistoryViewModel = hiltViewModel()) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(12.dp))
            .background(color = PrimaryBackGround)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(call.members.firstOrNull()?.custom?.image ?: "").crossfade(true).build(),
                contentDescription = null,
                placeholder = painterResource(id = R.drawable.profile_picture_holder),
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .border(1.dp, color = MainColor, shape = CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = call.members.firstOrNull()?.custom?.name ?: "Unknown",
                    color = Color.Black,
                    fontFamily = helveticaFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )


                Text(
                    text = call.type.capitalize(),
                    color = SecondaryText,
                    fontFamily = helveticaFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = call.status,
                        color = when (call.status) {
                            "Ended" -> Color.Green
                            "Rejected" -> Color.Red
                            else -> MainColor
                        },
                        fontFamily = helveticaFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )

                    if (call.duration != null) {
                        Text(
                            text = " • ${formatDuration(call.duration)}",
                            color = PrimaryText,
                            fontFamily = helveticaFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Column(
                horizontalAlignment = Alignment.End,
            ) {
                var mDisplayMenu by remember { mutableStateOf(false) }

                Box {
                    IconButton(onClick = { mDisplayMenu = !mDisplayMenu }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More options",
                            tint = SecondaryText
                        )
                    }

                    DropdownMenu(
                        expanded = mDisplayMenu,
                        onDismissRequest = { mDisplayMenu = false },
                        modifier = Modifier.background(
                            color = Color.White,
                            shape = RoundedCornerShape(8.dp)
                        )
                    ) {
                        DropdownMenuItem(text = { Text(text = "Block") }, onClick = {
                            mDisplayMenu = false
                            viewModel.blockUser(
                                BlockUnblockRequestBody(
                                    "block",
                                    call.creator ?: ""
                                )
                            )
                        }, leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Block, contentDescription = "Block"
                            )
                        })

                        Divider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = Color.LightGray
                        )

                        DropdownMenuItem(text = { Text(text = "Report") }, onClick = {
                            mDisplayMenu = false
                            viewModel.reportUser(
                                ReportRequestBody(
                                    call._id,
                                    call.creator ?: "",
                                    call.members.firstOrNull()?.custom?.name ?: "",
                                    "call me dekt",
                                    submittedBy = SubmittedBy(
                                        userId = call.members.firstOrNull()?.user_id ?: "" ,
                                        userType = "creator",
                                    )
                                )
                            )
                        }, leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Report,
                                contentDescription = "Report"
                            )
                        })
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = formatDate(call.startedAt),
                    color = SecondaryText,
                    fontFamily = helveticaFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                )
            }
        }
    }
}


fun formatDuration(duration: String): String {
    val durationInSeconds = duration.toDoubleOrNull() ?: return ""
    val minutes = (durationInSeconds / 60).toInt()
    val seconds = (durationInSeconds % 60).toInt()
    return when {
        minutes > 0 -> "${minutes}m ${seconds}s"
        else -> "${seconds}s"
    }
}

fun formatDate(dateString: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val outputFormat = SimpleDateFormat("EEE, MMM d, h:mm a", Locale.getDefault())
    inputFormat.timeZone = TimeZone.getTimeZone("UTC")
    outputFormat.timeZone = TimeZone.getDefault()

    return try {
        val date = inputFormat.parse(dateString)
        outputFormat.format(date)
    } catch (e: Exception) {
        "Invalid date"
    }
}

@Preview(showSystemUi = true)
@Composable
fun OrderHistoryCardPreview() {

}