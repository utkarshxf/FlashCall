    package com.example.myapplication.myapplication.flashcall.Screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.composeuisuite.ohteepee.OhTeePeeInput
import com.composeuisuite.ohteepee.configuration.OhTeePeeCellConfiguration
import com.composeuisuite.ohteepee.configuration.OhTeePeeConfigurations
//import com.example.myapplication.myapplication.flashcall.Components.OtpInput
//import com.example.myapplication.myapplication.flashcall.Components.OtpView
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.Data.model.APIResponse
import com.example.myapplication.myapplication.flashcall.Screens.common.CircularLoaderButton
import com.example.myapplication.myapplication.flashcall.ViewModel.AuthenticationViewModel
import com.example.myapplication.myapplication.flashcall.ui.theme.Background
import com.example.myapplication.myapplication.flashcall.ui.theme.MainColor
import com.example.myapplication.myapplication.flashcall.ui.theme.OTPBackground
import com.example.myapplication.myapplication.flashcall.ui.theme.OTPBorder
import com.example.myapplication.myapplication.flashcall.ui.theme.PrimaryText
import com.example.myapplication.myapplication.flashcall.ui.theme.SecondaryText
import com.example.myapplication.myapplication.flashcall.ui.theme.arimoFontFamily

    var resendToken : String? = null
