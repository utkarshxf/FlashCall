package com.example.myapplication.myapplication.flashcall.Screens

import android.annotation.SuppressLint
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
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.Data.model.APIResponse
import com.example.myapplication.myapplication.flashcall.Data.model.LinkData
import com.example.myapplication.myapplication.flashcall.Data.model.firestore.UserServicesResponse
import com.example.myapplication.myapplication.flashcall.R
import com.example.myapplication.myapplication.flashcall.Screens.common.CircularLoaderButton
import com.example.myapplication.myapplication.flashcall.Screens.common.WideSwitch
import com.example.myapplication.myapplication.flashcall.ViewModel.AuthenticationViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.RegistrationViewModel
import com.example.myapplication.myapplication.flashcall.ui.theme.BorderColor
import com.example.myapplication.myapplication.flashcall.ui.theme.BorderColor2
import com.example.myapplication.myapplication.flashcall.ui.theme.MainColor
import com.example.myapplication.myapplication.flashcall.ui.theme.PrimaryBackGround
import com.example.myapplication.myapplication.flashcall.ui.theme.PrimaryText
import com.example.myapplication.myapplication.flashcall.ui.theme.SecondaryBackGround
import com.example.myapplication.myapplication.flashcall.ui.theme.SecondaryText
import com.example.myapplication.myapplication.flashcall.ui.theme.SwitchColor
import com.example.myapplication.myapplication.flashcall.ui.theme.helveticaFontFamily
import com.example.myapplication.myapplication.flashcall.utils.capitalizeAfterSpace
import com.jetpack.draganddroplist.DragDropList
import com.jetpack.draganddroplist.move

//var uriImg: Uri? = null
var creatorUid: String = ""
var token: String = ""
var creatorUserName: String = ""

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavController,
    registrationViewModel: RegistrationViewModel = hiltViewModel(),
    authenticationViewModel: AuthenticationViewModel = hiltViewModel()
) {
    var uid by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var userId by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var profession by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var themeSelected by remember { mutableStateOf("") }
    var profilePic by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }

    val (firstName, lastName) = name.split(" ", limit = 2).let {
        if (it.size == 2) it else listOf(it[0], "")
    }
    val context = LocalContext.current


//    if (uriImg != null) {
//        uriImg?.let { uri ->
//            uploadImageToFirebase(uri, context) { url ->
//                imageUrl = url
//                registrationViewModel.updateUser(
//                    userId = uid,
//                    username = username,
//                    phone = phone,
//                    fullName = name,
//                    firstName = firstName,
//                    lastName = lastName,
//                    photo = imageUrl,
//                    profession = profession,
//                    themeSelected = themeSelected,
//                    gender = gender,
//                    dob = dob,
//                    bio = bio,
//                    navController = navController
//                ){}
//            }
//        }
//    }
    val createUserState1 by registrationViewModel.createUserState.collectAsState()
    val userData = authenticationViewModel.getUserFromPreferences(context)
    if (userData != null) {
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
    } else {
        when (createUserState1) {
            is APIResponse.Success -> {

                val response = (createUserState1 as APIResponse.Success).data
                Log.d("UserResponse", response._id)
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
    val scrollState = rememberScrollState()
    Scaffold(
        modifier = Modifier.wrapContentSize(), containerColor = Color.Black
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(Color.Black)
                .verticalScroll(scrollState)
        ) {

//                BottomNavGraph(navController = navController, registrationViewModel = registrationViewModel)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .width(30.dp)
                    .height(50.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, end = 8.dp),
                        horizontalArrangement = Arrangement.Absolute.Right,
                    ) {
                        Button(colors = ButtonDefaults.buttonColors(Color.White), onClick = {
                            navController.navigate(ScreenRoutes.EditScreen.route) {
                                popUpTo(ScreenRoutes.MainScreen.route) { inclusive = true }
                            }
                        }) {
                            Text(
                                text = "edit profile", style = TextStyle(
                                    fontFamily = helveticaFontFamily,
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
                ) {
                    if (profilePic == "") {
                        val imageUri = rememberSaveable {
                            mutableStateOf("")
                        }

                        val painter =
                            rememberAsyncImagePainter(imageUri.value.ifEmpty { R.drawable.profile_picture_holder })

                        val launcher = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.GetContent()
                        ) { uri: Uri? ->
                            uri?.let {
//                                    uriImg = it
                                imageUri.value = it.toString()
                            }
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp)
                        ) {
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

                            Box(modifier = Modifier.padding(start = 200.dp, top = 70.dp)) {
                                Image(painter = painterResource(id = R.drawable.edit_icon),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .clickable {
                                            launcher.launch("image/*")

                                        })
                            }

                        }


                    } else {

                        Log.d("ProfileImageofUser", "$profilePic")
                        ImageFromUrl(imageUrl = profilePic)
                    }
                }

            }


            Text(
                text = name, color = Color.White, style = TextStyle(
                    fontFamily = helveticaFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                ), modifier = Modifier.align(Alignment.CenterHorizontally)
            )


            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "@$userId", color = Color.White, style = TextStyle(
                    fontFamily = helveticaFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                ), modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Box(modifier = Modifier.fillMaxSize()) {
                HomeScreenBottom(navController, username)
            }
        }
    }
}


