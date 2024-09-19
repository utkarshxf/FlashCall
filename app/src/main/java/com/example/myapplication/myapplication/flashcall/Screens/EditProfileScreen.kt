package com.example.myapplication.myapplication.flashcall.Screens

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.Data.model.APIResponse
import com.example.myapplication.myapplication.flashcall.R
import com.example.myapplication.myapplication.flashcall.Screens.common.CircularLoaderButton
import com.example.myapplication.myapplication.flashcall.ViewModel.AuthenticationViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.RegistrationViewModel
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
import com.example.myapplication.myapplication.flashcall.ui.theme.ThemeColorLightMaroon
import com.example.myapplication.myapplication.flashcall.ui.theme.ThemeColorMaroon
import com.example.myapplication.myapplication.flashcall.ui.theme.ThemeColorOrange
import com.example.myapplication.myapplication.flashcall.ui.theme.ThemeColorPurple
import com.example.myapplication.myapplication.flashcall.ui.theme.ThemeColorYellow
import com.example.myapplication.myapplication.flashcall.ui.theme.arimoFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavController,registrationViewModel: RegistrationViewModel = hiltViewModel(), authenticationViewModel: AuthenticationViewModel= hiltViewModel())
{
    var uriImg: Uri? = null

    var uid by remember {
        mutableStateOf("")
    }
    var name by remember {
        mutableStateOf("")
    }
    var username by remember {
        mutableStateOf("")
    }
    var userId by remember {
        mutableStateOf("")
    }
    var phone by remember {
        mutableStateOf("")
    }
    var profession by remember {
        mutableStateOf("")
    }
    var gender by remember {
        mutableStateOf("")
    }
    var themeSelected by remember {
        mutableStateOf("")
    }
    var profilePic by remember {
        mutableStateOf("")
    }
    var dob by remember {
        mutableStateOf("")
    }
    var bio by remember {
        mutableStateOf("")
    }
    val (firstName, lastName) = name.trim().split(" ", limit = 2).let {
        when {
            it.isEmpty() -> "" to ""
            it.size == 1 -> it[0] to ""
            else -> it[0] to it[1]
        }
    }
    Log.d("First Name", "$firstName")
    val context = LocalContext.current
    if (uriImg != null) {
        uriImg.let { uri ->
            uploadImageToFirebase(uri, context) { url ->
                imageUrl = url
                registrationViewModel.updateUser(
                    userId = uid,
                    username = username,
                    phone = phone,
                    fullName = name,
                    firstName = firstName,
                    lastName = lastName,
                    photo = imageUrl,
                    profession = profession,
                    themeSelected = themeSelected,
                    gender = gender,
                    dob = dob,
                    bio = bio,
                    navController = navController
                ){}
            }
        }
    }

    val createdUserState2 by authenticationViewModel.isCreatedUserState.collectAsState()
    val createUserState1 by registrationViewModel.createUserState.collectAsState()
    Log.d("UserCreatedAlready", "$createdUserState2")
    Log.d("CreatedUserData", createUserState1.toString())
    Log.d("CreateUserResponse", "$createUserState1")

    val userData = authenticationViewModel.getUserFromPreferences(context)
    if (userData != null ) {
        uid = userData._id ?: ""
        Log.d("uid", "$uid")
        username = userData.username ?: ""
        name = userData.fullName ?: ""
        phone = userData.phone ?: ""
        userId = userData.username ?: ""
        gender = userData.gender ?: ""
        dob = userData.dob ?: ""
        bio = userData.bio ?: ""
        Log.d("bio", "$bio")
        themeSelected = userData.themeSelected ?: ""
        profession = userData.profession ?: ""
        profilePic = userData.photo ?: ""

    }
    else{
        when (createUserState1) {
            is APIResponse.Success -> {
                val response = (createUserState1 as APIResponse.Success).data
                Log.d("UserResponse", response.toString())
                uid = response._id
                name = response.fullName
                userId = response.username
                profilePic = response.photo
                Log.d("UserId", uid.toString())

            }

            APIResponse.Empty -> Log.e("EmptyError", "empty")
            is APIResponse.Error -> {
                Log.e("Error", "Error Failed")
            }

            APIResponse.Loading -> Log.e("Loading", "Loading")
        }
    }

    val imageUri = rememberSaveable {
        mutableStateOf("")
    }
    val painter = rememberAsyncImagePainter(
        imageUri.value.ifEmpty { R.drawable.profile_picture_holder }
    )



    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->

        uri?.let {
            uriImg = it
            imageUri.value = it.toString()

            if (uriImg != null) {
                uriImg.let { uri ->
                    uploadImageToFirebase(uri, context) { url ->
                        imageUrl = url
                        registrationViewModel.updateUser(
                            userId = uid,
                            username = username,
                            phone = phone,
                            fullName = name,
                            firstName = firstName,
                            lastName = lastName,
                            photo = imageUrl,
                            profession = profession,
                            themeSelected = themeSelected,
                            gender = gender,
                            dob = dob,
                            bio = bio,
                            navController = navController
                        ){}
                    }
                }
            }
        }
    }



    var edit_profile_name by remember {
        mutableStateOf("")
    }

    var edit_profile_bio by remember {
        mutableStateOf("")
    }

    var colorSelected by remember {
        mutableStateOf(false)
    }

    var greenTheme by remember {
        mutableStateOf(false)
    }

    var blackTheme by remember {
        mutableStateOf(true)
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
    var lightMaroonTheme by remember {
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
    var loading by remember { mutableStateOf(false) }

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
                    Spacer(modifier = Modifier.height(10.dp))
                    if (profilePic=="") {
                        Box(modifier = Modifier.padding(start = 8.dp))
                        {

                            Image(
                                painter = painter,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .clip(CircleShape)
                                    .size(120.dp)
                            )
                            Image(
                                painter = painterResource(id = R.drawable.edit_icon),
                                contentDescription = null,
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .clip(CircleShape)
                                    .clickable {
                                        launcher.launch("image/*")

                                    }
                            )
                        }
                    }else{
                        Box(modifier = Modifier.padding(start = 8.dp))
                        {
                        ImageFromUrl(imageUrl = profilePic!!)

                            Image(
                                painter = painterResource(id = R.drawable.edit_icon),
                                contentDescription = null,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .align(Alignment.BottomEnd)
                                    .clickable {
                                        launcher.launch("image/*")

                                    }
                            )
                        }
                    }
                }

                Box(modifier = Modifier.padding(start = 110.dp,top = 190.dp))
                {
                    if (profilePic==""){
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp)
                        )
                        {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

//                                Image(
//                                    painter = painter,
//                                    contentDescription = null,
//                                    contentScale = ContentScale.Crop,
//                                    modifier = Modifier
//                                        .clip(CircleShape)
//                                        .size(96.dp)
//                                )
                            }

                            Box(modifier = Modifier.padding(start = 200.dp, top = 70.dp))
                            {
                                Image(
                                    painter = painterResource(id = R.drawable.edit_icon),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .clickable {
                                            launcher.launch("image/*")

                                        }
                                )
                            }

                        }


                    }

                    else {
                        Box(modifier = Modifier.padding(start = 200.dp, top = 70.dp))
                        {
                            Image(
                                painter = painterResource(id = R.drawable.edit_icon),
                                contentDescription = null,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .clickable {
                                        launcher.launch("image/*")

                                    }
                            )
                        }
                    }
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
                                text=if(name!=""){
                                    name
                                }else{
                                    "Enter Yor Name"
                                },
                                color = Color.Black,
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
                            MyTextField(value = edit_profile_bio, onValueChange = {edit_profile_bio=it}, hintText = bio)
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


                        // Theme First Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                                .padding(horizontal = 10.dp)
                        ) {


                            // Black theme circle
                            Box(contentAlignment = Alignment.Center) {

                                Button(onClick = {
                                    blackTheme = true
                                    grayTheme = false
                                    darkBlueTheme = false
                                    lightBlueTheme = false
                                    maroonTheme = false
                                    lightMaroonTheme = false
                                    greenTheme = false
                                    purpleTheme = false
                                    yellowTheme = false
                                    orangeTheme = false

                                },
                                    shape = CircleShape,
                                    colors = ButtonDefaults.buttonColors(Color.Black),
                                    modifier = Modifier.size(36.dp)) {
                                }

                                if(blackTheme)
                                {
                                    Icon(painter =  painterResource(id = R.drawable.selected_check_icon),
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(36.dp)
                                            .drawBehind {
                                                drawCircle(color = Color.Black, style = Stroke(width = 5f,
                                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)
                                                )
                                                )
                                            }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(15.dp))


                            // Gray theme circle
                            Box(contentAlignment = Alignment.Center) {

                                Button(onClick = {
                                    blackTheme = false
                                    grayTheme = true
                                    darkBlueTheme = false
                                    lightBlueTheme = false
                                    maroonTheme = false
                                    lightMaroonTheme = false
                                    greenTheme = false
                                    purpleTheme = false
                                    yellowTheme = false
                                    orangeTheme = false
                                },
                                    shape = CircleShape,
                                    colors = ButtonDefaults.buttonColors(ThemeColorGray),
                                    modifier = Modifier.size(36.dp)) {
                                }

                                if(grayTheme)
                                {
                                    Icon(painter =  painterResource(id = R.drawable.selected_check_icon),
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(36.dp)
                                            .drawBehind {
                                                drawCircle(color = ThemeColorGray, style = Stroke(width = 5f,
                                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)
                                                )
                                                )
                                            }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(15.dp))


                            // Dark Blue theme circle
                            Box(contentAlignment = Alignment.Center) {

                                Button(onClick = {
                                    blackTheme = false
                                    grayTheme = false
                                    darkBlueTheme = true
                                    lightBlueTheme = false
                                    maroonTheme = false
                                    lightMaroonTheme = false
                                    greenTheme = false
                                    purpleTheme = false
                                    yellowTheme = false
                                    orangeTheme = false
                                },
                                    shape = CircleShape,
                                    colors = ButtonDefaults.buttonColors(ThemeColorDarkBlue),
                                    modifier = Modifier.size(36.dp)) {
                                }

                                if(darkBlueTheme)
                                {
                                    Icon(painter =  painterResource(id = R.drawable.selected_check_icon),
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(36.dp)
                                            .drawBehind {
                                                drawCircle(color = ThemeColorDarkBlue, style = Stroke(width = 5f,
                                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)
                                                )
                                                )
                                            }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(15.dp))

                            // Light Blue theme circle
                            Box(contentAlignment = Alignment.Center) {

                                Button(onClick = {
                                    blackTheme = false
                                    grayTheme = false
                                    darkBlueTheme = false
                                    lightBlueTheme = true
                                    maroonTheme = false
                                    lightMaroonTheme = false
                                    greenTheme = false
                                    purpleTheme = false
                                    yellowTheme = false
                                    orangeTheme = false
                                },
                                    shape = CircleShape,
                                    colors = ButtonDefaults.buttonColors(ThemeColorLightBlue),
                                    modifier = Modifier.size(36.dp)) {
                                }

                                if(lightBlueTheme)
                                {
                                    Icon(painter =  painterResource(id = R.drawable.selected_check_icon),
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(36.dp)
                                            .drawBehind {
                                                drawCircle(color = ThemeColorLightBlue, style = Stroke(width = 5f,
                                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)
                                                )
                                                )
                                            }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(15.dp))


                            //Maroon theme circle
                            Box(contentAlignment = Alignment.Center) {

                                Button(onClick = {
                                    blackTheme = false
                                    grayTheme = false
                                    darkBlueTheme = false
                                    lightBlueTheme = false
                                    maroonTheme = true
                                    lightMaroonTheme = false
                                    greenTheme = false
                                    purpleTheme = false
                                    yellowTheme = false
                                    orangeTheme = false
                                },
                                    shape = CircleShape,
                                    colors = ButtonDefaults.buttonColors(ThemeColorMaroon),
                                    modifier = Modifier.size(36.dp)) {
                                }

                                if(maroonTheme)
                                {
                                    Icon(painter =  painterResource(id = R.drawable.selected_check_icon),
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(36.dp)
                                            .drawBehind {
                                                drawCircle(color = ThemeColorMaroon, style = Stroke(width = 5f,
                                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)
                                                )
                                                )
                                            }
                                    )
                                }
                            }


                            Spacer(modifier = Modifier.width(15.dp))


                            //Light Maroon theme circle
                            Box(contentAlignment = Alignment.Center) {

                                Button(onClick = {
                                    blackTheme = false
                                    grayTheme = false
                                    darkBlueTheme = false
                                    lightBlueTheme = false
                                    maroonTheme = false
                                    lightMaroonTheme = true
                                    greenTheme = false
                                    purpleTheme = false
                                    yellowTheme = false
                                    orangeTheme = false
                                },
                                    shape = CircleShape,
                                    colors = ButtonDefaults.buttonColors(ThemeColorLightMaroon),
                                    modifier = Modifier.size(36.dp)) {
                                }

                                if(lightMaroonTheme)
                                {
                                    Icon(painter =  painterResource(id = R.drawable.selected_check_icon),
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(36.dp)
                                            .drawBehind {
                                                drawCircle(color = ThemeColorLightMaroon, style = Stroke(width = 5f,
                                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)
                                                )
                                                )
                                            }
                                    )
                                }
                            }



                        }


                        // Themes Second Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                                .padding(horizontal = 10.dp)
                        ){


                            // Green theme circle
                            Box(contentAlignment = Alignment.Center) {

                                Button(onClick = {
                                    blackTheme = false
                                    grayTheme = false
                                    darkBlueTheme = false
                                    lightBlueTheme = false
                                    maroonTheme = false
                                    lightMaroonTheme = false
                                    greenTheme = true
                                    purpleTheme = false
                                    yellowTheme = false
                                    orangeTheme = false
                                },
                                    shape = CircleShape,
                                    colors = ButtonDefaults.buttonColors(MainColor),
                                    modifier = Modifier.size(36.dp)) {
                                }

                                if(greenTheme)
                                {
                                    Icon(painter =  painterResource(id = R.drawable.selected_check_icon),
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(36.dp)
                                            .drawBehind {
                                                drawCircle(color = MainColor, style = Stroke(width = 5f,
                                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)
                                                )
                                                )
                                            }
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(15.dp))


                            //Purple theme circle
                            Box(contentAlignment = Alignment.Center) {

                                Button(onClick = {
                                    blackTheme = false
                                    grayTheme = false
                                    darkBlueTheme = false
                                    lightBlueTheme = false
                                    maroonTheme = false
                                    lightMaroonTheme = false
                                    greenTheme = false
                                    purpleTheme = true
                                    yellowTheme = false
                                    orangeTheme = false
                                },
                                    shape = CircleShape,
                                    colors = ButtonDefaults.buttonColors(ThemeColorPurple),
                                    modifier = Modifier.size(36.dp)) {
                                }

                                if(purpleTheme)
                                {
                                    Icon(painter =  painterResource(id = R.drawable.selected_check_icon),
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(36.dp)
                                            .drawBehind {
                                                drawCircle(color = ThemeColorPurple, style = Stroke(width = 5f,
                                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)
                                                )
                                                )
                                            }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(15.dp))


                            //Yellow theme circle
                            Box(contentAlignment = Alignment.Center) {

                                Button(onClick = {
                                    blackTheme = false
                                    grayTheme = false
                                    darkBlueTheme = false
                                    lightBlueTheme = false
                                    maroonTheme = false
                                    lightMaroonTheme = false
                                    greenTheme = false
                                    purpleTheme = false
                                    yellowTheme = true
                                    orangeTheme = false
                                },
                                    shape = CircleShape,
                                    colors = ButtonDefaults.buttonColors(ThemeColorYellow),
                                    modifier = Modifier.size(36.dp)) {
                                }

                                if(yellowTheme)
                                {
                                    Icon(painter =  painterResource(id = R.drawable.selected_check_icon),
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(36.dp)
                                            .drawBehind {
                                                drawCircle(color = ThemeColorYellow, style = Stroke(width = 5f,
                                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)
                                                )
                                                )
                                            }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(15.dp))


                            //Orange theme circle
                            Box(contentAlignment = Alignment.Center) {

                                Button(onClick = {
                                    blackTheme = false
                                    grayTheme = false
                                    darkBlueTheme = false
                                    lightBlueTheme = false
                                    maroonTheme = false
                                    lightMaroonTheme = false
                                    greenTheme = false
                                    purpleTheme = false
                                    yellowTheme = false
                                    orangeTheme = true
                                },
                                    shape = CircleShape,
                                    colors = ButtonDefaults.buttonColors(ThemeColorOrange),
                                    modifier = Modifier.size(36.dp)) {
                                }

                                if(orangeTheme)
                                {
                                    Icon(painter =  painterResource(id = R.drawable.selected_check_icon),
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(36.dp)
                                            .drawBehind {
                                            drawCircle(color = ThemeColorOrange, style = Stroke(width = 5f,
                                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)
                                            )
                                            )
                                        }
                                    )

                                }
                            }



                        }

                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    // Bottom Cancel And Update Buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    )
                    {
                        Button(
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .width(150.dp)
                                .height(48.dp),
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
                        CircularLoaderButton(
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .width(150.dp)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(MainColor),
                            loading = loading,
                            text = "UPDATE",
                            textStyle = TextStyle(
                                fontFamily = arimoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            ),
                            onClick = {
                                registrationViewModel.updateUser(
                                    userId = uid,
                                    username = username,
                                    phone = phone ,
                                    fullName = edit_profile_name,
                                    firstName = firstName,
                                    lastName= lastName,
                                    photo = imageUrl,
                                    profession = profession,
                                    themeSelected = themeSelected,
                                    gender = gender,
                                    dob = dob,
                                    bio = edit_profile_bio,
                                    navController = navController,
                                ){ loading = it }
                            }
                        )
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
    hintText : String
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

                Box(
                    Modifier
                        .padding(12.dp)
                        .verticalScroll(scrollState)
                ) {
                    innerTextField()
                    if (value.isEmpty())
                    {
                        Text(
                            text = hintText,
                            color = Color.Black,
                            fontSize = 16.sp
                        )
                    }


                Spacer(Modifier.height(50.dp))
                Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.Bottom) {
                    Spacer(Modifier.weight(1f))
                    Text(text = charCount.toString() + "/100")
                }
            }
        }
    )
}


//@Preview(showBackground = true)
//@Composable
//private fun PreviewEditProfile() {
//    val resistrationViewModel: RegistrationViewModel = hiltViewModel()
//    val authenticationViewModel: AuthenticationViewModel = hiltViewModel()
//    EditProfileScreen(navController = rememberNavController(),registrationViewModel = resistrationViewModel, authenticationViewModel = authenticationViewModel)
//}