var verificationToken : String? = null
@Composable
fun SignUpOTP(navController: NavController, viewModel: AuthenticationViewModel) {
    var isKeyboardOpen by remember { mutableStateOf(false) }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Background
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            if (!isKeyboardOpen) {
                // Only show image slider and texts when the keyboard is closed
                ImageSlider()
                Spacer(modifier = Modifier.height(20.dp))
                TitleText()
                Spacer(modifier = Modifier.height(15.dp))
                SubTitleText()
                Spacer(modifier = Modifier.height(30.dp))
            }
            if(isKeyboardOpen){
                Spacer(modifier = Modifier.height(50.dp))
                TitleText()
                Spacer(modifier = Modifier.height(15.dp))
                SubTitleText()
                Spacer(modifier = Modifier.height(30.dp))
            }
            BottomOTPBar(navController,viewModel, isKeyboardOpen, onKeyboardToggle = {
                isKeyboardOpen = it
            })


        }
    }

}

    @Composable
    fun BottomOTPBar(navController: NavController, viewModel: AuthenticationViewModel,isKeyboardOpen: Boolean,
                     onKeyboardToggle: (Boolean) -> Unit) {
        var otpValue by remember { mutableStateOf("") }
        var phone = viewModel.phoneNumber.value
        var loading by remember {
            mutableStateOf(false)
        }

        var loadingResend by remember { mutableStateOf(false) }
        val resendOTPState by viewModel.resendOTPState.collectAsState()
        val sendOTPState by viewModel.sendOTPState.collectAsState()
        val verifyOTPState by viewModel.verifyOTPState.collectAsState()

        val context = LocalContext.current
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    // Detect taps anywhere on the screen
                    detectTapGestures {
                        // Clear focus when tapping outside the input field
                        focusManager.clearFocus()
                    }
                },
            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
            color = Color.White
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Color.White,
                        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                    ),
                contentAlignment = Alignment.BottomCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Enter verification code",
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = Color.Black,
                        style = TextStyle(
                            fontFamily = arimoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center) {
                        Text(
                            text = "We sent a 6-digit code to $phone.",
                            color = PrimaryText,
                            style = TextStyle(
                                fontFamily = arimoFontFamily,
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp
                            )
                        )
                        Text(
                            text = "edit number",
                            modifier = Modifier.clickable {
                                navController.navigate(ScreenRoutes.SignUpScreen.route)
                            },
                            color = MainColor,
                            style = TextStyle(
                                fontFamily = arimoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                textDecoration = TextDecoration.Underline
                            )
                        )
                    }

                    when (resendOTPState) {
                        is APIResponse.Success -> {
                            val response = (resendOTPState as APIResponse.Success).data
                            resendToken = response.token
                        }
                        APIResponse.Empty -> Log.e("ERROR", "ERROR CODE")
                        is APIResponse.Error -> {
                            Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                        }
                        APIResponse.Loading -> Log.e("ERROR", "ERROR CODE")
                    }

                    when (sendOTPState) {
                        is APIResponse.Success -> {
                            val sendData = (sendOTPState as APIResponse.Success).data
                            verificationToken = sendData.token
                        }
                        APIResponse.Empty -> Log.e("ERROR", "ERROR CODE")
                        is APIResponse.Error -> {
                            Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                        }
                        APIResponse.Loading -> Log.e("ERROR", "ERROR CODE")
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Conditional UI based on OTP verification state
//                    when (verifyOTPState) {
//                        is APIResponse.Success -> {
//
//                        }
//                        is APIResponse.Error -> {
//                            // Display error message and modify OTP input field configuration
//                            Text(
//                                text = "This code is invalid. Please check the code & try again",
//                                color = Color.Red,
//                                fontSize = 14.sp,
//                                modifier = Modifier.padding(start = 10.dp)
//                            )
//                            Spacer(modifier = Modifier.height(20.dp))
//                        }
//                        APIResponse.Loading -> {
//                            // Optionally show a loading spinner or similar indicator
//                        }
//                        APIResponse.Empty -> Log.e("ERROR", "ERROR CODE")
//                    }
                    if (verifyOTPState is APIResponse.Error) {
                        Log.e("verifyOtp state", "is showing error")
                        Column(verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally) {


                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                val otpCellConfig = OhTeePeeCellConfiguration.withDefaults(
                                    borderColor = Color.Red,
                                    borderWidth = 1.dp,
                                    shape = RoundedCornerShape(16.dp),
                                    backgroundColor = OTPBackground,
                                    textStyle = TextStyle(
                                        color = Color.Black,
                                        fontSize = 16.sp,
                                        fontFamily = arimoFontFamily,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                OhTeePeeInput(
                                    value = otpValue,
                                    onValueChange = { newValue, isValid ->
                                        otpValue = newValue
                                        if (otpValue.length == 6 && isValid) {
                                            // Avoid multiple calls by checking that the length is exactly 6
                                            if (otpValue.length == 6) {
                                                // Automatically call verifyOTP when otpValue is exactly 6 digits
                                                keyboardController?.hide()
                                                if (resendToken != null) {
                                                    viewModel.verifyOTP(phone, otpValue, resendToken, navController){loading = it}
                                                } else {
                                                    viewModel.verifyOTP(phone, otpValue, verificationToken, navController){ loading = it}
                                                }
                                                viewModel.iCreatedUser(phone){loading = it}
                                            }
                                        }
                                    },
                                    configurations = OhTeePeeConfigurations.withDefaults(
                                        cellsCount = 6,
                                        activeCellConfig = otpCellConfig.copy(
                                            borderColor = Color.Red,
                                            borderWidth = 3.dp
                                        ),
                                        emptyCellConfig = otpCellConfig,
                                        cellModifier = Modifier
                                            .padding(horizontal = 4.dp)
                                            .width(46.dp)
                                            .height(50.dp)
                                            .focusRequester(focusRequester)
                                            .onFocusChanged {
                                                onKeyboardToggle(it.isFocused)
                                            },
                                        elevation = 4.dp
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = "This Code is invalid. Please check the code & try again", color = Color.Red, fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                    else{
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            val otpCellConfig = OhTeePeeCellConfiguration.withDefaults(
                                borderColor = OTPBorder,
                                borderWidth = 1.dp,
                                shape = RoundedCornerShape(16.dp),
                                backgroundColor = OTPBackground,
                                textStyle = TextStyle(
                                    color = Color.Black,
                                    fontSize = 16.sp,
                                    fontFamily = arimoFontFamily,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            OhTeePeeInput(
                                value = otpValue,
                                onValueChange = { newValue, isValid ->
                                    otpValue = newValue
                                    if (otpValue.length == 6 && isValid) {
                                        // Avoid multiple calls by checking that the length is exactly 6
                                        keyboardController?.hide()
                                        if (otpValue.length == 6) {
                                            // Automatically call verifyOTP when otpValue is exactly 6 digits
                                            if (resendToken != null) {
                                                viewModel.verifyOTP(phone, otpValue, resendToken, navController){loading = it}
                                            } else {
                                                viewModel.verifyOTP(phone, otpValue, verificationToken, navController){loading = it}
                                            }
                                            viewModel.iCreatedUser(phone){}
                                        }
                                    }
                                },
                                configurations = OhTeePeeConfigurations.withDefaults(
                                    cellsCount = 6,
                                    activeCellConfig = otpCellConfig.copy(
                                        borderColor =  OTPBorder ,
                                        borderWidth = 3.dp
                                    ),
                                    emptyCellConfig = otpCellConfig,
                                    cellModifier = Modifier
                                        .padding(horizontal = 4.dp)
                                        .width(46.dp)
                                        .height(50.dp)
                                        .focusRequester(focusRequester)
                                        .onFocusChanged {
                                            onKeyboardToggle(it.isFocused)
                                        },
                                    elevation = 4.dp
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))

                    }

                    Text(
                        text = "Resend OTP",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .clickable {
                                viewModel.resendOTP(phone) { loadingResend = it}
                            },
                        color = PrimaryText,
                        style = TextStyle(
                            fontFamily = arimoFontFamily,
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp
                        )
                    )

                    CircularLoaderButton(
                        onClick = {
                            if (resendToken != null)
                                viewModel.verifyOTP(phone, otpValue, resendToken, navController){loading = it}
                            else
                                viewModel.verifyOTP(phone, otpValue, verificationToken, navController){loading = it}
                                viewModel.iCreatedUser(phone){loading = it}
                        },
                        modifier = Modifier
                            .align(CenterHorizontally)
                            .padding(top = 5.dp, end = 1.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MainColor,
                            contentColor = Color.White
                        ),
                        text = "Verify",
                        loading = loading,
                        enabled = !loadingResend
                    )

                    Spacer(modifier = Modifier.height(10.dp))

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
                                Text(
                                    text = "Terms of Service",
                                    modifier = Modifier.clickable { },
                                    style = TextStyle(
                                        fontFamily = arimoFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = Color.Black
                                    )
                                )

                                Text(
                                    text = " and ",
                                    modifier = Modifier.clickable { },
                                    style = TextStyle(
                                        fontFamily = arimoFontFamily,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 16.sp,
                                        color = SecondaryText
                                    )
                                )

                                Text(
                                    text = "Privacy Policy",
                                    modifier = Modifier.clickable { },
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