@Composable
fun HomeScreenBottom(
    homeNavController: NavController,
    username: String,
    viewModel: RegistrationViewModel = hiltViewModel()
) {
    var showShareDialog by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = Unit) {
        viewModel.getTodaysWalletBalance()
    }

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
                    PrimaryBackGround, shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .fillMaxHeight()
                    .padding(10.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    CopyBar(viewModel)
                }

                Spacer(modifier = Modifier.height(20.dp))

                WalletBar(navController = homeNavController, viewModel)

                Spacer(modifier = Modifier.height(20.dp))

                ServicesSection()

                Spacer(modifier = Modifier.height(20.dp))

                LaunchedEffect(key1 = Unit) {
                    viewModel.getAddedAdditionalLinks()
                }


                val addAditionalLinkState = viewModel.addAditionalLinkState
                val editAdditionalLinkState = viewModel.editAdditionalLinkState

                var additionalLinksList = addAditionalLinkState.linksList

                if (!additionalLinksList.isNullOrEmpty()) {
                    DragDropList(
                        items = viewModel.draggableList,
                        onMove = { fromIndex, toIndex ->
                            viewModel.draggableList.move(
                                fromIndex,
                                toIndex
                            )
                        },
                        viewModel = viewModel
                    )
                }

                if (editAdditionalLinkState.editingLayout.showEditingLayout && editAdditionalLinkState.editingLayout.index > -1) {
                    EditLinkLayout(
                        viewModel,
                        model = additionalLinksList?.get(editAdditionalLinkState.editingLayout.index)
                    )
                }


                if (addAditionalLinkState.showAddLinkLayout) {
                    AddLinkLayout {
                        viewModel.showLayoutForAddLinks(false)
                    }
                } else {
                    addExtraLink(
                        modifier = Modifier
                            .height(84.dp)
                            .fillMaxWidth()
                            .padding(vertical = 10.dp, horizontal = 5.dp)
                            .background(Color.White, shape = RoundedCornerShape(8.dp))
                            .clickable {
                                viewModel.showLayoutForAddLinks(true)
                            },
                        borderColor = BorderColor2,
                        dashLength = 10f,
                        gapLength = 10f,
                        cornerRadius = 16f,
                        borderWidth = 4f
                    ) {
                        Row(
                            modifier = Modifier
                                .background(color = Color.White, shape = RoundedCornerShape(16.dp)),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.add_circle_outline_24dp_1),
                                contentDescription = "addIcon",
                                tint = Color.Black
                            )
                            Text(
                                text = "Add Your Link",
                                color = Color.Black,
                                modifier = Modifier.padding(start = 10.dp),
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    fontFamily = helveticaFontFamily,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                DemoText(viewModel)

                Spacer(modifier = Modifier.height(60.dp))


            }
        }
    }
}


@Composable
fun addExtraLink(
    modifier: Modifier = Modifier,
    borderColor: Color = Color.Red,
    dashLength: Float = 10f,
    gapLength: Float = 10f,
    cornerRadius: Float = 16f,
    borderWidth: Float = 4f,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(cornerRadius.dp)
            ),
        contentAlignment = Alignment.Center

    ) {
        // Draw the dashed border
        Canvas(modifier = Modifier.fillMaxSize()) {
            val pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashLength, gapLength), 0f)
            drawRoundRect(
                color = borderColor,
                size = size,
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius),
                style = Stroke(
                    width = borderWidth,
                    pathEffect = pathEffect,
                    cap = StrokeCap.Round
                )
            )
        }
        content()
    }
}

