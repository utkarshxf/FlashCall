package com.example.myapplication.myapplication.flashcall.Screens

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.FlowColumnScopeInstance.align
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
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
import kotlinx.coroutines.delay
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.Dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.Data.model.APIResponse
import com.example.myapplication.myapplication.flashcall.Screens.common.CircularLoaderButton
import com.google.accompanist.insets.LocalWindowInsets
import kotlinx.coroutines.launch
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.input.ImeAction
import com.example.myapplication.myapplication.flashcall.ui.theme.helveticaFontFamily
import kotlinx.coroutines.launch

var sendtoken: String? = ""

//var keyBoard by remember { mutableStateOf(false) }
@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(navController: NavController, viewModel: AuthenticationViewModel) {
    // Create a mutable state to track keyboard visibility
//    var isKeyboardOpen by remember { mutableStateOf(false) }
//    val insets = LocalWindowInsets.current
    val keyboardController = LocalSoftwareKeyboardController.current
//    LaunchedEffect(insets) {
//        val keyboardHeight = insets.ime.bottom
//        isKeyboardOpen = keyboardHeight > 0
//        Log.d("isKeyboard", "$isKeyboardOpen")
//    }
//    Log.d("isKeyboard", "$isKeyboardOpen")
    //Hello
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
        color = Background,
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
//            if (!isKeyboardOpen) {
            // Only show image slider and texts when the keyboard is closed
            ImageSlider()
//                Spacer(modifier = Modifier.height(20.dp))
            //TitleText()
//                Spacer(modifier = Modifier.height(15.dp))
//                SubTitleText()
//                Spacer(modifier = Modifier.height(30.dp))
//            }
//            if(isKeyboardOpen){
//                Spacer(modifier = Modifier.height(50.dp))
//                TitleText()
//                Spacer(modifier = Modifier.height(15.dp))
//                SubTitleText()
//                Spacer(modifier = Modifier.height(30.dp))
//            }



            BottomSignUpBar(navController, viewModel)

        }
    }
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

        while (true) {
            delay(3000)
            val nextPager = (pagerState.currentPage + 1) % pagerState.pageCount
            pagerState.scrollToPage(nextPager)
        }

    }

    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = modifier
//            .padding(top = 70.dp)
//            .wrapContentSize()
//            .align(Alignment.CenterHorizontally)
//            .width(200.dp)
//            .height(200.dp)
        ) {

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


@Composable
fun PageIndicator(pageCount: Int, currentPage: Int, modifier: Modifier) {
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
            repeat(pageCount) {
                IndicatorDots(isSelected = it == currentPage, modifier = modifier)
            }
        }
    }


}


@Composable
fun IndicatorDots(isSelected: Boolean, modifier: Modifier) {
    val size = animateDpAsState(targetValue = if (isSelected) 7.dp else 5.dp, label = "")
    Box(
        modifier = modifier
            .padding(2.dp)
            .size(size.value)
            .clip(CircleShape)
            .background(if (isSelected) Color(0xFFD9D9D9) else Color(0x40D9D9D9))
    )
}

@Composable
fun BottomSignUpBar(
    navController: NavController,
    viewModel: AuthenticationViewModel
) {
    var loading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var phoneNumber by remember {
        mutableStateOf("")
    }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val sendOTPState by viewModel.sendOTPState.collectAsState()

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .imePadding()
            .wrapContentHeight(),
        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
    ) {


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color.White,
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                ),
            contentAlignment = Alignment.BottomCenter,
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),

                ) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Login or SignUp",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = Color.Black,
                    style = TextStyle(
                        fontFamily = helveticaFontFamily,
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
                        fontFamily = helveticaFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    )
                )
                Text(
                    text = "and start earning",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = PrimaryText,
                    style = TextStyle(
                        fontFamily = helveticaFontFamily,
                        fontWeight = FontWeight.Normal,
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
                            .background(PrimaryBackGround, shape = RoundedCornerShape(16.dp)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "+91",
                            modifier = Modifier.padding(start = 10.dp),
                            color = Color.Black,
                            style = TextStyle(
                                fontFamily = helveticaFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 15.sp
                            )
                        )



                        VerticalDivider(
                            modifier = Modifier
                                .width(2.dp)
                                .height(20.dp)
                                .padding(start = 7.dp, end = 8.dp)
                                .background(color = SecondaryText)
                        )

                        TextField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            modifier = Modifier
                                .wrapContentHeight()
                                .fillMaxWidth()
                                .weight(1f)
                                .focusRequester(focusRequester),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            textStyle = TextStyle(
                                fontFamily = helveticaFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 15.sp,
                                color = Color.Black
                            ),
                            placeholder = {
                                Text(
                                    "Enter your number",
                                    color = SecondaryText,
                                    style = TextStyle(
                                        fontFamily = helveticaFontFamily,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 15.sp
                                    )
                                )
                            }
                        )

                        when (sendOTPState) {
                            is APIResponse.Success -> {
                                val response = (sendOTPState as APIResponse.Success).data
                                sendtoken = response.token
                            }

                            APIResponse.Empty -> Log.e("ERROR", "ERROR CODE")
                            is APIResponse.Error -> {
                                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT)
                                    .show()
                            }

                            APIResponse.Loading -> Log.e("ERROR", "ERROR CODE")
                        }

                        Box(contentAlignment = Alignment.CenterEnd) {
                            CircularLoaderButton(
                                onClick = {
                                    viewModel.signUP(
                                        phoneNumber = phoneNumber, navController, sendtoken
                                    ) { loading = it }
                                },
                                modifier = Modifier
                                    .padding(end = 10.dp)
                                    .wrapContentSize(),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MainColor,
                                    contentColor = Color.White
                                ),
                                text = "Get OTP",
                                textStyle = TextStyle(
                                    fontFamily = helveticaFontFamily,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 15.sp
                                ),
                                loading = loading,
                                enabled = phoneNumber.length == 10
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "We’ll send you a code to confirm your phone number.",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = TextStyle(
                        fontFamily = helveticaFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.sp,
                        color = SecondaryText
                    )
                )

                Spacer(modifier = Modifier.height(50.dp))

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "By signing up, you agree to our",
                            style = TextStyle(
                                fontFamily = helveticaFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                color = SecondaryText
                            )
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .padding(top = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Terms of Service",
                                modifier = Modifier.clickable {
//                                    navController.navigate(ScreenRoutes.RegistrationScreen.route)
                                },
                                style = TextStyle(
                                    fontFamily = helveticaFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )
                            )
                            Text(
                                text = "  and  ",
                                modifier = Modifier.clickable { },
                                style = TextStyle(
                                    fontFamily = helveticaFontFamily,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.sp,
                                    color = SecondaryText
                                )
                            )

                            Text(
                                text = "Privacy Policy",
                                modifier = Modifier.clickable { },
                                style = TextStyle(
                                    fontFamily = helveticaFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun SignupPreview() {
    //SignUpScreen(navController = rememberNavController(), viewModel = AuthenticationViewModel(): hiltViewModel())
    //IncomingAudioCallScreen(callerName = "Mohd Gauri", navController = rememberNavController())
}






