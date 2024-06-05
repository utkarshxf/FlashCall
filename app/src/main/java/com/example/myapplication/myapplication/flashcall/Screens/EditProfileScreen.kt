package com.example.myapplication.myapplication.flashcall.Screens

import android.widget.Toast
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.R
import com.example.myapplication.myapplication.flashcall.ui.theme.BorderColor2
import com.example.myapplication.myapplication.flashcall.ui.theme.BottomBackground
import com.example.myapplication.myapplication.flashcall.ui.theme.MainColor
import com.example.myapplication.myapplication.flashcall.ui.theme.PrimaryBackGround
import com.example.myapplication.myapplication.flashcall.ui.theme.PrimaryText
import com.example.myapplication.myapplication.flashcall.ui.theme.SecondaryBackGround
import com.example.myapplication.myapplication.flashcall.ui.theme.SecondaryText
import com.example.myapplication.myapplication.flashcall.ui.theme.ThemeColorDarkBlue
import com.example.myapplication.myapplication.flashcall.ui.theme.ThemeColorGray
import com.example.myapplication.myapplication.flashcall.ui.theme.ThemeColorLightBlue
import com.example.myapplication.myapplication.flashcall.ui.theme.ThemeColorMaroon
import com.example.myapplication.myapplication.flashcall.ui.theme.ThemeColorOrange
import com.example.myapplication.myapplication.flashcall.ui.theme.ThemeColorPurple
import com.example.myapplication.myapplication.flashcall.ui.theme.ThemeColorYellow
import com.example.myapplication.myapplication.flashcall.ui.theme.arimoFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavController) {

    var edit_profile_name by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    var bio by remember {
        mutableStateOf("")
    }

    var colorSelected by remember {
        mutableStateOf(false)
    }

    var greenTheme by remember {
        mutableStateOf(false)
    }

    var blackTheme by remember {
        mutableStateOf(false)
    }

    var grayTheme by remember {
        mutableStateOf(false)
    }
    var darkBlueTheme by remember {
        mutableStateOf(false)
    }
    var lightBlueTheme by remember {
        mutableStateOf(false)
    }
    var maroonTheme by remember {
        mutableStateOf(false)
    }
    var purpleTheme by remember {
        mutableStateOf(false)
    }
    var yellowTheme by remember {
        mutableStateOf(false)
    }
    var orangeTheme by remember {
        mutableStateOf(false)
    }

    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
            ){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Back", tint = Color.White, modifier = Modifier.size(28.dp))
                    }


                    Text(text = "Edit your profile",
                        modifier = Modifier.padding(start = 18.dp),
                        style = TextStyle(
                            color = colorResource(id = R.color.black),
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,

                        ), color = Color.White
                    )

                    Image(
                        painter = painterResource(id = R.drawable.home_image),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(8.dp)
                            .clip(CircleShape)
                            .size(120.dp)
                    )
                }

                Box(modifier = Modifier.padding(start = 110.dp,top = 190.dp))
                {
                    Image(
                        painter = painterResource(id = R.drawable.edit_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable {

                            }
                    )
                }

                }


            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        PrimaryBackGround,
                        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                    )
            ){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(scrollState)
                ) {

                    Text(text = "Name",
                        textAlign = TextAlign.Start,
                        style = TextStyle(
                            fontFamily = arimoFontFamily,
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp
                        ),
                        color = SecondaryText,
                        modifier = Modifier.padding(start = 10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        shape = RoundedCornerShape(10.dp),
                        value = edit_profile_name,
                        onValueChange = {edit_profile_name = it},
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                            .border(1.dp, color = BorderColor2, shape = RoundedCornerShape(10.dp)),
                        placeholder = {
                            Text(
                                text="Your Name",
                                color = SecondaryText,
                                style = TextStyle(
                                    fontFamily = arimoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                )
                            )
                        },
                        maxLines = 1,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            cursorColor = MainColor
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(text = "Your Bio",
                        textAlign = TextAlign.Start,
                        style = TextStyle(
                            fontFamily = arimoFontFamily,
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp
                        ),
                        color = SecondaryText,
                        modifier = Modifier.padding(start = 10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                        .border(1.dp, color = BorderColor2, shape = RoundedCornerShape(10.dp)))
                    {
                        Column(modifier = Modifier
                            .padding(5.dp)
                            .fillMaxSize()) {
                            MyTextField(value = bio, onValueChange = {bio=it})
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(text = "Select your app theme color",
                        textAlign = TextAlign.Start,
                        style = TextStyle(
                            fontFamily = arimoFontFamily,
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp
                        ),
                        color = SecondaryText,
                        modifier = Modifier.padding(start = 10.dp)
                    )

                    Column {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                        ) {

                            Button(
                                shape = RoundedCornerShape(26.dp),
                                onClick = { greenTheme = !greenTheme },
                                colors = ButtonDefaults.buttonColors(MainColor),
                                modifier = Modifier
                            )
                            {
                                if(greenTheme)
                                {
                                    Icon(painter =  painterResource(id = R.drawable.selected_check_icon), contentDescription = null )
                                }
                            }
                            Spacer(modifier = Modifier.width(7.dp))

                            Button(
                                shape = RoundedCornerShape(26.dp),
                                onClick = { grayTheme = !grayTheme },
                                colors = ButtonDefaults.buttonColors(ThemeColorGray),
                                modifier = Modifier
                            )
                            {
                                if(grayTheme)
                                {
                                    Icon(painter =  painterResource(id = R.drawable.selected_check_icon), contentDescription = null )
                                }
                            }
                            Spacer(modifier = Modifier.width(7.dp))

                            Button(
                                shape = RoundedCornerShape(26.dp),
                                onClick = { blackTheme = !blackTheme },
                                colors = ButtonDefaults.buttonColors(Color.Black),
                                modifier = Modifier
                            )
                            {
                                if(blackTheme)
                                {
                                    Icon(painter =  painterResource(id = R.drawable.selected_check_icon), contentDescription = null )
                                }
                            }

                            Spacer(modifier = Modifier.width(7.dp))

                            Button(
                                shape = RoundedCornerShape(26.dp),
                                onClick = { darkBlueTheme = !darkBlueTheme },
                                colors = ButtonDefaults.buttonColors(ThemeColorDarkBlue),
                                modifier = Modifier
                            )
                            {
                                if(darkBlueTheme)
                                {
                                    Icon(painter =  painterResource(id = R.drawable.selected_check_icon), contentDescription = null )
                                }
                            }

                            Spacer(modifier = Modifier.width(7.dp))

                            Button(
                                shape = RoundedCornerShape(26.dp),
                                onClick = { lightBlueTheme = !lightBlueTheme },
                                colors = ButtonDefaults.buttonColors(ThemeColorLightBlue),
                                modifier = Modifier
                            )
                            {
                                if(lightBlueTheme)
                                {
                                    Icon(painter =  painterResource(id = R.drawable.selected_check_icon), contentDescription = null )
                                }
                            }


                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                        ){
                            Button(
                                shape = RoundedCornerShape(26.dp),
                                onClick = { maroonTheme = !maroonTheme },
                                colors = ButtonDefaults.buttonColors(ThemeColorMaroon),
                                modifier = Modifier
                            )
                            {
                                if(maroonTheme)
                                {
                                    Icon(painter =  painterResource(id = R.drawable.selected_check_icon), contentDescription = null )
                                }
                            }
                            Spacer(modifier = Modifier.width(7.dp))

                            Button(
                                shape = RoundedCornerShape(26.dp),
                                onClick = { purpleTheme = !purpleTheme },
                                colors = ButtonDefaults.buttonColors(ThemeColorPurple),
                                modifier = Modifier
                            )
                            {
                                if(purpleTheme)
                                {
                                    Icon(painter =  painterResource(id = R.drawable.selected_check_icon), contentDescription = null )
                                }
                            }
                            Spacer(modifier = Modifier.width(7.dp))

                            Button(
                                shape = RoundedCornerShape(26.dp),
                                onClick = { yellowTheme = !yellowTheme },
                                colors = ButtonDefaults.buttonColors(ThemeColorYellow),
                                modifier = Modifier
                            )
                            {
                                if(yellowTheme)
                                {
                                    Icon(painter =  painterResource(id = R.drawable.selected_check_icon), contentDescription = null )
                                }
                            }
                            Spacer(modifier = Modifier.width(7.dp))

                            Button(
                                shape = RoundedCornerShape(26.dp),
                                onClick = { orangeTheme = !orangeTheme },
                                colors = ButtonDefaults.buttonColors(ThemeColorOrange),
                                modifier = Modifier
                            )
                            {
                                if(orangeTheme)
                                {
                                    Icon(painter =  painterResource(id = R.drawable.selected_check_icon), contentDescription = null )
                                }
                            }
                        }

                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    )
                    {
                        Button(
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.width(150.dp).height(60.dp),
                            colors = ButtonDefaults.buttonColors(SecondaryBackGround),
                            onClick = {
                                navController.navigate(ScreenRoutes.HomeScreen.route)
                            }
                        )
                        {
                            Text(text = "Cancel",
                                textAlign = TextAlign.Center,
                                style = TextStyle(
                                    fontFamily = arimoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                ),color = Color.White
                            )
                        }

                        Button(
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.width(150.dp).height(60.dp),
                            colors = ButtonDefaults.buttonColors(MainColor),
                            onClick = {
                                navController.navigate(ScreenRoutes.HomeScreen.route)
                                Toast.makeText(context, "Profile Updated", Toast.LENGTH_SHORT).show()
                            }
                        )
                        {
                            Text(text = "NEXT",
                                textAlign = TextAlign.Center,
                                style = TextStyle(
                                    fontFamily = arimoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                ),color = Color.White
                            )
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun MyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hintText : String = "Your Bio"
){
    val scrollState = rememberScrollState()
    val maxChar = 100
    var charCount = value.count() // How do I update this in onValueChange?

    BasicTextField(
        value = value,
        onValueChange = {
            charCount = it.count()
            if(charCount <= maxChar)
                onValueChange(it)
        },
        modifier = modifier,
        maxLines = 5,
        textStyle = TextStyle(
            color = Color.Black,
            fontSize = 18.sp
        ),
        decorationBox = { innerTextField ->

                Column(
                    Modifier
                        .padding(12.dp)
                        .verticalScroll(scrollState)
                ) {
                    if (value.isEmpty())
                    {
                        Text(
                            text = hintText,
                            color = SecondaryText,
                            fontSize = 16.sp
                        )
                    }
                innerTextField()
                Spacer(Modifier.height(50.dp))
                Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.Bottom) {
                    Spacer(Modifier.weight(1f))
                    Text(text = charCount.toString() + "/100")
                }
            }
        }
    )
}
