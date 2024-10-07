package com.example.myapplication.myapplication.flashcall.Screens.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.myapplication.flashcall.Data.model.userFeedbacks.FeedbackItem
import com.example.myapplication.myapplication.flashcall.R
import com.example.myapplication.myapplication.flashcall.ViewModel.AuthenticationViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.feedback.FeedbackViewModel
import com.example.myapplication.myapplication.flashcall.ui.theme.BorderColor
import com.example.myapplication.myapplication.flashcall.ui.theme.MainColor
import com.example.myapplication.myapplication.flashcall.ui.theme.PrimaryText
import com.example.myapplication.myapplication.flashcall.ui.theme.ProfileBackground
import com.example.myapplication.myapplication.flashcall.ui.theme.RatingColor
import com.example.myapplication.myapplication.flashcall.ui.theme.SecondaryText
import com.example.myapplication.myapplication.flashcall.ui.theme.helveticaFontFamily
import com.example.myapplication.myapplication.flashcall.utils.LoadingIndicator
import com.example.myapplication.myapplication.flashcall.utils.dragAndDrop.FeedbackDragDropList
import com.jetpack.draganddroplist.move
//import com.jetpack.draganddroplist.move
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun FeedbackScreen(
    feedbackViewModel: FeedbackViewModel = hiltViewModel(),
    authenticationViewModel: AuthenticationViewModel = hiltViewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val userData = authenticationViewModel.getUserFromPreferences(context)
    val uid = userData?._id

    // Collect the feedbacks from the ViewModel
    val feedbacks = feedbackViewModel.userFeedbackState.list

    LaunchedEffect(Unit) {
        feedbackViewModel.getFeedbacks(uid.toString())
    }

    Surface(
        modifier = Modifier.wrapContentSize(), color = ProfileBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
        ) {

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp)
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Back",
                            tint = Color.Black,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "User's Feedbacks", style = TextStyle(
                        fontFamily = helveticaFontFamily,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

//            val configuration = LocalConfiguration.current
//            val screenHeight = configuration.screenHeightDp.dp
//            val screenWidth = configuration.screenWidthDp.dp
//            Log.d("maxHeightAndWidth","height: $screenHeight, width: $screenWidth")


            if (feedbackViewModel.userFeedbackState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    LoadingIndicator()
                }
            }


            // Check if feedbacks are available
            if (feedbacks == null || feedbacks.isEmpty()) {
                Text("No feedbacks available")
            } else {
//                feedbacks?.feedbacks?.let {
//                    DragDropFeedbackList(feedbacks = it,
//                        onMove = { fromIndex, toIndex -> feedbacks.feedbacks?.move(fromIndex, toIndex)})
//                }

//                FeedbackDragDropList(
//                    items = feedbackViewModel.originalFeedbackList,
//                    onMove = { fromIndex, toIndex -> feedbackViewModel.originalFeedbackList.move(fromIndex, toIndex)},
//                    viewModel = feedbackViewModel)

//                if(feedbacks.isNotEmpty()){

                FeedbackDragDropList(
                    items = feedbackViewModel.originalFeedbackList,
                    onMove = { fromIndex, toIndex ->
                        feedbackViewModel.originalFeedbackList.move(
                            fromIndex,
                            toIndex
                        )
                    },
                    viewModel = feedbackViewModel
                )

//                    LazyColumn(
//                        modifier = Modifier.fillMaxWidth(),
//                        verticalArrangement = Arrangement.spacedBy(12.dp)
//                    ) {
//                        items(feedbacks.feedbacks!!) { feedbackResponse ->
//                            feedbackResponse.feedbacks?.forEach { feedback ->
//                                FeedbackListUtil(feedback) {
//                                    feedbackViewModel.updateFeedback(
//                                        UpdateFeedback(
//                                            clientId = feedback.clientId?.id,
//                                            createdAt = feedback.createdAt,
//                                            creatorId = feedbackResponse.creatorId,
//                                            feedbackText = feedback.feedback,
//                                            rating = feedback.rating,
//                                            showFeedback = it
//                                        )
//                                    )
//                                }
//                                Spacer(modifier = Modifier.height(12.dp))
//                            }
//                        }
//                    }
//                }

            }
        }
    }
}


