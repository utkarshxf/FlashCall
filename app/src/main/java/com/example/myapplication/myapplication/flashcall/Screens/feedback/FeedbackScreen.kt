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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.myapplication.flashcall.R
import com.example.myapplication.myapplication.flashcall.ViewModel.feedback.FeedbackViewModel
import com.example.myapplication.myapplication.flashcall.ui.theme.BorderColor2
import com.example.myapplication.myapplication.flashcall.ui.theme.MainColor
import com.example.myapplication.myapplication.flashcall.ui.theme.ProfileBackground
import com.example.myapplication.myapplication.flashcall.ui.theme.RatingColor
import com.example.myapplication.myapplication.flashcall.ui.theme.SwitchColor
import com.example.myapplication.myapplication.flashcall.ui.theme.arimoFontFamily

@Composable
fun FeedbackScreen(
    feedbackViewModel: FeedbackViewModel = hiltViewModel(),
    navController: NavController
){

    LaunchedEffect(
        Unit
    ) {
        feedbackViewModel.getFeedbacks()
    }

    Surface(
        modifier = Modifier.wrapContentSize(),
        color = ProfileBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
        ) {

            Box(
                modifier = Modifier.fillMaxWidth()
            ){
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Back", tint = Color.Black, modifier = Modifier.size(28.dp))
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = "User's Feedbacks",
                    style = TextStyle(
                        fontFamily = arimoFontFamily,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
            

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    FeedbackListUtil()
                }

                item {
                    FeedbackListUtil()
                }

                item {
                    FeedbackListUtil()
                }

                item {
                    FeedbackListUtil()
                }

                item {
                    FeedbackListUtil()
                }

                item {
                    FeedbackListUtil()
                }

                item {
                    FeedbackListUtil()
                }
            }

        }
    }
}


@Composable
fun FeedbackListUtil(){

    var isChecked by remember {
        mutableStateOf(false)
    }

    var isExpanded by remember {
        mutableStateOf(false)
    }


    Card(
        onClick = { /*TODO*/ },
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(1.dp, Color.Black, shape = RoundedCornerShape(12.dp))
            .background(Color.White)
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.wrapContentSize()
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp),
            ){

                Icon(
                    painter = painterResource(id = R.drawable.reorder_icon),
                    contentDescription ="reorder")

                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    
                    Text(text = "+91 7008150349",
                        style = TextStyle(
                            fontFamily = arimoFontFamily,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    RatingBar(rating = 4)
                    
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Switch(
                        checked = isChecked,
                        onCheckedChange = {
                            isChecked = it
                        },
                        modifier = Modifier.align(Alignment.End),
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = MainColor,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color.Gray,
                        )
                    )
                }

            }

            HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(start = 16.dp, end = 16.dp))

            Spacer(modifier = Modifier.size(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 16.dp, end = 16.dp)
                    .clickable {
                        isExpanded = !isExpanded
                    }
            ){
                if(isExpanded == false) {
                    Row(
                        modifier = Modifier.weight(0.5f),
                    ) {
                        Text(
                            text = "The architecture firm Cadence, led by the vision\u2028husband-and-wife duo Angelo and Nancy Maras\u2028co, recently a transformative rebranding.",
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                        )
                    }
                    Text("View More")
                } else {

                    Column(
                        modifier = Modifier.wrapContentHeight(),
                    ){

                        Text(
                            text = "The architecture firm Cadence, led by the vision\u2028husband-and-wife duo Angelo and Nancy Maras\u2028co, recently a transformative rebranding.",
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "Video Call"
                        )
                        
                        Spacer(modifier = Modifier.height(10.dp))
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {

                            Text(text = "+91 7008150349")
                            
                            Text(text = "17/06/24, 04:22 PM")

                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    starColor : Color = RatingColor,
    stars : Int = 5,
    rating : Int = 0,
){

    Row {
        for (index in 1..stars) {
            Icon(
                imageVector =
                if (index <= rating) {
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