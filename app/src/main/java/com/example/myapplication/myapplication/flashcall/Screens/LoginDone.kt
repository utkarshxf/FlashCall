package com.example.myapplication.myapplication.flashcall.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.myapplication.flashcall.BaseClass
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.R
import com.example.myapplication.myapplication.flashcall.ui.theme.helveticaFontFamily
import io.getstream.video.android.core.StreamVideo
import kotlinx.coroutines.delay

@Composable
fun LoginDoneScreen(navController: NavController) {
    val context = LocalContext.current
    (context.applicationContext as? BaseClass)?.streamBuilder(context)
    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate(ScreenRoutes.MainScreen.route) {
            popUpTo(0){inclusive = true}
        }
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(id = R.drawable.login_done), contentDescription =null
        ,             modifier = Modifier
                .size(200.dp) // You can adjust the size as needed
                .padding(bottom = 36.dp) )

        Text(text = "Verified Successfully!", fontFamily = helveticaFontFamily,fontWeight = FontWeight.Bold,fontSize = 20.sp,color = Color.White)
    }

}