@Composable
fun CopyBar(viewModel: RegistrationViewModel) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getShareLink()
    }
    var shareLinkState = viewModel.shareLinkState

    var copyText by remember {
        mutableStateOf("")
    }
    var myBio by remember {
        mutableStateOf(viewModel.getMyBio())
    }
    copyText = shareLinkState.shareLink

    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(50.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(color = Color.White)
                .border(1.dp, BorderColor2, shape = RoundedCornerShape(24.dp))
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.link_icon),
                    modifier = Modifier.padding(start = 16.dp, end = 8.dp),
                    contentDescription = "Link Icon"
                )

                Text(
                    text = copyText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    style = TextStyle(
                        fontFamily = helveticaFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                )

                Image(
                    painter = painterResource(id = R.drawable.copy_icon),
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clickable {
                            copyToClipboard(context = context, copyText)
                            Toast.makeText(context,"Copied",Toast.LENGTH_SHORT).show()
                        },
                    contentDescription = "Copy Icon"
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        ShareTextButton(
            shareLink = copyText, bio = myBio
        )
    }
}

@Composable
fun WalletBar(navController: NavController, viewModel: RegistrationViewModel) {
    var walletBalance by remember {
        mutableStateOf(0)
    }
    walletBalance = viewModel.todaysWalletBalanceState.todaysBalance

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(color = Color.White, shape = RoundedCornerShape(10.dp))
            .border(1.dp, BorderColor2, shape = RoundedCornerShape(10.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp), // Padding to add some space between borders and content
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Wallet icon
            walletIcon()


            // Column containing text for "Today's Earning" and balance
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 7.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {

                Text(
                    text = "Today's Earning",
                    modifier = Modifier.padding(bottom = 8.dp),
                    style = TextStyle(
                        fontFamily = helveticaFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Rs. $walletBalance",
                        style = TextStyle(
                            fontFamily = helveticaFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.Black
                        ),
                        modifier = Modifier.padding(end = 2.dp)
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            Button(
                modifier = Modifier,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MainColor,
                    contentColor = Color.White
                ),
                onClick = {
                    navController.navigate(ScreenRoutes.WalletScreen.route)
                }
            ) {
                Text(
                    text = "View Wallet",
                    style = TextStyle(
                        fontFamily = helveticaFontFamily,
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp,
                    ),
                    maxLines = 1
                )
            }
        }
    }
}


@Composable
fun ServicesSection(
    registrationViewModel: RegistrationViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val serviceData = registrationViewModel.serviceState.collectAsState()
    var serviceSelected by remember { mutableStateOf(false) }
    var audioService by remember { mutableStateOf(true) }
    var videoService by remember { mutableStateOf(true) }
    var chatService by remember { mutableStateOf(true) }

    var audioPrice by remember { mutableStateOf("") }
    var chatPrice by remember { mutableStateOf("") }
    var videoPrice by remember { mutableStateOf("") }

    var isDialogOpen by remember { mutableStateOf(false) }
    var selectedService by remember { mutableStateOf("") }
    var priceToEdit by remember { mutableStateOf("") }
    val textColor = if (serviceSelected) Color.Black else Color.Gray
    LaunchedEffect(Unit) {
        registrationViewModel.getAllServiceData()
    }
    if (serviceData.value is APIResponse.Success) {
        LaunchedEffect(Unit) {
            val response = (serviceData.value as APIResponse.Success<UserServicesResponse>).data
            response.prices.let {
                videoPrice = it.videoCall.toString()
                audioPrice = it.audioCall.toString()
                chatPrice = it.chat.toString()
            }
            response.services.let {
                videoService = it.videoCall
                audioService = it.audioCall
                chatService = it.chat
            }

            serviceSelected = response.services.myServices
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White, shape = RoundedCornerShape(10.dp))
            .border(1.dp, BorderColor2, shape = RoundedCornerShape(10.dp))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "My Services",
                    style = TextStyle(
                        fontFamily = helveticaFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                WideSwitch(
                    checked = serviceSelected, onCheckedChange = {


                        serviceSelected = it
                        registrationViewModel.updateServices(userId = creatorUid, masterToggle = it)
                        registrationViewModel.changeUserStatus(it)

                        videoService = it
                        registrationViewModel.updateServices(
                            userId = creatorUid, servicesVideo = it
                        )
                        audioService = it
                        registrationViewModel.updateServices(
                            userId = creatorUid, servicesAudio = it
                        )
                        chatService = it
                        registrationViewModel.updateServices(
                            userId = creatorUid, servicesChat = it
                        )
                    }

                )
            }
            Spacer(modifier = Modifier.height(5.dp))

            HorizontalDivider(
                thickness = 1.dp, modifier = Modifier.padding(start = 10.dp, end = 10.dp)
            )

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
                            if (it == false && audioService == false && chatService == false) {
                                serviceSelected = it
                                registrationViewModel.updateServices(
                                    userId = creatorUid,
                                    masterToggle = it
                                )
                                registrationViewModel.changeUserStatus(it)
                            }
                            videoService = it
                            registrationViewModel.updateServices(
                                userId = creatorUid, servicesVideo = it
                            )

                        } else {
                            serviceSelected = it
                            registrationViewModel.updateServices(
                                userId = creatorUid,
                                masterToggle = it
                            )
                            registrationViewModel.changeUserStatus(it)
                            videoService = it
                            registrationViewModel.updateServices(
                                userId = creatorUid, servicesVideo = it
                            )
                        }
                    },
//                    serviceSelected = serviceSelected,
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
                            if (it == false && videoService == false && chatService == false) {
                                serviceSelected = it
                                registrationViewModel.updateServices(
                                    userId = creatorUid,
                                    masterToggle = it
                                )
                                registrationViewModel.changeUserStatus(it)
                            }
                            audioService = it
                            registrationViewModel.updateServices(
                                userId = creatorUid, servicesAudio = it
                            )
                        } else {
                            serviceSelected = it
                            registrationViewModel.updateServices(
                                userId = creatorUid,
                                masterToggle = it
                            )
                            registrationViewModel.changeUserStatus(it)
                            audioService = it
                            registrationViewModel.updateServices(
                                userId = creatorUid, servicesAudio = it
                            )
                        }
                    },
