package com.example.myapplication.myapplication.flashcall.Screens

//import com.example.myapplication.myapplication.flashcall.hyperKycLauncher
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.multidex.BuildConfig
import androidx.navigation.NavController
import co.hyperverge.hyperkyc.data.models.HyperKycConfig
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.R
import com.example.myapplication.myapplication.flashcall.ViewModel.AuthenticationViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.RegistrationViewModel
import com.example.myapplication.myapplication.flashcall.ui.theme.BorderColor
import com.example.myapplication.myapplication.flashcall.ui.theme.BorderColor2
import com.example.myapplication.myapplication.flashcall.ui.theme.MainColor
import com.example.myapplication.myapplication.flashcall.ui.theme.ProfileBackground
import com.example.myapplication.myapplication.flashcall.ui.theme.SecondaryText
import com.example.myapplication.myapplication.flashcall.ui.theme.arimoFontFamily
import com.example.myapplication.myapplication.flashcall.utils.PreferencesKey
import com.example.myapplication.myapplication.flashcall.utils.getAppVersionName

@Composable
fun ProfileScreen(
    navController: NavController,
    hyperKycLauncher: ActivityResultLauncher<HyperKycConfig>,
    registrationViewModel: RegistrationViewModel = hiltViewModel(),
    authenticationViewModel: AuthenticationViewModel = hiltViewModel()

) {
    var isKyc by remember {
        mutableStateOf(registrationViewModel.getIsKycRequired())
    }
    var isPaymentDetails by remember {
        mutableStateOf(registrationViewModel.getIsPaymentDetails())
    }
    val context = LocalContext.current
    val uid = registrationViewModel.getStoredUserData("_id")
    val userData = authenticationViewModel.getUserFromPreferences(context)

    // Use the Elvis operator to simplify null checks and assignment
    val name = userData?.fullName ?: registrationViewModel.getStoredUserData("fullName")
    val profilePic = userData?.photo ?: registrationViewModel.getStoredUserData("photo")
    val profession = userData?.profession ?: registrationViewModel.getStoredUserData(PreferencesKey.Profession.key)

    // Remember state values
    var profilePicState by remember { mutableStateOf(profilePic) }

    Surface(
        modifier = Modifier.fillMaxSize(), color = ProfileBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
//                                .padding(6.dp)
                        ) {
                            ImageFromUrl(imageUrl = profilePic!!)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier.padding(start = 6.dp)
                        ) {
                            Text(
                                text = name.toString(), color = Color.Black, style = TextStyle(
                                    fontFamily = arimoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp,
                                )
                            )

                            Text(
                                text = profession+"", style = TextStyle(
                                    fontFamily = arimoFontFamily,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp,
                                ), color = SecondaryText
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            HorizontalDivider(
                thickness = 1.dp, color = BorderColor2
            )

            Spacer(modifier = Modifier.height(30.dp))

            val config = HyperKycConfig(
                appId = "muzdob",
                appKey = "2ns9u1evoeugbrydykl7",
                workflowId = "workflow_9KW4mUl",
                transactionId = "TestTransact1"
            )

            Box(modifier = Modifier
                .fillMaxWidth()
                .height(if (isKyc) 75.dp else 60.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .border(1.dp, BorderColor, RoundedCornerShape(10.dp))
                .clickable {
                    navController.navigate(ScreenRoutes.KycScreen.route)
                }) {

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.kyc_icon),
                            contentDescription = null
                        )

                        Text(
                            text = "KYC",
                            modifier = Modifier.padding(start = 10.dp),
                            color = Color.Black,
                            style = TextStyle(
                                fontFamily = arimoFontFamily,
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp
                            )
                        )

                        Spacer(modifier = Modifier.weight(1f))
                        if (isKyc) {
                            Image(
                                painter = painterResource(id = R.drawable.exclamation1),
                                contentDescription = null,
                                modifier = Modifier
                                    .height(24.dp)
                                    .width(24.dp)
                            )
                            Icon(
                                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "Next",
                                tint = Color.Black,
                                modifier = Modifier.size(36.dp)
                            )

                        }else{
                            Image(
                                painter = painterResource(id = R.drawable.baseline_verified_24),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(MainColor),
                                modifier = Modifier
                                    .height(24.dp)
                                    .width(24.dp)
                            )
                        }

                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }
                if (isKyc) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Please complete your KYC",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .padding(start = 45.dp, top = 50.dp)
                            .fillMaxWidth()

                    )
                }
            }

            // Additional Boxes for Support, Payment Settings, etc.
            Spacer(modifier = Modifier.height(15.dp))

            Box(modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .border(1.dp, BorderColor, RoundedCornerShape(10.dp))
                .clickable {
                    navController.navigate(ScreenRoutes.FeedbackScreen.route)
                }) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.id_icon_register),
                        contentDescription = null
                    )

                    Text(
                        text = "User's Feedback",
                        color = Color.Black,
                        modifier = Modifier.padding(start = 10.dp),
                        style = TextStyle(
                            fontFamily = arimoFontFamily,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp
                        )
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Next",
                        tint = Color.Black,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            Box(modifier = Modifier
                .fillMaxWidth()
                .height(if (isPaymentDetails) 75.dp else 60.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .clickable {
                    navController.navigate(ScreenRoutes.PaymentSettings.route)
                }
                .border(1.dp, BorderColor, RoundedCornerShape(10.dp))) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.payment_icon),
                            contentDescription = null
                        )

                        Text(
                            text = "Payment Settings",
                            color = Color.Black,
                            modifier = Modifier.padding(start = 10.dp),
                            style = TextStyle(
                                fontFamily = arimoFontFamily,
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp
                            )
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        if(isPaymentDetails){
                            Image(
                                painter = painterResource(id = R.drawable.exclamation1),
                                contentDescription = null,
                                modifier = Modifier
                                    .height(24.dp)
                                    .width(24.dp)
                            )
                            Icon(
                                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "Next",
                                tint = Color.Black,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        else{
                            Image(
                                painter = painterResource(id = R.drawable.baseline_verified_24),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(MainColor),
                                modifier = Modifier
                                    .height(24.dp)
                                    .width(24.dp)
                            )
                        }


                    }

                }
                if (isPaymentDetails) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Please complete your payment details",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .padding(start = 45.dp, top = 50.dp)
                            .fillMaxWidth()

                    )
                }

            }

            Spacer(modifier = Modifier.height(15.dp))


            // Logout And Support
            var isLogout by remember {
                mutableStateOf(false)
            }
            Row{
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White)
                        .border(1.dp, BorderColor, RoundedCornerShape(10.dp))
                        .weight(1f)
                        .clickable {
                            navController.navigate(ScreenRoutes.Support.route)
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.support_icon),
                            contentDescription = null
                        )

                        Text(
                            text = "Support",
                            modifier = Modifier.padding(start = 10.dp),
                            color = Color.Black,
                            style = TextStyle(
                                fontFamily = arimoFontFamily,
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp
                            )
                        )

                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                
                Spacer(modifier = Modifier.width(10.dp))

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .border(1.dp, BorderColor, RoundedCornerShape(10.dp))
                    .weight(1f)
                    .clickable {
                        isLogout = !isLogout
                    }) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logout_icon),
                            contentDescription = null
                        )

                        Text(
                            text = "Log Out",
                            color = Color.Black,
                            modifier = Modifier.padding(start = 10.dp),
                            style = TextStyle(
                                fontFamily = arimoFontFamily,
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp
                            )
                        )

                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }


            Spacer(modifier = Modifier.height(15.dp))




            //Logout Confirmation Dialog
            if(isLogout){
                LogoutConfirmationDialog(cancel = {
                    isLogout = !isLogout
                }, confirm = {
                    authenticationViewModel.deleteTokenFromPreferences()
                    navController.navigate(ScreenRoutes.SignUpScreen.route) {
                        popUpTo(0) { inclusive = true } // Clear the backstack
                    }
                    isLogout = !isLogout
                })
            }

            AppVersionAndPrivacyPolicy(navController)

        }
    }
}

@Composable
fun LogoutConfirmationDialog(confirm: () -> Unit, cancel: () -> Unit) {
    AlertDialog(
        onDismissRequest = { cancel() }, // Dismiss the dialog on outside touch or back press
        title = {
            Text(text = "Logout Confirmation")
        },
        text = {
            Text("Are you sure you want to log out?")
        },
        confirmButton = {
            Button(
                onClick = {
                    confirm()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(
                onClick = { cancel()}
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun AppVersionAndPrivacyPolicy(navController: NavController) {
    val context = LocalContext.current
    val versionCode = getAppVersionName(context)
    Box(modifier = Modifier.fillMaxWidth()) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column {
                Text(
                    text = "",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                )
                Text(
                    text = "version $versionCode",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                )
                Text(
                    text = "Terms & Conditions",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            navController.navigate(ScreenRoutes.TermAndCondition.route)
                        }, style = TextStyle(
                        textDecoration = TextDecoration.Underline,
                        color = MainColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                )
            }
        }
    }
}



//@Preview
//@Composable
//fun ProfileScreenPreview(){
//    ProfileScreen()
//}


