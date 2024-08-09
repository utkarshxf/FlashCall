package com.example.myapplication.myapplication.flashcall.Screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.myapplication.flashcall.ui.theme.PrimaryBackGround

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SelectSpecialityScreen() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(color = PrimaryBackGround)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(

                    ),
                    title = { },
                    navigationIcon = {
                        IconButton(onClick = { }) {
                            Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Back")
                        }
                    })
            }
        ) {

        }

    }

}

@Preview
@Composable
fun SelectSpecialityScreenPreview(){
    SelectSpecialityScreen()
}