//                    serviceSelected = serviceSelected,
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
                            if (it == false && audioService == false && videoService == false) {
                                serviceSelected = it
                                registrationViewModel.updateServices(
                                    userId = creatorUid,
                                    masterToggle = it
                                )
                                registrationViewModel.changeUserStatus(it)
                            }
                            chatService = it
                            registrationViewModel.updateServices(
                                userId = creatorUid, servicesChat = it
                            )
                        } else {
                            serviceSelected = it
                            registrationViewModel.updateServices(
                                userId = creatorUid,
                                masterToggle = it
                            )
                            registrationViewModel.changeUserStatus(it)
                            chatService = it
                            registrationViewModel.updateServices(
                                userId = creatorUid, servicesChat = it
                            )
                        }
                    },
                    textColor = textColor
                )
            }
        }
    }

    if (isDialogOpen) {
        EditPriceDialog(videoPrice = videoPrice,
            audioPrice = audioPrice,
            chatPrice = chatPrice,
            onDismiss = { isDialogOpen = false },
            onConfirm = { newVideoPrice, newAudioPrice, newChatPrice ->
                videoPrice = newVideoPrice
                audioPrice = newAudioPrice
                chatPrice = newChatPrice
                isDialogOpen = false
                registrationViewModel.updateServices(
                    userId = creatorUid,
                    videoRate = videoPrice,
                    audioRate = audioPrice,
                    chatRate = chatPrice
                )
            })

    }
}

