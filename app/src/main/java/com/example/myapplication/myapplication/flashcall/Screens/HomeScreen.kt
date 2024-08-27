package com.example.myapplication.myapplication.flashcall.Screens

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
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
//import androidx.compose.foundation.layout.FlowColumnScopeInstance.align
//import androidx.compose.foundation.layout.FlowColumnScopeInstance.align
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.request.ImageResult
import coil.size.Size
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.Data.model.APIResponse
import com.example.myapplication.myapplication.flashcall.R
import com.example.myapplication.myapplication.flashcall.ViewModel.AuthenticationViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.RegistrationViewModel
import com.example.myapplication.myapplication.flashcall.bottomnav.BottomBar
import com.example.myapplication.myapplication.flashcall.bottomnav.BottomNavGraph
//import com.example.myapplication.myapplication.flashcall.bottomnav.BottomNavGraph
//import com.example.myapplication.myapplication.flashcall.bottomnav.BottomNavGraph
import com.example.myapplication.myapplication.flashcall.bottomnav.Screen
//import com.example.myapplication.myapplication.flashcall.bottomnav.BottomNavGraph
import com.example.myapplication.myapplication.flashcall.ui.theme.BorderColor2
import com.example.myapplication.myapplication.flashcall.ui.theme.BottomBackground
import com.example.myapplication.myapplication.flashcall.ui.theme.MainColor
import com.example.myapplication.myapplication.flashcall.ui.theme.PrimaryBackGround
import com.example.myapplication.myapplication.flashcall.ui.theme.SwitchColor
import com.example.myapplication.myapplication.flashcall.ui.theme.arimoFontFamily
import com.example.myapplication.myapplication.flashcall.utils.ShareComplete
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate

var uriImg : Uri? = null
var creatorUid:String = ""
var token : String = ""
var creatorUserName : String = ""
@Composable
fun HomeScreen(navController: NavController, registrationViewModel: RegistrationViewModel = hiltViewModel(), authenticationViewModel: AuthenticationViewModel= hiltViewModel())
{

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
    var videoRate by remember {
        mutableStateOf("")
    }
    var audioRate by remember {
        mutableStateOf("")
    }
    var chatRate by remember {
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
    val (firstName, lastName) = name.split(" ", limit = 2).let {
        if (it.size == 2) it else listOf(it[0], "")
    }
    val context = LocalContext.current
    if (uriImg != null) {
        uriImg?.let { uri ->
            uploadImageToFirebase(uri, context) { url ->
                imageUrl = url
//                Toast.makeText(context, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
                registrationViewModel.updateUser(
                    uid,
                    username,
                    phone ,
                    name,
                    firstName,
                    lastName,
                    imageUrl,
                    profession,
                    themeSelected,
                    "25",
                    "25",
                    "25",
                    gender,
                    dob,
                    bio,
                    "incomplete",
                    navController
                )
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
        username = userData.username ?: ""
        name = userData.fullName ?: ""
        phone = userData.phone ?: ""
        userId = userData.username ?: ""
        gender = userData.gender ?: ""
        dob = userData.dob ?: ""
        bio = userData.bio ?: ""
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

    creatorUid = uid
    creatorUserName = userId

//    val createUserState by registrationViewModel.createUserState.collectAsState()
//    Log.d("CreateUserStateHome", "$createUserState")
//    when(createUserState)
//    {
//        is APIResponse.Success->{
//            val response = (createUserState as APIResponse.Success).data
//            Log.d("UserResponse", response.toString())
//            uid = response._id
//            name = response.fullName
//            userId = response.username
//            Log.d("UserId", uid.toString())
//        }
//
//        APIResponse.Empty -> Log.e("EmptyError", "empty")
//        is APIResponse.Error -> {
//            Log.e("Error", "Error Failed")
//        }
//        APIResponse.Loading -> Log.e("Loading", "Loading")
//    }



    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier.wrapContentSize(),
        color = Color.Black
    ) {

        Scaffold(
            modifier = Modifier.wrapContentSize()
//            bottomBar = { BottomBar(navController = navController) }
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(it)
                    .background(Color.Black)
                    .verticalScroll(scrollState)
            ) {

//                BottomNavGraph(navController = navController, registrationViewModel = registrationViewModel)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .width(30.dp)
                        .height(50.dp)
                )
                {
                    Column(modifier = Modifier.fillMaxSize()) {


                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp, end = 8.dp),
                            horizontalArrangement = Arrangement.Absolute.Right,
                        ) {
                            Button(
                                colors = ButtonDefaults.buttonColors(Color.White),
                                onClick = { navController.navigate(ScreenRoutes.EditScreen.route) }
                            ) {
                                Text(
                                    text = "edit profile",
                                    style = TextStyle(
                                        fontFamily = arimoFontFamily,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 13.sp,
                                        color = Color.Black
                                    )
                                )
                            }
                        }

                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    )
                    {
                        if (profilePic==""){
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


                        }

                        else {

                            Log.d("ProfileImageofUser", "$profilePic")
                            ImageFromUrl(imageUrl = profilePic!!)
                        }
                    }

                }


                    Text(
                        text = name,
                        color = Color.White,
                        style = TextStyle(
                            fontFamily = arimoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                        ),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )


                Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = userId,
                        color = Color.White,
                        style = TextStyle(
                            fontFamily = arimoFontFamily,
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                        ),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                Spacer(modifier = Modifier.height(20.dp))

                Box(modifier = Modifier.fillMaxSize()){
                    HomeScreenBottom(navController, username)
                }
            }
        }
    }
}


