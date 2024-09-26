package com.example.myapplication.myapplication.flashcall.Screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.myapplication.flashcall.BaseClass
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.R
import com.example.myapplication.myapplication.flashcall.ui.theme.MainColor
import com.example.myapplication.myapplication.flashcall.ui.theme.PrimaryBackGround
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SelectSpecialityScreen(navController: NavController) {
    var selectedIndex by remember { mutableStateOf(-1) } // Track the selected item

    val items = listOf(
        "Astrologer", "Designer", "Nutritionist", "Fitness", "Yoga", "Aayurved",
        "Share Market", "Marketing", "Legal Advisor", "Relationship", "Other"
    )

    val images = listOf(
        R.drawable.group_26, R.drawable.group_27, R.drawable.group_28,
        R.drawable.group_29, R.drawable.group_30, R.drawable.group_31,
        R.drawable.group_32, R.drawable.group_33, R.drawable.group_34,
        R.drawable.group_35, R.drawable.group_36
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(color = PrimaryBackGround)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(),
                    title = { Text(text = "Back") },
                    navigationIcon = {
                        IconButton(onClick = { }) {
                            Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Back")
                        }
                    })
            }
        ) {
            // Using BoxWithConstraints to control the height of LazyVerticalGrid
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(8.dp)
            ) {
                val maxHeight = maxHeight

                Column {
                    Text(
                        text = "Select Your Speciality",
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    // Constraining the height of LazyVerticalGrid
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier
                            .height(maxHeight * 0.7f) // Give LazyVerticalGrid a fixed height relative to available space
                            .padding(10.dp)
                    ) {
                        items(items.size) { index ->
                            SpecialityItem(
                                index = index,
                                items = items,
                                imageRes = images[index],
                                isSelected = index == selectedIndex, // Check if the current item is selected
                                onClick = { selectedIndex = index } // Update the selected item on click
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(60.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (selectedIndex != -1) Color(MainColor.value) else Color.Gray // Change color based on selection
                            )
                    ) {
                        ButtonSpeciality(isSelected = selectedIndex != -1, navController)
                    }
                }
            }
        }
    }

}

@Composable
fun ButtonSpeciality(isSelected: Boolean, navController: NavController) {
    Button(
        onClick = {
            navController.navigate(ScreenRoutes.LoginDoneScreen.route)
        },
        modifier = Modifier
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(MainColor.value) else Color.Gray,
            contentColor = Color.White
        )
    ) {
        Text(text = "DONE", fontWeight = FontWeight.Bold, fontSize = 20.sp)
    }
}
@Composable
fun SpecialityItem(
    index: Int,
    items: List<String>,
    imageRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp) // Increase padding for more spacing
                .size(100.dp) // Set a fixed size for each item
                .background(Color.White, shape = CircleShape)
                .let {
                    if (isSelected) it.border(
                        width = 3.dp,
                        color = Color(MainColor.value), // Show border only when selected
                        shape = CircleShape
                    ) else it
                }
                .clickable(onClick = onClick) // Handle click event
                .shadow(8.dp, CircleShape), // Add shadow for 3D effect
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
        Text(text = items[index], fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
    }
}