@Composable
fun ServiceRow(
    serviceName: String,
    servicePrice: String,
    serviceEnabled: Boolean,
    onEditClick: () -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    textColor: Color // Dynamic text color based on main toggle state
) {

    val rowTextColor = if (serviceEnabled) textColor else Color.Gray
    // Text and icon color should be grey if the service is disabled
//    val rowTextColor = if (serviceEnabled && serviceSelected) textColor else Color.Gray
//    val iconAlpha = if (serviceEnabled && serviceSelected) 1f else 0.5f


    val iconAlpha = if (serviceEnabled) 1f else 0.5f

    Row(
        modifier = Modifier.height(50.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxHeight()
        ) {
            Text(
                text = serviceName,
                modifier = Modifier.clickable(enabled = serviceEnabled) { onEditClick() },
                style = TextStyle(
                    fontFamily = helveticaFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = rowTextColor // Apply dynamic color
                )
            )
            Spacer(modifier = Modifier.height(5.dp))
            Row {
                Text(
                    text = "Rs. $servicePrice/min", style = TextStyle(
                        fontFamily = helveticaFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = rowTextColor
                    )
                )

                Image(painter = painterResource(id = R.drawable.edit),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .size(16.dp)
                        .clickable(enabled = serviceEnabled) { onEditClick() }
                        .alpha(iconAlpha) // Reduce opacity if disabled
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        WideSwitch(
            checked = serviceEnabled,
            onCheckedChange = { onCheckedChange(it) },
            modifier = Modifier
                .padding(bottom = 15.dp)
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

    var isError by remember {
        mutableStateOf("")
    }

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(12.dp), color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Price", style = TextStyle(
                            fontFamily = helveticaFontFamily,
                            fontWeight = FontWeight.Bold, fontSize = 18.sp
                        ), color = Color.Black, modifier = Modifier.padding(12.dp)
                    )
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(color = BorderColor)
                        .padding(vertical = 10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                PriceInputRow(
                    serviceName = "Video Call",
                    price = newVideoPrice,
                    onPriceChange = { newVideoPrice = it }
                )
                PriceInputRow(serviceName = "Audio Call",
                    price = newAudioPrice,
                    onPriceChange = { newAudioPrice = it })

                PriceInputRow(serviceName = "Chat",
                    price = newChatPrice,
                    onPriceChange = { newChatPrice = it })


                if (!isError.isEmpty()) {
                    Text(
                        text = "Error: ${isError}",
                        color = Color.Red,
                        style = TextStyle(fontSize = 12.sp)
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(
                            text = "Cancel", style = TextStyle(
                                color = Color.Gray, fontSize = 16.sp
                            )
                        )
                    }

                    Button(
                        onClick = {
                            val videoPriceInt = newVideoPrice.toIntOrNull()
                            val audioPriceInt = newAudioPrice.toIntOrNull()
                            val chatPriceInt = newChatPrice.toIntOrNull()

                            val minPrice = 10

                            if (videoPriceInt != null && audioPriceInt != null && chatPriceInt != null) {
                                if (videoPriceInt >= minPrice && audioPriceInt >= minPrice && chatPriceInt >= minPrice) {
                                    onConfirm(newVideoPrice, newAudioPrice, newChatPrice)
                                } else {
                                    if (videoPriceInt < minPrice) {
                                        isError = "video call price should be more than 10 RS"
                                    }
                                    if (audioPriceInt < minPrice) {
                                        isError = "audio call price should be more than 10 RS"
                                    }
                                    if (chatPriceInt < minPrice) {
                                        isError = "chat price should be more than 10 RS"
                                    }
                                }
                            } else {
                                isError = "price should be more than 10 RS"
                            }
                        }, shape = RoundedCornerShape(50), colors = ButtonDefaults.buttonColors(
                            Color(0xFF00A862) // Green color for Save button
                        )
                    ) {
                        Text(
                            text = "Save", color = Color.White, fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PriceInputRow(
    serviceName: String, price: String, onPriceChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "$serviceName", style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = helveticaFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                )
            )
            Text(
                text = "per minute", style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = helveticaFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = SecondaryText,
                )
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 7.dp)) {
            Text(text = "Rs.", fontFamily = helveticaFontFamily, fontWeight = FontWeight.Normal)
            Spacer(modifier = Modifier.width(4.dp))


            Box(
                modifier = Modifier
                    .height(40.dp)
                    .width(65.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(6.dp))
                    .border(width = 1.dp, color = BorderColor, shape = RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                BasicTextField(
                    value = price,
                    onValueChange = onPriceChange,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 10.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center, fontSize = 20.sp),

                )
            }

        }
    }
}

@Composable
fun CustomToggleButton(selected: Boolean, onChangeValue: (Boolean) -> Unit) {
    Card(
        modifier = Modifier.width(50.dp),
        elevation = CardDefaults.cardElevation(0.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    if (selected) MainColor else Color.White
                )
                .clickable {
                    onChangeValue(!selected)
                }, contentAlignment = if (selected) Alignment.TopEnd else Alignment.TopStart
        ) {
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
    context: Context, copyText: String
) {
    val clipBoard = context.getSystemService(
        Context.CLIPBOARD_SERVICE
    ) as ClipboardManager

    val clip = ClipData.newPlainText(
        "Copied Text", copyText
    )
    clipBoard.setPrimaryClip(clip)
}

@Composable
fun ImageFromUrl(imageUrl: String) {
    // Use the AsyncImage directly for simplicity
    if (imageUrl.isNotEmpty()) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(imageUrl).crossfade(true)
                .build(),
            contentDescription = null,
            placeholder = painterResource(id = R.drawable.profile_picture_holder),
            modifier = Modifier
                .size(120.dp)  // Ensure a consistent size
                .clip(CircleShape)
                .border(1.dp, color = MainColor, shape = CircleShape),  // Clip to a circle
            contentScale = ContentScale.Crop  // Crop the image to fit within the circle
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.profile_picture_holder),
            contentDescription = "profile",
            modifier = Modifier
                .size(120.dp)  // Ensure a consistent size
                .clip(CircleShape)
                .border(1.dp, color = MainColor, shape = CircleShape),  // Clip to a circle
            contentScale = ContentScale.Crop
        )
    }
}


@Composable
fun shareLink(url: String) {
    val context = LocalContext.current

    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, url)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}

