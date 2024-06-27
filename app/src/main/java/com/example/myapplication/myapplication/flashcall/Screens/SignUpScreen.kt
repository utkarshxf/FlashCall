package com.example.myapplication.myapplication.flashcall.Screens

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.FlowColumnScopeInstance.align
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults.color
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.myapplication.flashcall.R
import com.example.myapplication.myapplication.flashcall.ViewModel.AuthenticationViewModel
import com.example.myapplication.myapplication.flashcall.ui.theme.Background
import com.example.myapplication.myapplication.flashcall.ui.theme.BorderColor
import com.example.myapplication.myapplication.flashcall.ui.theme.MainColor
import com.example.myapplication.myapplication.flashcall.ui.theme.PrimaryBackGround
import com.example.myapplication.myapplication.flashcall.ui.theme.PrimaryText
//import com.example.myapplication.myapplication.flashcall.ui.theme.SecondaryColor
import com.example.myapplication.myapplication.flashcall.ui.theme.SecondaryText
import com.example.myapplication.myapplication.flashcall.ui.theme.arimoFontFamily
import kotlinx.coroutines.delay
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.Data.model.APIResponse

var sendtoken : String? = ""
@Composable
fun SignUpScreen(navController: NavController,viewModel: AuthenticationViewModel) {

    val images = listOf(
        R.drawable.home_slider_image1,
        R.drawable.home_slider_image2,
        R.drawable.home_slider_image3,
    )



    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
        color = Background,
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            ImageSlider()
            Spacer(modifier = Modifier.height(20.dp))
            TitleText()
            Spacer(modifier = Modifier.height(15.dp))
            SubTitleText()
            Spacer(modifier = Modifier.height(30.dp))
            BottomSignUpBar(navController, viewModel)

        }
    }
    
}

