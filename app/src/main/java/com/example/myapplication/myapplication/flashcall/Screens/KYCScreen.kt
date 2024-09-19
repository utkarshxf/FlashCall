package com.example.myapplication.myapplication.flashcall.Screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.myapplication.flashcall.Data.model.APIResponse
import com.example.myapplication.myapplication.flashcall.R
import com.example.myapplication.myapplication.flashcall.Screens.common.CircularLoaderButton
import com.example.myapplication.myapplication.flashcall.ViewModel.KycViewModel
import com.example.myapplication.myapplication.flashcall.ui.theme.BorderColor
import com.example.myapplication.myapplication.flashcall.ui.theme.BorderColor2
import com.example.myapplication.myapplication.flashcall.ui.theme.MainColor
import com.example.myapplication.myapplication.flashcall.ui.theme.PrimaryBackGround
import com.example.myapplication.myapplication.flashcall.ui.theme.SecondaryText
import com.example.myapplication.myapplication.flashcall.ui.theme.arimoFontFamily
import com.example.myapplication.myapplication.flashcall.ui.theme.helveticaFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KYCScreen(navController: NavController, vm: KycViewModel = hiltViewModel())
{
    Box(contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Back", tint = Color.Black, modifier = Modifier.size(28.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "KYC Documents", style = TextStyle(
                fontFamily = arimoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.Black
            ),
                )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Pan Card Number",
                style = TextStyle(
                    fontFamily = arimoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            panVerification(vm)

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Aadhar Card Details",
                style = TextStyle(
                    fontFamily = arimoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            aadharVerification()


            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(Color.White) // White background
                    .padding(2.dp) // Add padding to create space for the border
                    .drawBehind {
                        drawRoundRect(
                            color = Color.Black,
                            style = Stroke(
                                width = 2.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                            ),
                            cornerRadius = CornerRadius(10.dp.toPx())
                        )
                    }
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp), // Add horizontal padding for the content
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Upload,
                        contentDescription = "Upload Documents",
                        tint = Color(0xFF673AB7) // Purple icon color
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Upload Documents", color = Color(0xFF673AB7))
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun panVerification(vm: KycViewModel) {

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        var panNumber by remember {
            mutableStateOf("")
        }
        var loading by remember {
            mutableStateOf(false)
        }
        var isPanVerified by remember {
            mutableStateOf(false)
        }

        val panState by vm.panState.collectAsState()
        when(panState){
            APIResponse.Empty -> { }
            is APIResponse.Error -> { }
            APIResponse.Loading -> { }
            is APIResponse.Success -> { isPanVerified = true}
        }

        OutlinedTextField(
            shape = RoundedCornerShape(10.dp),
            value = panNumber,
            onValueChange = {
                panNumber = it.uppercase()
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .border(1.dp, color = BorderColor2, shape = RoundedCornerShape(10.dp)),
            placeholder = {
                Text(
                    text = "Enter PAN",
                    color = SecondaryText,
                    style = TextStyle(
                        fontFamily = arimoFontFamily,
                        fontWeight = FontWeight.Bold,
                    )
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = MainColor
            ),
            trailingIcon = {
                if(isPanVerified){
                    Image(painter = painterResource(id = R.drawable.baseline_verified_24), contentDescription = "", colorFilter = ColorFilter.tint(Color.Green))
                }

            },
            enabled = !isPanVerified
        )

        if(!isPanVerified){
            Box(contentAlignment = Alignment.CenterEnd) {
                CircularLoaderButton(
                    onClick = {
                        vm.makePanStateSuccess(panNumber
                        ) {
                            loading = it
                        }
                    },
                    modifier = Modifier
                        .width(80.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MainColor,
                        contentColor = Color.White
                    ),
                    text = "Add",
                    loading = loading,
                    enabled = panNumber.length == 10
                )
            }

        }
    }
}

@Composable
fun aadharVerification(){
    OutlinedTextField(
        shape = RoundedCornerShape(10.dp),
        value = "",
        onValueChange = { "" },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        maxLines = 1,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .border(1.dp, color = BorderColor2, shape = RoundedCornerShape(10.dp)),
        placeholder = {
            Text(
                text = "Enter Aadhar",
                color = SecondaryText,
                style = TextStyle(
                    fontFamily = arimoFontFamily,
                    fontWeight = FontWeight.Bold,
                )
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            cursorColor = MainColor
        )
    )
}



@Preview
@Composable
fun KycScreenPreview() {
    //panVerification(vm)
    //KYCScreen(navController = rememberNavController())
}