@Composable
fun FeedbackListUtil(feedback: FeedbackItem, toggle: (Boolean) -> Unit = {}) {
    var isChecked by remember { mutableStateOf(feedback.showFeedback ?: false) }
    var isExpanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(1.dp, BorderColor, shape = RoundedCornerShape(12.dp))
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.wrapContentSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(10.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.reorder_icon),
                    contentDescription = "reorder",
                    modifier = Modifier.padding(top = 8.dp, end = 7.dp)
                )

                Column(modifier = Modifier
                    .weight(1f)
                    .padding(top = 8.dp)) {
                    // Display client's phone or username
                    var phone: String? = null
                    if (feedback.clientId?.phone != null) {
                        phone = feedback.clientId.phone.dropLast(5) + "XXXXX"
                    }
                    Text(
                        text = phone ?: feedback.clientId?.username
                        ?: "Unknown User", style = TextStyle(
                            fontFamily = helveticaFontFamily,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    // Display rating
                    RatingBar(rating = feedback.rating ?: 0)
                }
                Switch(
                    checked = isChecked, onCheckedChange = {
                        isChecked = it
                        toggle(it)
                    }, colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = MainColor,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color.Gray,
                    )
                )
            }

            HorizontalDivider(
                color = BorderColor,
                thickness = 1.dp,
                modifier = Modifier.padding(start = 40.dp, end = 16.dp)
            )
            Spacer(modifier = Modifier.size(10.dp))

            Row(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 40.dp, end = 16.dp, bottom = 10.dp)
                .clickable {
                    isExpanded = !isExpanded
                }) {
                if (!isExpanded) {
                    Row(
                        modifier = Modifier.weight(0.5f),
                    ) {
                        Text(
                            text = feedback.feedback ?: "No feedback provided",
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                        )
                    }
                    Text(
                        "View More",
                        color = Color.Black,
                        fontFamily = helveticaFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Column(modifier = Modifier.wrapContentHeight()) {
                        Text(
                            text = feedback.feedback ?: "No feedback provided",
                            color = PrimaryText
                        )

//                        Spacer(modifier = Modifier.height(20.dp))

                        // Display call type (e.g., "Video Call")
                        //Text(text = "Video Call", color = Color.Black, fontFamily = helveticaFontFamily, fontWeight = FontWeight.Bold, fontSize = )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            horizontalArrangement = Arrangement.End,
                        ) {
                            // Display client's phone or username
//                            var phone:String? = null
//                            if(feedback.clientId?.phone != null){
//                                phone = feedback.clientId.phone.dropLast(5)+"XXXXX"
//                            }
//                            Text(
//                                text = phone ?: feedback.clientId?.username
//                                ?: "Unknown User"
//                            )

                            // Display created date
                            Text(
                                text = convertDateFormate(feedback.createdAt),
                                color = SecondaryText,
                                fontSize = 12.sp,
                                fontFamily = helveticaFontFamily
                            )
                        }
                    }
                }
            }
        }
    }
}

fun convertDateFormate(dateTime: String?): String {
    if (!dateTime.isNullOrEmpty()) {
        // Parse the original date-time string
        val parsedDateTime = ZonedDateTime.parse(dateTime)

        // Define the desired output format
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yy, hh:mma")

        // Format the date-time into the desired format
        val formattedDateTime = parsedDateTime.format(formatter)
        return formattedDateTime
    } else {
        return "Unknown Date"
    }

}


@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    starColor: Color = RatingColor,
    stars: Int = 5,
    rating: Int = 0,
) {

    Row {
        for (index in 1..stars) {
            Icon(
                imageVector = if (index <= rating) {
                    Icons.Rounded.Star
                } else {
                    Icons.Rounded.StarOutline
                },
                contentDescription = null,
                tint = starColor,
            )
        }
    }

}
