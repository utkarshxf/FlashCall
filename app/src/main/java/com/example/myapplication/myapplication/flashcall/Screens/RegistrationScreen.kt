package com.example.myapplication.myapplication.flashcall.Screens

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.Data.model.APIResponse
import com.example.myapplication.myapplication.flashcall.R
import com.example.myapplication.myapplication.flashcall.ViewModel.AuthenticationViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.RegistrationViewModel
import com.example.myapplication.myapplication.flashcall.ui.theme.BorderColor2
import com.example.myapplication.myapplication.flashcall.ui.theme.MainColor
import com.example.myapplication.myapplication.flashcall.ui.theme.PrimaryBackGround
import com.example.myapplication.myapplication.flashcall.ui.theme.SecondaryText
import com.example.myapplication.myapplication.flashcall.ui.theme.arimoFontFamily
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

var uriImg : Uri? = null
var imageUrl : String? = null
var imageUploadCounter = false
var userToken : String = ""
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(navController: NavController, registrationViewModel: RegistrationViewModel, authenticationViewModel: AuthenticationViewModel)
{
    var buttonColor1 by remember {
        mutableStateOf(
            Color.White
        )
    }

    val context = LocalContext.current

    var buttonColor by remember { mutableStateOf(Color.White) }

    var currentDate by remember {
        mutableStateOf(LocalDate.now())
    }
    var dateState by remember {
        mutableStateOf(false)
    }

    val phoneNumber = authenticationViewModel.phoneNumber.value

    var genderMale by remember { mutableStateOf(false) }
    var genderFemale by remember { mutableStateOf(false) }
    var genderOthers by remember { mutableStateOf(false) }

    var name by remember {
        mutableStateOf("")
    }

    var userId by remember {
        mutableStateOf("")
    }

    var selectedGender by remember {
        mutableStateOf("")
    }

    var dateOfBirth by remember {
        mutableStateOf("")
    }

    val scrollState = rememberScrollState()


    val autheticationState by authenticationViewModel.verifyOTPState.collectAsState()

    when(autheticationState){
        APIResponse.Empty -> {Log.e("Error", "Error Failed")}
        is APIResponse.Error -> Log.e("Error", "Error Failed")
        APIResponse.Loading -> Log.e("Error", "Error Failed")
        is APIResponse.Success ->{
            val response = (autheticationState as APIResponse.Success).data
                userToken = response.token.toString()
            Log.d("UserTokenAPI", "$token")
        }
    }


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
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Back")
                        }
                    })}
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .verticalScroll(scrollState)
                    .padding(8.dp)
            ) {

                Text(
                    text = "Enter your details",
                    style = TextStyle(
                        color = colorResource(id = R.color.black),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

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
                    }
                }

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
                        Image(
                            painter = painter,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(96.dp)
                        )
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

                Spacer(modifier = Modifier.height(25.dp))

                Text(
                    text = "Name*",
                    textAlign = TextAlign.Start,
                    style = TextStyle(
                        fontFamily = arimoFontFamily,
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp
                    ),
                    color = SecondaryText,
                    modifier = Modifier.padding(start = 10.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    shape = RoundedCornerShape(10.dp),
                    value = name,
                    onValueChange = { name = it },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    maxLines = 1,
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.profile_icon_register),
                            contentDescription = null
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.White)
                        .border(1.dp, color = BorderColor2, shape = RoundedCornerShape(10.dp)),
                    placeholder = {
                        Text(
                            text = "Your Name",
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

                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = "Create your ID*",
                    textAlign = TextAlign.Start,
                    style = TextStyle(
                        fontFamily = arimoFontFamily,
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp
                    ),
                    color = SecondaryText,
                    modifier = Modifier.padding(start = 10.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    shape = RoundedCornerShape(10.dp),
                    value = userId,
                    onValueChange = { userId = it },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    maxLines = 1,
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.id_icon_register),
                            contentDescription = null
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.White)
                        .border(1.dp, color = BorderColor2, shape = RoundedCornerShape(10.dp)),
                    placeholder = {
                        Text(
                            text = "Your UserId",
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
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Ex : Nitra123@Consultant",
                    textAlign = TextAlign.Start,
                    style = TextStyle(
                        fontFamily = arimoFontFamily,
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp
                    ),
                    color = SecondaryText,
                    modifier = Modifier.padding(start = 10.dp)
                )

                Spacer(modifier = Modifier.height(15.dp))


                Text(
                    text = "Select your Gender*",
                    textAlign = TextAlign.Start,
                    style = TextStyle(
                        fontFamily = arimoFontFamily,
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp
                    ),
                    color = SecondaryText,
                    modifier = Modifier.padding(start = 10.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {

                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(50.dp)
                            .background(Color.White)
                            .border(
                                if (genderMale) 4.dp else 2.dp,
                                color = BorderColor2,
                                shape = RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    )
                    {
                        Text(text = "Male",
                            modifier = Modifier
                                .clickable {
                                    genderMale = !genderMale
                                    selectedGender = "Male"
                                    genderFemale = false
                                    genderOthers = false
                                },
                            style = TextStyle(
                                fontFamily = arimoFontFamily,
                                fontWeight = if (genderMale) FontWeight.Bold else FontWeight.Black,
                                fontSize = if (genderMale) 16.sp else 14.sp
                            )
                        )

                    }

                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(50.dp)
                            .background(Color.White)
                            .border(
                                if (genderFemale) 4.dp else 2.dp,
                                color = BorderColor2,
                                shape = RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    )
                    {
                        Text(
                            text = "Female",
                            modifier = Modifier.clickable {
                                genderFemale = !genderFemale
                                selectedGender = "Female"
                                genderOthers = false
                                genderMale = false
                            },
                            style = TextStyle(
                                fontFamily = arimoFontFamily,
                                fontWeight = if (genderFemale) FontWeight.Bold else FontWeight.Black,
                                fontSize = if (genderFemale) 16.sp else 14.sp
                            )
                        )

                    }

                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(50.dp)
                            .background(Color.White)
                            .border(
                                if (genderOthers) 4.dp else 2.dp,
                                color = BorderColor2,
                                shape = RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    )
                    {
                        Text(
                            text = "Others",
                            modifier = Modifier.clickable {
                                genderOthers = !genderOthers
                                selectedGender = "Others"
                                genderMale = false
                                genderFemale = false
                            },
                            style = TextStyle(
                                fontFamily = arimoFontFamily,
                                fontWeight = if (genderOthers) FontWeight.Bold else FontWeight.Black,
                                fontSize = if (genderOthers) 16.sp else 14.sp
                            )
                        )

                    }
                }

                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = "Select your DOB*",
                    textAlign = TextAlign.Start,
                    style = TextStyle(
                        fontFamily = arimoFontFamily,
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp
                    ),
                    color = SecondaryText,
                    modifier = Modifier.padding(start = 10.dp)
                )

                val formattedDate by remember {
                    derivedStateOf {
                        DateTimeFormatter
                            .ofPattern("dd-MM-yyyy")
                            .format(currentDate)
                    }
                }

                val dateDialogState = rememberMaterialDialogState()

                Spacer(modifier = Modifier.height(8.dp))

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .border(2.dp, color = BorderColor2, shape = RoundedCornerShape(10.dp))
                    .clickable {
                        dateDialogState.show()
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White),
                        verticalAlignment = Alignment.CenterVertically,

                        ) {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = formattedDate,
                            color = SecondaryText,
                            style = TextStyle(
                                fontFamily = arimoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        )

                        Spacer(modifier = Modifier)

                        Image(
                            painter = painterResource(id = R.drawable.calendar_icon_register),
                            modifier = Modifier
                                .padding(start = 190.dp)
                                .size(24.dp),
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.weight(2f))
                    }

                }

                MaterialDialog(
                    dialogState = dateDialogState,
                    buttons = {
                        positiveButton(text = "Ok")
                        negativeButton(text = "Cancel")
                    }
                ) {
                    datepicker(
                        initialDate = LocalDate.now(),
                        title = "Select your DOB",
                        allowedDateValidator = {
                            it.isBefore(LocalDate.now().plusYears(18))
                        }
                    ) {
                        currentDate = it
                    }
                }
                val context = LocalContext.current

                Spacer(modifier = Modifier.height(60.dp))

//                Text(text = R.color.splash.toString())
                if (uriImg != null) {
                    uriImg.let { uri ->
                        uploadImageToFirebase(uri, context)
                    }
                }

                Button(
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(MainColor),
//                    enabled = imageUploadCounter,
                    onClick = {
                        registrationViewModel.createUser(
                            userId,
                            "",
                            name,
                            "",
                            "",
                            imageUrl!!,
                            "Astrologer",
                            "#50A65C",
                            "25",
                            "25",
                            "25",
                            selectedGender,
                            formattedDate,
                            "",
                            "Incomplete",
                            navController
                        )
                        Toast.makeText(context, "User Created", Toast.LENGTH_SHORT).show()
                        authenticationViewModel.saveToken(userToken)
                    }
                ) {
                    Text(
                        text = "NEXT",
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontFamily = arimoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        ), color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun GenderRadioButton(
    text: String,
    selected: String,
    onClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .width(100.dp)
            .padding(8.dp)
            .border(
                width = 1.dp,
                color = if (selected == text) MaterialTheme.colorScheme.primary else Color.Gray,
                shape = RoundedCornerShape(50)
            )
            .clickable { onClick(text) }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Text(text)
    }
}



    @Composable
    fun PickImageFromGallery()
    {
        val context = LocalContext.current
        var imageUri by remember { mutableStateOf<Uri?>(null) }
    }

//                        Button(
//                            shape = RoundedCornerShape(10.dp),
//                            modifier = Modifier.fillMaxSize(),
//                            elevation = ButtonDefaults.buttonElevation(
//                                defaultElevation = 1.dp,
//                                pressedElevation = 5.dp
//                            ),
//                            border = BorderStroke(1.dp, color = BorderColor2),
//                            colors = ButtonDefaults.buttonColors(Color.White),
//                            onClick = {
//                                selectedGender = "Male"
//                            }
//                        )
//                        {
//                            Text(text = "Male",
//                                modifier = Modifier,
//                                textAlign = TextAlign.Center,
//                                style = TextStyle(
//                                    fontFamily = arimoFontFamily,
//                                    fontWeight = FontWeight.Black,
//                                    fontSize = 14.sp
//                                ),color = SecondaryText
//                            )
//                        }
@Composable
fun gender()
{
    Box(
        modifier = Modifier
            .width(120.dp)
            .height(50.dp)
            .background(Color.White)
            .border(2.dp, color = BorderColor2, shape = RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    )
    {
        Text(text = "Male",
            style = TextStyle(
                fontFamily = arimoFontFamily,
                fontWeight = FontWeight.Black,
                fontSize = 14.sp
            )
        )

    }
}

fun uploadImageToFirebase(uri: Uri?, context: Context) {

    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference
    val imageRef = storageRef.child("images/${uri!!.lastPathSegment}")

    val uploadTask = uri.let {
        imageRef.putFile(it)
    }

//    imageUploadCounter = false

    uploadTask.addOnSuccessListener {
        imageRef.downloadUrl.addOnSuccessListener {
            imageUrl = it.toString()
        }
//        imageUploadCounter = true

    }.addOnFailureListener {
    }
}

