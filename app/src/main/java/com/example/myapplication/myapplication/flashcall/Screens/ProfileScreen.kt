package com.example.myapplication.myapplication.flashcall.Screens

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import co.hyperverge.hyperkyc.HyperKyc
import co.hyperverge.hyperkyc.data.models.HyperKycConfig
import co.hyperverge.hyperkyc.data.models.result.HyperKycResult
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.R
import com.example.myapplication.myapplication.flashcall.ViewModel.AuthenticationViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.RegistrationViewModel
//import com.example.myapplication.myapplication.flashcall.hyperKycLauncher
import com.example.myapplication.myapplication.flashcall.kyc_package.KycActivity
import com.example.myapplication.myapplication.flashcall.ui.theme.BorderColor
import com.example.myapplication.myapplication.flashcall.ui.theme.BorderColor2
import com.example.myapplication.myapplication.flashcall.ui.theme.ProfileBackground
import com.example.myapplication.myapplication.flashcall.ui.theme.SecondaryText
import com.example.myapplication.myapplication.flashcall.ui.theme.arimoFontFamily

@Composable
fun ProfileScreen(
    navController: NavController,
    hyperKycLauncher: ActivityResultLauncher<HyperKycConfig>,
    registrationViewModel: RegistrationViewModel = hiltViewModel(),
    authenticationViewModel: AuthenticationViewModel = hiltViewModel()

) {
    val isKyc = false
    val context = LocalContext.current
    val uid = registrationViewModel.getStoredUserData("_id")
    val userData = authenticationViewModel.getUserFromPreferences(context)

    // Use the Elvis operator to simplify null checks and assignment
    val name = userData?.fullName ?: registrationViewModel.getStoredUserData("fullName")
    val profilePic = userData?.photo ?: registrationViewModel.getStoredUserData("photo")

    // Remember state values
    var profilePicState by remember { mutableStateOf(profilePic) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = ProfileBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    IconButton(onClick = {
//                        navController.popBackStack()
                    }) {
                        Icon(
                            Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Back",
                            tint = Color.Black,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp).padding(6.dp)  // Adjust the size as per your requirement
                        ) {
                            ImageFromUrl(imageUrl = profilePic!!)
                        }

                        Column {
                            Text(
                                text = name.toString(),
                                color = Color.Black,
                                style = TextStyle(
                                    fontFamily = arimoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp,
                                ),
                                modifier = Modifier.padding(top = 50.dp, start = 6.dp)
                            )

                            Text(
                                text = "Astrologer",
                                style = TextStyle(
                                    fontFamily = arimoFontFamily,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp,
                                ),
                                color = SecondaryText
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            HorizontalDivider(
                thickness = 10.dp,
                color = BorderColor2
            )

            Spacer(modifier = Modifier.height(30.dp))

            val config = HyperKycConfig(
                appId = "muzdob",
                appKey = "2ns9u1evoeugbrydykl7",
                workflowId = "workflow_9KW4mUl",
                transactionId = "TestTransact1"
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isKyc) 75.dp else 60.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .border(1.dp, BorderColor, RoundedCornerShape(10.dp))
                    .clickable {
                        val updatedConfig = HyperKycConfig(
                            appId = "muzdob",
                            appKey = "2ns9u1evoeugbrydykl7",
                            workflowId = "workflow_9KW4mUl",
                            transactionId = "TestTransact6"
                        )
                        hyperKycLauncher.launch(updatedConfig)
                    }
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center
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
                                    .height(26.dp)
                                    .width(26.dp)
                            )

                        }

                        Icon(
                            Icons.Default.KeyboardArrowRight,
                            contentDescription = "Next",
                            tint = Color.Black,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))


                    }
                if (isKyc) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Please complete your KYC",
                        color = Color.Red,
                        modifier = Modifier
                            .padding(start = 45.dp, top = 50.dp)
                            .fillMaxWidth()
                    )
                }
            }

            // Additional Boxes for Support, Payment Settings, etc.

            Spacer(modifier = Modifier.height(15.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .border(1.dp, BorderColor, RoundedCornerShape(10.dp))
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

                    Icon(
                        Icons.Default.KeyboardArrowRight,
                        contentDescription = "Next",
                        tint = Color.Black,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(15.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .border(1.dp, BorderColor, RoundedCornerShape(10.dp))
                    .clickable {
                        navController.navigate(ScreenRoutes.FeedbackScreen.route)
                    }
            ) {
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
                        Icons.Default.KeyboardArrowRight,
                        contentDescription = "Next",
                        tint = Color.Black,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .clickable {
                        navController.navigate(ScreenRoutes.PaymentSettings.route)
                    }
                    .border(1.dp, BorderColor, RoundedCornerShape(10.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
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

                    Icon(
                        Icons.Default.KeyboardArrowRight,
                        contentDescription = "Next",
                        tint = Color.Black,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .border(1.dp, BorderColor, RoundedCornerShape(10.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.terms_icon),
                        contentDescription = null
                    )

                    Text(
                        text = "Terms & Conditions",
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
                        Icons.Default.KeyboardArrowRight,
                        contentDescription = "Next",
                        tint = Color.Black,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            val activity = (LocalContext.current as? Activity)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .border(1.dp, BorderColor, RoundedCornerShape(10.dp))
                    .clickable {
//                        activity?.finish()
                        authenticationViewModel.deleteTokenFromPreferences()

                        // Navigate to the login screen or some other screen
                        navController.navigate(ScreenRoutes.SignUpScreen.route) {
                            popUpTo(0) { inclusive = true } // Clear the backstack
                        }
                    }
            ) {
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

                    Icon(
                        Icons.Default.KeyboardArrowRight,
                        contentDescription = "Next",
                        tint = Color.Black,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }
    }
}



//@Preview
//@Composable
//fun ProfileScreenPreview(){
//    ProfileScreen()
//}