@Composable
fun TitleText()
{
    Text(text = "Title 1: Catchy Line to Make user Fool",
        color = Color.White,
        style = TextStyle(
            fontFamily = arimoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    )

}

@Composable
fun SubTitleText()
{
    Text(text = "Sub Title 1:To support the above line",
        color = Color.Gray,
        style = TextStyle(
            fontFamily = arimoFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        )
    )
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageSlider(modifier: Modifier = Modifier) {

    val images = listOf(
        R.drawable.home_slider_image1,
        R.drawable.home_slider_image2,
        R.drawable.home_slider_image3,
    )

   val pagerState = rememberPagerState(pageCount = { images.size })

    LaunchedEffect(Unit) {

        while(true)
        {
            delay(3000)
            val nextPager = (pagerState.currentPage + 1) % pagerState.pageCount
            pagerState.scrollToPage(nextPager)
        }

    }

    Column(modifier = modifier.fillMaxWidth()) {
        Box(modifier = modifier
            .padding(top = 70.dp)
            .wrapContentSize()
            .align(Alignment.CenterHorizontally)
            .width(200.dp)
            .height(200.dp))
        {

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .wrapContentSize()

            ) { currentPage ->
                Card(
                    modifier
                        .wrapContentSize(),
                    elevation = CardDefaults.cardElevation(5.dp)
                ) {
                    Image(
                        painter = painterResource(id = images[currentPage]),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        PageIndicator(
            pageCount = images.size,
            currentPage = pagerState.currentPage,
            modifier = Modifier
        )
    }

}

//fun ImageSlider(modifier: Modifier = Modifier) {
//
//    val images = listOf(
//        R.drawable.home_slider_image1,
//        R.drawable.home_slider_image2,
//        R.drawable.home_slider_image3,
//    )
//
//    val pagerState = rememberPagerState(pageCount = { images.size })
//
//    LaunchedEffect(Unit) {
//        while (true) {
//            delay(3000)
//            val nextPager = (pagerState.currentPage + 1) % pagerState.pageCount
//            pagerState.scrollToPage(nextPager)
//        }
//    }
//
//    Column(
//        modifier = modifier
//            .wrapContentSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Box(modifier = Modifier.weight(1f).wrapContentSize()) {
//            HorizontalPager(
//                state = pagerState,
//            ) { currentPage ->
//                Card(
//                    modifier = Modifier
//                        .wrapContentSize()
//                        .width(200.dp)
//                        .height(200.dp)
//                        .padding(horizontal = 16.dp),
//                    elevation = CardDefaults.cardElevation(5.dp)
//                ) {
//                    Image(
//                        painter = painterResource(id = images[currentPage]),
//                        contentDescription = null,
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier.fillMaxSize()
//                    )
//                }
//            }
//        }
//
//        PageIndicator(
//            pageCount = images.size,
//            currentPage = pagerState.currentPage,
//            modifier = Modifier.padding(vertical = 8.dp)
//        )
//    }
//}


@Composable
fun PageIndicator(pageCount: Int, currentPage: Int, modifier:  Modifier)
{
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ) {
        Row(
            modifier = modifier.align(Alignment.Center),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            repeat(pageCount){
                IndicatorDots(isSelected = it == currentPage, modifier= modifier)
            }
        }
    }


}

@Composable
fun IndicatorDots(isSelected: Boolean, modifier: Modifier) {
    val size = animateDpAsState(targetValue = if (isSelected) 12.dp else 10.dp, label = "")
    Box(modifier = modifier
        .padding(2.dp)
        .size(size.value)
        .clip(CircleShape)
        .background(if (isSelected) Color(0xff373737) else Color(0xA8373737))
    )
}

@Composable
fun BottomSignUpBar(navController: NavController, viewModel: AuthenticationViewModel) {

    val context = LocalContext.current
    var phoneNumber by remember {
        mutableStateOf("")
    }

    val sendOTPState by viewModel.sendOTPState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color.White,
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                ),
            contentAlignment = Alignment.BottomCenter,
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Login or SignUp",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = Color.Black,
                    style = TextStyle(
                        fontFamily = arimoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                )

                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Get Start with your first consultation",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = PrimaryText,
                    style = TextStyle(
                        fontFamily = arimoFontFamily,
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp
                    )
                )
                Text(
                    text = "and start earning",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = PrimaryText,
                    style = TextStyle(
                        fontFamily = arimoFontFamily,
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize()
                        .height(60.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(shape = RoundedCornerShape(16.dp))
                            .background(PrimaryBackGround, shape = RoundedCornerShape(16.dp))
                            .border(BorderStroke(1.dp, SolidColor(BorderColor)))
                    ) {

                        Text(
                            text = "+91",
                            modifier = Modifier.padding(start = 5.dp, top = 20.dp, bottom = 10.dp),
                            color = SecondaryText,
                            style = TextStyle(
                                fontFamily = arimoFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 20.sp
                            )
                        )

                        Text(
                            text = "|",
                            modifier = Modifier.padding(start = 10.dp, top = 5.dp),
                            color = SecondaryText,
                            style = TextStyle(
                                fontFamily = arimoFontFamily,
                                fontWeight = FontWeight.Light,
                                fontSize = 40.sp
                            )
                        )

                        TextField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            modifier = Modifier
                                .padding(top = 7.dp)
                                .fillMaxHeight()
                                .width(170.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            textStyle = TextStyle(
                                fontFamily = arimoFontFamily,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 20.sp,
                                color = Color.Black
                            ),
                            placeholder = {
                                Text(
                                    "Enter your number",
                                    color = SecondaryText,
                                    style = TextStyle(
                                        fontFamily = arimoFontFamily,
                                        fontWeight = FontWeight.Bold,
                                    )
                                )
                            }
                        )

                        when(sendOTPState){
                            is APIResponse.Success->{
                                val response = (sendOTPState as APIResponse.Success).data
                                 sendtoken = response.token
                            }

                            APIResponse.Empty -> Log.e("ERROR","ERROR CODE")
                            is APIResponse.Error -> {
                                Toast.makeText(context,"Something Went Wrong", Toast.LENGTH_SHORT).show()
                            }
                            APIResponse.Loading -> Log.e("ERROR","ERROR CODE")
                        }

                        Box(contentAlignment = Alignment.CenterEnd) {
                            Button(
                                onClick = {
                                    viewModel.signUP(
                                        phoneNumber = phoneNumber,navController, sendtoken
                                    )
                                },
                                modifier = Modifier
                                    .padding(top = 5.dp, end = 1.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MainColor,
                                    contentColor = Color.White
                                )
                            ) {
                                Text(

                                    text = "Get OTP",
                                    style = TextStyle(
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "We’ll send you a code to confirm your phone number.",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = TextStyle(
                        fontFamily = arimoFontFamily,
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp,
                        color = SecondaryText
                    )
                )

                Spacer(modifier = Modifier.height(50.dp))

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "By signing up, you agree to our",
                            style = TextStyle(
                                fontFamily = arimoFontFamily,
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp,
                                color = SecondaryText
                            )
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(text = "Terms of Service",
                                modifier = Modifier.clickable {  },
                                style = TextStyle(
                                    fontFamily = arimoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color.Black
                                )
                            )
                            Text(text = " and ",
                                modifier = Modifier.clickable {  },
                                style = TextStyle(
                                    fontFamily = arimoFontFamily,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp,
                                    color = SecondaryText
                                )
                            )

                            Text(text = "Privacy Policy",
                                modifier = Modifier.clickable {  },
                                style = TextStyle(
                                    fontFamily = arimoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color.Black
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ClickableText() {


    val termsOfServices = "Terms of Services"
    val and = " and "
    val privacyPolicy = "Privacy Policy"

    val annotatedString = buildAnnotatedString {

        withStyle(
            style = SpanStyle(
                color = Color.Black,
                fontFamily = arimoFontFamily,
                fontWeight = FontWeight.Bold
            )
        ) {
            append(termsOfServices)
        }
        append(and)
        withStyle(
            style = SpanStyle(
                color = Color.Black,
                fontFamily = arimoFontFamily,
                fontWeight = FontWeight.Bold
            )
        ) {
            append(privacyPolicy)
        }

    }


    ClickableText(text = annotatedString,
        modifier = Modifier.padding(start = 40.dp),
        onClick = { offset ->

            annotatedString.getStringAnnotations(offset, offset)
                .firstOrNull()?.also { span ->
                    Log.d("ClickableText", "{$span}")

                }
        }
    )
}






