package com.example.myapplication.myapplication.flashcall.Screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import co.hyperverge.hyperkyc.data.models.HyperKycConfig
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.Data.model.APIResponse
import com.example.myapplication.myapplication.flashcall.Data.model.spacialization.SpacializationResponse
import com.example.myapplication.myapplication.flashcall.R
import com.example.myapplication.myapplication.flashcall.ViewModel.RegistrationViewModel
import com.example.myapplication.myapplication.flashcall.ui.theme.BorderColor
import com.example.myapplication.myapplication.flashcall.ui.theme.BorderColor2
import com.example.myapplication.myapplication.flashcall.ui.theme.MainColor
import com.example.myapplication.myapplication.flashcall.ui.theme.PrimaryBackGround
import com.example.myapplication.myapplication.flashcall.ui.theme.ProfileBackground
import com.example.myapplication.myapplication.flashcall.ui.theme.helveticaFontFamily
import com.example.myapplication.myapplication.flashcall.utils.LoadingIndicator

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SelectSpecialityScreen(navController: NavController,
                           viewModel: RegistrationViewModel = hiltViewModel()) {
    var selectedIndex by remember { mutableStateOf(-1) } // Track the selected item
    var selectedPrefession by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.getSpacialization()
    }

    Surface(
        modifier = Modifier.fillMaxSize(), color = ProfileBackground
    ) {

        Column {
            Spacer(modifier = Modifier.height(30.dp))
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Back", tint = Color.Black, modifier = Modifier.size(28.dp))
            }

            Text(
                text = "Select Your Speciality",
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                modifier = Modifier.padding(start = 10.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))

            val spacializationState by viewModel.spacializationState.collectAsState()

            when(spacializationState){
                APIResponse.Empty -> {

                }
                is APIResponse.Error -> {
                    val error = (spacializationState as APIResponse.Error).message
                    Text(text = "error: $error", color = Color.Red)
                }
                APIResponse.Loading -> {
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center){
                        Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                            LoadingIndicator()
                        }
                    }
                }
                is APIResponse.Success -> {
                    val professions = (spacializationState as APIResponse.Success<SpacializationResponse>).data.professions
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier
                            .weight(1f)
                            .padding(10.dp)
                    ) {
                        itemsIndexed(professions!!){ index, item ->
                            SpecialityItem(
                                name = item?.name?:"default",
                                imageUrl = item?.icon?: "default",
                                isSelected = index == selectedIndex, // Check if the current item is selected
                                onClick = {
                                    selectedIndex = index
                                    selectedPrefession = professions!!.get(selectedIndex)?.name+""
                                } // Update the selected item on click
                            )
                        }
                    }

                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            val updateUserState = viewModel.addProfessionState.collectAsState()

            when(updateUserState.value){
                APIResponse.Empty -> {

                }
                is APIResponse.Error -> {
                    val error = (updateUserState.value as APIResponse.Error).message
                    Row (horizontalArrangement = Arrangement.Center, modifier = Modifier.padding(10.dp)){
                        Text(text = "error: $error", color = Color.Red)
                    }
                }
                APIResponse.Loading -> {
                    Row (horizontalArrangement = Arrangement.Center, modifier = Modifier.padding(10.dp)){
                        LoadingIndicator()
                    }
                }
                is APIResponse.Success -> {
                    LaunchedEffect(key1 = Unit) {
                        navController.navigate(ScreenRoutes.LoginDoneScreen.route)
                    }
                }
            }


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .background(
                        if (selectedIndex != -1) Color(MainColor.value) else Color.Gray // Change color based on selection
                    )
            ) {
                ButtonSpeciality(isSelected = selectedIndex != -1, viewModel, selectedPrefession)
            }
        }
    }


}

@Composable
fun ButtonSpeciality(
    isSelected: Boolean,
    viewmodel: RegistrationViewModel,
    selectedPrefession: String
) {
    val context = LocalContext.current

    Button(
        onClick = {
            if(selectedPrefession.isNotEmpty()){
                viewmodel.addSpacialization(profession = selectedPrefession)
            }else{
                Toast.makeText(context, "Select Specialization Please",Toast.LENGTH_SHORT).show()
            }
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
    name: String,
    imageUrl: String,
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
                .size(82.dp) // Set a fixed size for each item
                .clip(CircleShape)
                .clickable {
                    onClick.invoke()
                }
                .shadow(8.dp, CircleShape) // Add shadow for 3D effect
                .background(Color.White, shape = CircleShape)
                .let {
                    if (isSelected) it.border(
                        width = 3.dp,
                        color = Color(MainColor.value), // Show border only when selected
                        shape = CircleShape
                    ) else it
                },
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(imageUrl).crossfade(true).build(),
                contentDescription = null,
                placeholder = painterResource(id = R.drawable.profile_picture_holder),
                modifier = Modifier
                    .fillMaxSize()  // Ensure a consistent size
                    .clip(CircleShape),  // Clip to a circle
//                contentScale = ContentScale.Crop  // Crop the image to fit within the circle
            )
        }
        Text(text = name, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal, fontFamily = helveticaFontFamily)
    }
}


