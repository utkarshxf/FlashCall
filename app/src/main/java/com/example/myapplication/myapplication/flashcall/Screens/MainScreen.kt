package com.example.myapplication.myapplication.flashcall.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.myapplication.flashcall.bottomnav.BottomBar
import com.example.myapplication.myapplication.flashcall.bottomnav.BottomNavGraph

@Composable
fun MainScreen(navController: NavController)
{
    val homeNavController = rememberNavController()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomBar(navController = homeNavController)
        }
    ) {
        Column(modifier = Modifier.padding(it).fillMaxSize())
        {
            BottomNavGraph(homeNavController, navController)
        }
    }
}