@Composable
fun HomeScreenBottom(homeNavController: NavController, username: String)
{
    var showShareDialog by remember { mutableStateOf(true) }


    Surface(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight(),
        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxHeight()
                .background(
                    PrimaryBackGround,
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                )
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .fillMaxHeight()
                    .padding(10.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {

//                    var createdAt = System.currentTimeMillis()
//                    Text(text = "$createdAt")

                    CopyBar(homeNavController, username = username)
//                    if (showShareDialog) {
//                        ShareTextButton(
//                            textToShare = "https://www.flashcall.me/nitra-sahgal-55-consultant",
//                            homeNavController = homeNavController
//                        )
//                    }


                }

                Spacer(modifier = Modifier.height(20.dp))

                WalletBar(navController = homeNavController)

                Spacer(modifier = Modifier.height(20.dp))

                ServicesSection()

                Spacer(modifier = Modifier.height(40.dp))

                DemoText()

                Spacer(modifier = Modifier.height(60.dp))


            }
        }
    }
}

@Composable
fun CopyBar(homeNavController: NavController, username: String)
{
    var copyText by remember {
        mutableStateOf("https://www.flashcall.vercel.app/expert/$creatorUserName/$creatorUid")
    }
    var showShareDialog by remember { mutableStateOf(true) }


    var context = LocalContext.current
    Row(
        modifier = Modifier
            .wrapContentWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(24.dp))
    )
    {
        Box(modifier = Modifier
            .wrapContentSize()
            .background(color = Color.White)
            .border(1.dp, BorderColor2, shape = RoundedCornerShape(24.dp)))

        {
            Row(modifier = Modifier.wrapContentSize())
            {
                Image(painter = painterResource(id = R.drawable.link_icon),
                    modifier = Modifier.padding(16.dp),
                    contentDescription = null)

                Text(text = copyText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .width(200.dp)
                        .align(Alignment.CenterVertically),
                    style = TextStyle(
                        fontFamily = arimoFontFamily,
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                )

                Image(painter = painterResource(id = R.drawable.copy_icon),
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            copyToClipboard(
                                context = context,
                                copyText
                            )
                        },
                    contentDescription = null)


            }
        }

        if (showShareDialog) {
            ShareTextButton(
                textToShare = "https://www.flashcall.me/nitra-sahgal-55-consultant",
                homeNavController = homeNavController,
                username = username
            )
        }
    }
}