@Composable
fun ShareTextButton(shareLink: String, bio: String) {
    var sharingContent =
        "Hi 👋\n\nYou can use the below link to consult with me through Video Call, Audio Call or Chat. \n\nLink: $shareLink\n\n"

    if (bio.isNotEmpty()) {
        sharingContent += "About me: $bio"
    }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

            }

        }

    Image(
        painter = painterResource(id = R.drawable.share_icon),
        modifier = Modifier
            .padding(start = 2.dp, top = 2.dp)
            .size(42.dp)
            .clickable {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(
                        Intent.EXTRA_TEXT, sharingContent
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
fun DemoText(viewModel: RegistrationViewModel) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getUserAssistanceLink()
    }
    var linksState = viewModel.userAssistanceLinkState
    var userAssistanceLink by remember {
        mutableStateOf("")
    }
    var userAssistanceLinkDesc by remember {
        mutableStateOf("")
    }
    userAssistanceLink = linksState.linkUrl + ""
    userAssistanceLinkDesc = linksState.linkDesc + ""

    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxWidth()) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column {
                Text(
                    text = userAssistanceLinkDesc,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                )
                Text(
                    text = "please click here",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            val urlIntent =
                                Intent(Intent.ACTION_VIEW, Uri.parse(userAssistanceLink + ""))
                            context.startActivity(urlIntent)
                        }, style = TextStyle(
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

@Composable
fun walletIcon() {
    Box(modifier = Modifier.padding(start = 5.dp), contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(shape = RoundedCornerShape(6.dp))
                .background(
                    Color(
                        0x1F50A65C
                    )
                )
        )

        Image(
            painter = painterResource(id = R.drawable.wallet_icon),
            modifier = Modifier.padding(16.dp),
            contentDescription = null
        )
    }
}

@Composable
fun AddedLinkLayout(item: LinkData, isActive: () -> Unit, edit: () -> Unit, delete: () -> Unit) {
    var mDisplayMenu by remember { mutableStateOf(false) }

    Box(modifier = Modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                .border(width = 1.dp, color = BorderColor2, shape = RoundedCornerShape(8.dp)),
            verticalAlignment = Alignment.CenterVertically

        ) {

            Icon(
                painter = painterResource(id = R.drawable.drag_indicator_24dp),
                contentDescription = "",
                modifier = Modifier.padding(start = 10.dp)
            )
            Text(
                text = item.title ?: "default",
                color = Color.Black,
                fontSize = 16.sp,
                style = TextStyle(
                    fontFamily = helveticaFontFamily,
                    fontWeight = FontWeight.Normal,
                ),
                modifier = Modifier
                    .padding(start = 10.dp)
                    .weight(1f)
            )


            Switch(
                checked = item.isActive ?: true, onCheckedChange = {
                    isActive()
                }, colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = MainColor,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = SwitchColor
                ), modifier = Modifier
                    .width(50.dp)
                    .padding(top = 5.dp)
            )

            //More vert
            Box {
                Icon(painter = painterResource(id = R.drawable.more_vert_24dp),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .clickable {
                            mDisplayMenu = !mDisplayMenu
                        }
                )
                DropdownMenu(
                    expanded = mDisplayMenu,
                    onDismissRequest = { mDisplayMenu = false },
                    modifier = Modifier
                        .background(color = Color.White, shape = RoundedCornerShape(10.dp))

                ) {

                    DropdownMenuItem(text = {
                        Text(text = "Edit Link")
                    },
                        onClick = {
                            mDisplayMenu = !mDisplayMenu
                            edit()
                        }, leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.edit_24dp__2),
                                contentDescription = ""
                            )
                        }
                    )

                    Divider(modifier = Modifier.padding(horizontal = 10.dp), color = Color.Gray)

                    DropdownMenuItem(text = {
                        Text(text = "Delete Link")
                    }, onClick = {
                        mDisplayMenu = !mDisplayMenu
                        delete()
                    }, leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.delete_24dp_2),
                            contentDescription = ""
                        )
                    }
                    )
                }
            }
        }
    }
}


