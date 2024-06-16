package com.example.myapplication.myapplication.flashcall.utils

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.myapplication.myapplication.flashcall.ui.theme.MainColor

@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier,
) {

    CircularProgressIndicator(
        modifier = modifier,
        color = MainColor
    )
}