@Composable
fun WalletBar(navController: NavController)
{


    Box(modifier = Modifier
        .fillMaxWidth()
        .height(76.dp)
        .background(color = Color.White)
        .border(1.dp, BorderColor2, shape = RoundedCornerShape(10.dp))
    ){
        Row(modifier = Modifier.fillMaxSize())
        {
            Image(painter = painterResource(id = R.drawable.wallet_icon),
                modifier = Modifier.padding(16.dp),
                contentDescription = null)

            Column(modifier = Modifier.fillMaxHeight()) {

                Text(text = "Today's Earning",
                    modifier = Modifier.padding(top = 10.dp),
                    style = TextStyle(
                    fontFamily = arimoFontFamily,
                    fontWeight = FontWeight.Black,
                    fontSize = 10.sp,
                    color = Color.Black
                    )
                )

                Row {
                    Text(text = "Rs.",
                        modifier = Modifier.padding(top = 8.dp),
                        style = TextStyle(
                            fontFamily = arimoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                    )
                    Text(text = "10000",
                        modifier = Modifier.padding(top = 8.dp),
                        style = TextStyle(
                            fontFamily = arimoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                    )
                }

            }

            Spacer(Modifier.weight(1f))
            Button(modifier = Modifier
                .padding(top = 8.dp, end = 10.dp)
                .width(140.dp)
                .height(55.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MainColor,
                    contentColor = Color.White
                ),
                onClick = {
                    navController.navigate(ScreenRoutes.WalletScreen.route)
                }
            ) {

                Text(text = "View Wallet",
                    style = TextStyle(
                        fontFamily = arimoFontFamily,
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp,
                    ))

            }
        }
    }
}

@Composable
fun ServicesSection() {
    var serviceSelected by remember { mutableStateOf(false) }
    var audioService by remember { mutableStateOf(true) }
    var videoService by remember { mutableStateOf(true) }
    var chatService by remember { mutableStateOf(true) }

    var audioPrice by remember { mutableStateOf("25") }
    var chatPrice by remember { mutableStateOf("25") }
    var videoPrice by remember { mutableStateOf("25") }

    var isDialogOpen by remember { mutableStateOf(false) }
    var selectedService by remember { mutableStateOf("") }
    var priceToEdit by remember { mutableStateOf("") }

    // Dynamic text color based on the service toggle state
    val textColor = if (serviceSelected) Color.Black else Color.Gray

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .background(color = Color.White)
            .border(1.dp, BorderColor2, shape = RoundedCornerShape(10.dp))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .padding(10.dp)
            ) {
                Text(
                    text = "My Services",
                    modifier = Modifier.padding(start = 5.dp),
                    style = TextStyle(
                        fontFamily = arimoFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = serviceSelected,
                    onCheckedChange = { serviceSelected = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = MainColor,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = SwitchColor
                    ),
                    modifier = Modifier
                        .width(50.dp)
                        .padding(top = 5.dp)
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(start = 10.dp, end = 10.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {

                // Video Call Service
                ServiceRow(
                    serviceName = "Video Call",
                    servicePrice = videoPrice,
                    serviceEnabled = videoService,
                    onEditClick = {
                        selectedService = "Video"
                        priceToEdit = videoPrice
                        isDialogOpen = true
                    },
                    onCheckedChange = {
                        if (serviceSelected) {
                            videoService = it
                        }
                    },
                    serviceSelected = serviceSelected,
                    textColor = textColor
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Audio Call Service
                ServiceRow(
                    serviceName = "Audio Call",
                    servicePrice = audioPrice,
                    serviceEnabled = audioService,
                    onEditClick = {
                        selectedService = "Audio"
                        priceToEdit = audioPrice
                        isDialogOpen = true
                    },
                    onCheckedChange = {
                        if (serviceSelected) {
                            audioService = it
                        }
                    },
                    serviceSelected = serviceSelected,
                    textColor = textColor
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Chat Service
                ServiceRow(
                    serviceName = "Chat",
                    servicePrice = chatPrice,
                    serviceEnabled = chatService,
                    onEditClick = {
                        selectedService = "Chat"
                        priceToEdit = chatPrice
                        isDialogOpen = true
                    },
                    onCheckedChange = {
                        if (serviceSelected) {
                            chatService = it
                        }
                    },
                    serviceSelected = serviceSelected,
                    textColor = textColor
                )
            }
        }
    }

    if (isDialogOpen) {
        EditPriceDialog(
            videoPrice = videoPrice,
            audioPrice = audioPrice,
            chatPrice = chatPrice,
            onDismiss = { isDialogOpen = false },
            onConfirm = { newVideoPrice, newAudioPrice, newChatPrice ->
                videoPrice = newVideoPrice
                audioPrice = newAudioPrice
                chatPrice = newChatPrice
                isDialogOpen = false
            }
        )
    }
}

@Composable
fun ServiceRow(
    serviceName: String,
    servicePrice: String,
    serviceEnabled: Boolean,
    onEditClick: () -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    serviceSelected: Boolean,
    textColor: Color // Dynamic text color based on main toggle state
) {
    // Text and icon color should be grey if the service is disabled
    val rowTextColor = if (serviceEnabled && serviceSelected) textColor else Color.Gray
    val iconAlpha = if (serviceEnabled && serviceSelected) 1f else 0.5f

    Row(
        modifier = Modifier.height(50.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxHeight()
        ) {
            Text(
                text = serviceName,
                modifier = Modifier.clickable(enabled = serviceEnabled && serviceSelected) { onEditClick() },
                style = TextStyle(
                    fontFamily = arimoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = rowTextColor // Apply dynamic color
                )
            )
            Spacer(modifier = Modifier.height(5.dp))
            Row {
                Text(
                    text = "Rs. $servicePrice/min",
                    style = TextStyle(
                        fontFamily = arimoFontFamily,
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        color = rowTextColor // Apply dynamic color
                    )
                )

                Image(
                    painter = painterResource(id = R.drawable.edit),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .size(16.dp)
                        .clickable(enabled = serviceEnabled && serviceSelected) { onEditClick() }
                        .alpha(iconAlpha) // Reduce opacity if disabled
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Switch(
            checked = serviceEnabled,
            onCheckedChange = { onCheckedChange(it) },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = MainColor,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = SwitchColor
            ),
            modifier = Modifier
                .width(50.dp)
                .padding(bottom = 15.dp),
            enabled = serviceSelected
        )
    }
}



@Composable
fun EditPriceDialog(
    videoPrice: String,
    audioPrice: String,
    chatPrice: String,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    var newVideoPrice by remember { mutableStateOf(videoPrice) }
    var newAudioPrice by remember { mutableStateOf(audioPrice) }
    var newChatPrice by remember { mutableStateOf(chatPrice) }

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Price",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                PriceInputRow(
                    serviceName = "Video Call",
                    price = newVideoPrice,
                    onPriceChange = { newVideoPrice = it }
                )

                PriceInputRow(
                    serviceName = "Audio Call",
                    price = newAudioPrice,
                    onPriceChange = { newAudioPrice = it }
                )

                PriceInputRow(
                    serviceName = "Chat",
                    price = newChatPrice,
                    onPriceChange = { newChatPrice = it }
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(
                            text = "Cancel",
                            style = TextStyle(
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        )
                    }

                    Button(
                        onClick = {
                            onConfirm(newVideoPrice, newAudioPrice, newChatPrice)
                        },
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                             Color(0xFF00A862) // Green color for Save button
                        )
                    ) {
                        Text(
                            text = "Save",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PriceInputRow(
    serviceName: String,
    price: String,
    onPriceChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$serviceName\nper minute",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
            )
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Rs.")
            Spacer(modifier = Modifier.width(4.dp))
            OutlinedTextField(
                value = price,
                onValueChange = onPriceChange,
                modifier = Modifier.width(80.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
            )
        }
    }
}


@Composable
fun TutorialSlide() {

}

@Composable
fun CustomToggleButton(selected:Boolean, onChangeValue:(Boolean)->Unit) {
    Card(
        modifier = Modifier.width(50.dp),
        elevation = CardDefaults.cardElevation(0.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(modifier = Modifier
            .background(
                if (selected) MainColor else Color.White
            )
            .clickable {
                onChangeValue(!selected)
            },
            contentAlignment = if(selected) Alignment.TopEnd else Alignment.TopStart
        ){
            CustomCheck(Modifier.padding(5.dp))
        }
    }
}

@Composable
fun CustomCheck(modifier: Modifier) {

    Card(
        modifier = modifier.size(20.dp),
        elevation = CardDefaults.cardElevation(0.dp),
        shape = CircleShape
    ) {
        Box(modifier = Modifier.background(Color.White))
    }

}


fun copyToClipboard(
    context : Context,
    copyText : String
){
    val clipBoard = context.getSystemService(
        Context.CLIPBOARD_SERVICE
    ) as ClipboardManager

    val clip = ClipData.newPlainText(
        "Copied Text",
        copyText
    )
    clipBoard.setPrimaryClip(clip)
}

@Composable
fun ImageFromUrl(imageUrl: String) {
    // Use the AsyncImage directly for simplicity
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = null,
        modifier = Modifier
            .size(120.dp)  // Ensure a consistent size
            .clip(CircleShape),  // Clip to a circle
        contentScale = ContentScale.Crop  // Crop the image to fit within the circle
    )
}


@Composable
fun shareLink(url : String) {
    val context = LocalContext.current

    val sendIntent : Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, url)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}

@Composable
fun ShareTextButton(textToShare: String,homeNavController: NavController, username: String) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {result->
        if(result.resultCode == Activity.RESULT_OK)
        {

        }

    }

    var shareText by remember {
        mutableStateOf("https://app.flashcall.me/creator/$username")
    }
    Image(painter = painterResource(id = R.drawable.share_icon),
        modifier = Modifier
            .padding(start = 2.dp, top = 2.dp)
            .size(42.dp)
            .clickable {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "$shareText"
                    )
                    type = "text/plain"
                }


                val shareIntent = Intent.createChooser(sendIntent, null)
                launcher.launch(shareIntent)
            },
        contentDescription = null
    )
}

@Composable
fun DemoText()
{
    Box(modifier = Modifier.fillMaxWidth()){

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Column(){
                Text(text = "If you are Interested in Learning how to create an ",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = TextStyle(
//                        textDecoration = TextDecoration.Underline,
                        color = Color.Black,
//                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    ))
                Text(text = "account on Flashcall and how it works.",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = TextStyle(
//                        textDecoration = TextDecoration.Underline,
                    color = Color.Black,
//                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                ))
                Text(
                    text = "please click here",

                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable {

                        },
                    style = TextStyle(
                        textDecoration = TextDecoration.Underline,
                        color = MainColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                )
            }
        }
    }
}