@Composable
fun AddLinkLayout(
    registrationViewModel: RegistrationViewModel = hiltViewModel(),
    onCancel: () -> Unit
) {
    var linkTitle by remember {
        mutableStateOf("")
    }
    var link by remember {
        mutableStateOf("")
    }
    var context = LocalContext.current

    //val addtingLinkState = registrationViewModel.addAditionalLinkState
    var loading by remember { mutableStateOf(false) }
    //loading = addtingLinkState.isLoading


    Column(
        modifier = Modifier
            .background(color = Color.White, shape = RoundedCornerShape(10.dp))
            .border(width = 1.dp, color = BorderColor2, shape = RoundedCornerShape(10.dp))
            .padding(15.dp)
    ) {

        OutlinedTextField(
            shape = RoundedCornerShape(10.dp),
            value = linkTitle,
            onValueChange = { linkTitle = it },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.Sentences
            ),

            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                .border(1.dp, color = BorderColor2, shape = RoundedCornerShape(10.dp)),
            placeholder = {
                Text(
                    text = "Enter Title Here",
                    color = Color.Black,
                    style = TextStyle(
                        fontFamily = helveticaFontFamily,
                        fontWeight = FontWeight.Normal,
                    )
                )
            },
            maxLines = 1,
        )

        OutlinedTextField(
            shape = RoundedCornerShape(10.dp),
            value = link,
            onValueChange = { link = it },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                .border(1.dp, color = BorderColor2, shape = RoundedCornerShape(10.dp)),
            placeholder = {
                Text(
                    text = "Ex: https://example.com",
                    color = Color.Black,
                    style = TextStyle(
                        fontFamily = helveticaFontFamily,
                        fontWeight = FontWeight.Normal,
                    )
                )
            },
//            maxLines = 1,
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.link_24dp_2),
                    contentDescription = ""
                )
            }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        )
        {
            Button(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .width(150.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(SecondaryBackGround),
                onClick = {
                    onCancel()
                }
            )
            {
                Text(
                    text = "Cancel",
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontFamily = helveticaFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    ), color = Color.White
                )
            }

            CircularLoaderButton(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .width(150.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(MainColor),
                loading = loading,
                text = "SAVE",
                textStyle = TextStyle(
                    fontFamily = helveticaFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                onClick = {
                    if (linkTitle.isNotEmpty() && link.isNotEmpty()) {
                        if (registrationViewModel.isValidUrl(link)) {
                            registrationViewModel.updateUserLinks(
                                link = LinkData(
                                    linkTitle,
                                    link,
                                    true
                                )
                            )
                        } else {
                            Toast.makeText(context, "Invalid URL", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Enter Details Please", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }

    }
}

@Composable
fun EditLinkLayout(
    viewModel: RegistrationViewModel = hiltViewModel(), model: LinkData?
) {
    var linkTitle by remember {
        mutableStateOf(model?.title + "")
    }
    var link by remember {
        mutableStateOf(model?.url + "")
    }
    var isActive by remember {
        mutableStateOf(model?.isActive)
    }
    var context = LocalContext.current

    val addtingLinkState = viewModel.editAdditionalLinkState
    var loading by remember { mutableStateOf(false) }
    loading = addtingLinkState.isLoading


    Column(
        modifier = Modifier
            .background(color = Color.White, shape = RoundedCornerShape(10.dp))
            .border(width = 1.dp, color = BorderColor2, shape = RoundedCornerShape(10.dp))
            .padding(15.dp)
    ) {

        OutlinedTextField(
            shape = RoundedCornerShape(10.dp),
            value = linkTitle,
            onValueChange = { linkTitle = it },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                .border(1.dp, color = BorderColor2, shape = RoundedCornerShape(10.dp)),
            placeholder = {
                Text(
                    text = "Enter Title Here",
                    color = Color.Black,
                    style = TextStyle(
                        fontFamily = helveticaFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp
                    )
                )
            },
            maxLines = 1,
        )

        OutlinedTextField(
            shape = RoundedCornerShape(10.dp),
            value = link,
            onValueChange = { link = it },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                .border(1.dp, color = BorderColor2, shape = RoundedCornerShape(10.dp)),
            placeholder = {
                Text(
                    text = "Ex: https://example.com",
                    color = Color.Black,
                    style = TextStyle(
                        fontFamily = helveticaFontFamily,
                        fontWeight = FontWeight.Normal,
                    )
                )
            },
//            maxLines = 1,
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.link_24dp_2),
                    contentDescription = ""
                )
            }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        )
        {
            Button(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .width(150.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(SecondaryBackGround),
                onClick = {
                    viewModel.showEditingAdditionalLayout(false, -1)
                }
            )
            {
                Text(
                    text = "Cancel",
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontFamily = helveticaFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    ), color = Color.White
                )
            }

            CircularLoaderButton(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .width(150.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(MainColor),
                loading = loading,
                text = "SAVE",
                textStyle = TextStyle(
                    fontFamily = helveticaFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                onClick = {

                    if (linkTitle.isNotEmpty() && link.isNotEmpty()) {
                        if (viewModel.isValidUrl(link)) {
                            viewModel.editUserLinks(
                                link = LinkData(
                                    linkTitle,
                                    link,
                                    isActive
                                )
                            )
                        } else {
                            Toast.makeText(context, "Invalid URL", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Enter Details Please", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }

    }
}


//Static value


//data class links(
//    val name: String = "",
//    val title: String = ""
//)

//val ReorderItem = listOf(
//    links("hello1","titel1"),
//    links("hello2","titel2"),
//    links("hello3","titel3"),
//    links("hello4","titel4"),
//    links("hello5","titel5"),
//    links("hello6","titel6"),
//    links("hello7","titel7"),
//    links("hello8","titel8"),
//    links("hello9","titel9"),
//    links("hello10","titel10"),
//    links("hello11","titel11"),
//    links("hello12","titel12"),
//    links("hello13","titel13"),
//    links("hello14","titel14"),
//    links("hello15","titel15")
//).toMutableStateList()

//val ReorderItem = listOf(
//    "Item 1",
//    "Item 2",
//    "Item 3",
//    "Item 4",
//    "Item 5",
//    "Item 6",
//    "Item 7",
//    "Item 8",
//    "Item 9",
//    "Item 10",
//    "Item 11",
//    "Item 12",
//    "Item 13",
//    "Item 14",
//    "Item 15",
//    "Item 16",
//    "Item 17",
//    "Item 18",
//    "Item 19",
//    "Item 20"
//).toMutableStateList()


@Preview(showBackground = false)
@Composable
fun HomeScreenPreview() {
//    addedLinkLayout{
//    }
//    addLinkLayout {
//    }
    //HomeScreen(navController = rememberNavController(), registrationViewModel = hiltViewModel(), authenticationViewModel = hiltViewModel() )
}
