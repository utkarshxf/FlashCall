package com.example.myapplication.myapplication.flashcall.Screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role.Companion.Button
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

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Background
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            ImageSlider()
            Spacer(modifier = Modifier.height(20.dp))
            TitleText()
            Spacer(modifier = Modifier.height(15.dp))
            SubTitleText()
            Spacer(modifier = Modifier.height(30.dp))
            BottomOTPBar(navController,viewModel)


        }
    }

}

@Composable
fun BottomOTPBar(navController: NavController,viewModel: AuthenticationViewModel){

//    var otp by remember { mutableStateOf("") }

    var otpValue : String by remember {
        mutableStateOf("")
    }

    var phone = viewModel.phoneNumber.value



    val resendOTPState by viewModel.resendOTPState.collectAsState()
    val sendOTPState by viewModel.sendOTPState.collectAsState()
    val verifyOTPState by viewModel.verifyOTPState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        color = Color.White
    ){

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color.White,
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                ),
            contentAlignment = Alignment.BottomCenter
        ){


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ){


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
                    horizontalArrangement = Arrangement.Center)
                {
                    Text(
                        text = "We sent a 6-digit code to $phone .",
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

                var context = LocalContext.current

                when(resendOTPState){
                    is APIResponse.Success->{
                        val response = (resendOTPState as APIResponse.Success).data
                        resendToken = response.token
                    }

                    APIResponse.Empty -> Log.e("ERROR","ERROR CODE")
                    is APIResponse.Error -> {
                        Toast.makeText(context,"Something Went Wrong", Toast.LENGTH_SHORT).show()
                    }
                    APIResponse.Loading -> Log.e("ERROR","ERROR CODE")
                }

                when(sendOTPState){
                    is APIResponse.Success->{
                        val sendData = (sendOTPState as APIResponse.Success).data
                        verificationToken = sendData.token
                    }

                    APIResponse.Empty -> Log.e("ERROR","ERROR CODE")
                    is APIResponse.Error -> {
                        Toast.makeText(context,"Something Went Wrong", Toast.LENGTH_SHORT).show()
                    }
                    APIResponse.Loading -> Log.e("ERROR","ERROR CODE")
                }

                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {


                    val defaultCellConfig = OhTeePeeCellConfiguration.withDefaults(
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
                        onValueChange = {
                                newValue, isValid->
                            otpValue = newValue

//                            if(otpValue.length == 6)
//                                viewModel.verifyOTP(phone,otpValue,verificationToken,navController)
//                            when(otpValue.length){
//
//                                6-> viewModel.verifyOTP(phone,otpValue,verificationToken,navController)
//                            }

                        },
                        configurations = OhTeePeeConfigurations.withDefaults(
                            cellsCount = 6,
                            activeCellConfig = defaultCellConfig.copy(
                                borderColor = OTPBorder,
                                borderWidth = 3.dp
                            ),
                            emptyCellConfig = defaultCellConfig,
                            cellModifier = Modifier
                                .padding(horizontal = 4.dp)
                                .width(46.dp)
                                .height(50.dp),
                            elevation = 4.dp
                        ),
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Resend OTP",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                                   viewModel.resendOTP(phone)
                        },
                    color = PrimaryText,
                    style = TextStyle(
                        fontFamily = arimoFontFamily,
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp
                    )
                )

                Button(
                    onClick = {

                        if(resendToken != null)
                            viewModel.verifyOTP(phone,otpValue,resendToken,navController)
                        else
                            viewModel.verifyOTP(phone,otpValue,verificationToken,navController)
                              },
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .padding(top = 5.dp, end = 1.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MainColor,
                        contentColor = Color.White
                    )
                )
                {
                    Text(
                        text = "Verify",
                        style = TextStyle(
                            fontSize = 10.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

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