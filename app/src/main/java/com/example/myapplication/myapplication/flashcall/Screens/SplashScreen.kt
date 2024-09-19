package com.example.myapplication.myapplication.flashcall.Screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.R
import kotlinx.coroutines.delay


@Composable
fun SplashScreen() {
    Box(modifier = Modifier.paint(painter = painterResource(id = R.drawable.splash_screen) , contentScale = ContentScale.FillBounds)
        .fillMaxSize())
}