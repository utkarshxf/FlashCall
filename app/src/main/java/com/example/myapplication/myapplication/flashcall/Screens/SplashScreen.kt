package com.example.myapplication.myapplication.flashcall.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.myapplication.flashcall.R
import com.example.myapplication.myapplication.flashcall.ui.theme.Background2


@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .background(color = Background2)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(painter = painterResource(id = R.drawable.logo), contentDescription = "logo")
            Spacer(modifier = Modifier.height(20.dp))
            Icon(painter = painterResource(id = R.drawable.flashcall), contentDescription = "logo", tint = Color.White)
        }
    }
}