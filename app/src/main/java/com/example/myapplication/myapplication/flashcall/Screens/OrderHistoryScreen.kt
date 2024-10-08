package com.example.myapplication.myapplication.flashcall.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
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
import co.hyperverge.hyperkyc.data.models.HyperKycConfig
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.R
import com.example.myapplication.myapplication.flashcall.ViewModel.OrderHistoryViewModel
import com.example.myapplication.myapplication.flashcall.ui.theme.BorderColor
import com.example.myapplication.myapplication.flashcall.ui.theme.BorderColor2
import com.example.myapplication.myapplication.flashcall.ui.theme.MainColor
import com.example.myapplication.myapplication.flashcall.ui.theme.PrimaryBackGround
import com.example.myapplication.myapplication.flashcall.ui.theme.PrimaryText
import com.example.myapplication.myapplication.flashcall.ui.theme.ProfileBackground
import com.example.myapplication.myapplication.flashcall.ui.theme.SecondaryText
import com.example.myapplication.myapplication.flashcall.ui.theme.helveticaFontFamily


@Composable
fun OrderHistoryScreen(navController: NavController, viewModel: OrderHistoryViewModel = hiltViewModel()) {


    LaunchedEffect(key1 = Unit) {
        viewModel.getOrderHistory()
    }


    Surface(
        modifier = Modifier.fillMaxWidth(), color = PrimaryBackGround
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(modifier = Modifier.height(35.dp))

            Icon(
                Icons.Default.KeyboardArrowLeft,
                contentDescription = "Back",
                tint = Color.Black,
                modifier = Modifier
                    .size(28.dp)
                    .clickable {
                        navController.popBackStack()
                    }
            )
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Order History", color = Color.Black, style = TextStyle(
                    fontFamily = helveticaFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            var isToggle by remember {
                mutableStateOf(false)
            }

            Row {
                Button(
                    modifier = Modifier,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!isToggle) MainColor else SecondaryText,
                        contentColor = Color.White
                    ),
                    onClick = {
                        isToggle = !isToggle
                    }
                ) {
                    Text(
                        text = "Calls",
                        style = TextStyle(
                            fontFamily = helveticaFontFamily,
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                        ),
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Button(
                    modifier = Modifier,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isToggle) MainColor else SecondaryText,
                        contentColor = Color.White
                    ),
                    onClick = {
                        isToggle = !isToggle
                    }
                ) {
                    Text(
                        text = "Chats",
                        style = TextStyle(
                            fontFamily = helveticaFontFamily,
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                        ),
                        maxLines = 1
                    )
                }


            }

            Spacer(modifier = Modifier.height(20.dp))


            Column {
                Text(text = "hello")
                Text(text = "hello")
            }

            LazyColumn(modifier = Modifier.height(500.dp)) {
                items(10) { item ->
                    CardView()
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }


        }
    }


}

@Composable
fun CardView() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(shape = RoundedCornerShape(12.dp))
            .background(color = PrimaryBackGround)
    ) {


        Row {


            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data("").crossfade(true)
                    .build(),
                contentDescription = null,
                placeholder = painterResource(id = R.drawable.profile_picture_holder),
                modifier = Modifier
                    .size(40.dp)  // Ensure a consistent size
                    .clip(CircleShape)
                    .border(1.dp, color = MainColor, shape = CircleShape),  // Clip to a circle
                contentScale = ContentScale.Crop  // Crop the image to fit within the circle
            )

            Column{
                Text(
                    text = "User Name",
                    color = Color.Black,
                    fontFamily = helveticaFontFamily,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = "Designer",
                    color = Color.Black,
                    fontFamily = helveticaFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp
                )


                Row {
                    Text(
                        text = "Completed ",
                        color = MainColor,
                        fontFamily = helveticaFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.sp
                    )


                    Text(
                        text = "13s  Rs. 6",
                        color = PrimaryText,
                        fontFamily = helveticaFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.sp
                    )


                }

            }

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .weight(1f))

            Column(horizontalAlignment = Alignment.End,
                modifier = Modifier.padding(end = 10.dp)) {

                var mDisplayMenu by remember {
                    mutableStateOf(false)
                }

                Box{

                    Image(
                        painter = painterResource(id = R.drawable.baseline_more_vert_24),
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            mDisplayMenu = !mDisplayMenu
                        }
                    )

                    DropdownMenu(
                        expanded = mDisplayMenu,
                        onDismissRequest = { mDisplayMenu = false },
                        modifier = Modifier
                            .background(color = Color.White, shape = RoundedCornerShape(10.dp))

                    ) {

                        DropdownMenuItem(text = {
                            Text(text = "Block")
                        },
                            onClick = {
                                mDisplayMenu = !mDisplayMenu

                            }, leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.edit_24dp__2),
                                    contentDescription = ""
                                )
                            }
                        )

                        Divider(modifier = Modifier.padding(horizontal = 10.dp), color = Color.Gray)

                        DropdownMenuItem(text = {
                            Text(text = "Delete")
                        }, onClick = {
                            mDisplayMenu = !mDisplayMenu

                        }, leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.delete_24dp_2),
                                contentDescription = ""
                            )
                        }
                        )
                    }

                }

                Spacer(modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f))

                Text(
                    text = "Tue, Oct, 4:10 AM",
                    color = SecondaryText,
                    fontFamily = helveticaFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 10.dp)

                )


            }
        }

    }

}

@Preview(showSystemUi = true)
@Composable
fun OrderHistoryCardPreview() {
    CardView()
}