package com.example.myapplication.myapplication.flashcall.Screens

import android.Manifest
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import co.hyperverge.hyperkyc.data.models.HyperKycConfig
import com.composeuisuite.ohteepee.OhTeePeeInput
import com.composeuisuite.ohteepee.configuration.OhTeePeeCellConfiguration
import com.composeuisuite.ohteepee.configuration.OhTeePeeConfigurations
import com.example.myapplication.myapplication.flashcall.R
import com.example.myapplication.myapplication.flashcall.ViewModel.KycViewModel
import com.example.myapplication.myapplication.flashcall.ui.theme.MainColor
import com.example.myapplication.myapplication.flashcall.ui.theme.OTPBackground
import com.example.myapplication.myapplication.flashcall.ui.theme.OTPBorder
import com.example.myapplication.myapplication.flashcall.ui.theme.ProfileBackground
import com.example.myapplication.myapplication.flashcall.utils.LoadingIndicator
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.ui.theme.helveticaFontFamily
import com.example.myapplication.myapplication.flashcall.utils.getVerificationId
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KYCScreen(
    navController: NavController,
    hyperKycLauncher: ActivityResultLauncher<HyperKycConfig>,
    viewModel: KycViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.checkKycStatus()
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(ProfileBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp)
                .verticalScroll(rememberScrollState()),

            ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "KYC Documents",
                style = TextStyle(
                    fontFamily = helveticaFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color.Black
                ),
            )
            Spacer(modifier = Modifier.height(16.dp))

            PanKYC(viewModel)

            AadharKYC(viewModel)

            LivelinessKYC(viewModel, navController = navController)

            val panState = viewModel.panState
            val aadharState = viewModel.aadharState
            val livelinessState = viewModel.livelinessState
            if (panState.isPanVerified && aadharState.isAadharVerified && livelinessState.isLivelinessVerified) {
                viewModel.makeKycDone(isDone = true)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp), horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "KYC Completed",
                        color = MainColor,
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = helveticaFontFamily
                    )
                }

            }
        }
    }
}

@Composable
fun PanKYC(vm: KycViewModel) {
    var isExpand by remember { mutableStateOf(false) }
    val panState = vm.panState


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "PAN",
                    fontFamily = helveticaFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (panState.isPanVerified) {
                        Image(
                            painter = painterResource(id = R.drawable.baseline_verified_24),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(MainColor),
                            modifier = Modifier
                                .height(24.dp)
                                .width(24.dp)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.exclamation1),
                            contentDescription = null,
                            modifier = Modifier
                                .height(24.dp)
                                .width(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    if (!panState.isPanVerified) {
                        Icon(
                            if (isExpand) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = if (isExpand) "Collapse" else "Expand",
                            tint = Color.Gray,
                            modifier = Modifier
                                .clickable(onClick = {
                                    isExpand = !isExpand
                                })
                        )
                    }
                }
            }

            if (!panState.isPanVerified) {
                if (isExpand) {
                    Spacer(modifier = Modifier.height(16.dp))
                    var panNumber by remember { mutableStateOf("") }
                    OutlinedTextField(
                        value = panNumber,
                        onValueChange = { panNumber = it.uppercase() },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Enter PAN", fontFamily = helveticaFontFamily) },
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    if (panState.isLoading) {
                        LoadingIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    if (panState.error != null) {
                        ShowError(error = panState.error + "")
                    }

                    Button(
                        onClick = { vm.panVerification(panNumber) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Verify", color = Color.White, fontFamily = helveticaFontFamily)
                    }
                }
            }

        }
    }
}

@Composable
fun AadharKYC(vm: KycViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val aadharState = vm.aadharState

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Aadhaar",
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    fontFamily = helveticaFontFamily
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (aadharState.isAadharVerified) {
                        Image(
                            painter = painterResource(id = R.drawable.baseline_verified_24),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(MainColor),
                            modifier = Modifier
                                .height(24.dp)
                                .width(24.dp)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.exclamation1),
                            contentDescription = null,
                            modifier = Modifier
                                .height(24.dp)
                                .width(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    if (!aadharState.isAadharVerified) {
                        Icon(
                            if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = if (expanded) "Collapse" else "Expand",
                            tint = Color.Gray,
                            modifier = Modifier
                                .clickable(onClick = { expanded = !expanded })
                        )
                    }
                }
            }

            if (!aadharState.isAadharVerified) {
                if (expanded) {
                    Spacer(modifier = Modifier.height(16.dp))
                    var aadhaarNumber by remember { mutableStateOf("") }

                    OutlinedTextField(
                        value = aadhaarNumber,
                        onValueChange = { aadhaarNumber = it },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        placeholder = {
                            Text(
                                "Enter your Aadhaar Number",
                                fontFamily = helveticaFontFamily
                            )
                        },
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    if (aadharState.isLoading) {
                        LoadingIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    if (aadharState.error != null) {
                        ShowError(error = aadharState.error + "")
                    }

                    Button(
                        onClick = { vm.aadharGenerateOTP(aadhaarNumber) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Get OTP", color = Color.White, fontFamily = helveticaFontFamily)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    val otpVerificationState = vm.aadharOTPVerificationState
                    if (otpVerificationState.otpVerificationStart.otpCheckStart && otpVerificationState.otpVerificationStart.ref_id != null) {

                        var refId = otpVerificationState.otpVerificationStart.ref_id
                        var ref_id by remember {
                            mutableStateOf(refId)
                        }
                        var otpValue by remember {
                            mutableStateOf("")
                        }
                        var isKeyboardOpen by remember { mutableStateOf(false) }
                        val keyboardController = LocalSoftwareKeyboardController.current
                        val focusRequester = remember { FocusRequester() }


                        if (otpVerificationState.error != null) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            )
                            {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    val otpCellConfig = OhTeePeeCellConfiguration.withDefaults(
                                        borderColor = Color.Red,
                                        borderWidth = 1.dp,
                                        shape = RoundedCornerShape(16.dp),
                                        backgroundColor = OTPBackground,
                                        textStyle = TextStyle(
                                            color = Color.Black,
                                            fontSize = 16.sp,
                                            fontFamily = helveticaFontFamily,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    OhTeePeeInput(
                                        value = otpValue,
                                        onValueChange = { newValue, isValid ->
                                            otpValue = newValue
                                            if (otpValue.length == 6 && isValid) {
                                                // Avoid multiple calls by checking that the length is exactly 6
                                                if (otpValue.length == 6) {
                                                    // Automatically call verifyOTP when otpValue is exactly 6 digits
                                                    keyboardController?.hide()
                                                    vm.aadharOTPVerification(
                                                        otpValue,
                                                        ref_id ?: "ref_id"
                                                    )
                                                }
                                            }
                                        },
                                        configurations = OhTeePeeConfigurations.withDefaults(
                                            cellsCount = 6,
                                            activeCellConfig = otpCellConfig.copy(
                                                borderColor = Color.Red,
                                                borderWidth = 3.dp
                                            ),
                                            emptyCellConfig = otpCellConfig,
                                            cellModifier = Modifier
                                                .padding(horizontal = 4.dp)
                                                .width(36.dp)
                                                .height(40.dp)
                                                .focusRequester(focusRequester)
                                                .onFocusChanged {
                                                    isKeyboardOpen = it.isFocused
                                                },
                                            elevation = 2.dp
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                ShowError(error = "Invalid OTP. Try again, ${otpVerificationState.error}")
                            }
                        } else {

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                val otpCellConfig = OhTeePeeCellConfiguration.withDefaults(
                                    borderColor = OTPBorder,
                                    borderWidth = 1.dp,
                                    shape = RoundedCornerShape(16.dp),
                                    backgroundColor = OTPBackground,
                                    textStyle = TextStyle(
                                        color = Color.Black,
                                        fontSize = 16.sp,
                                        fontFamily = helveticaFontFamily,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                OhTeePeeInput(
                                    value = otpValue,
                                    onValueChange = { newValue, isValid ->
                                        otpValue = newValue
                                        if (otpValue.length == 6 && isValid) {
                                            // Avoid multiple calls by checking that the length is exactly 6
                                            keyboardController?.hide()
                                            if (otpValue.length == 6) {
                                                // Automatically call verifyOTP when otpValue is exactly 6 digits
                                                vm.aadharOTPVerification(
                                                    otpValue,
                                                    ref_id ?: "ref_id"
                                                )
                                            }
                                        }
                                    },
                                    configurations = OhTeePeeConfigurations.withDefaults(
                                        cellsCount = 6,
                                        activeCellConfig = otpCellConfig.copy(
                                            borderColor = OTPBorder,
                                            borderWidth = 3.dp
                                        ),
                                        emptyCellConfig = otpCellConfig,
                                        cellModifier = Modifier
                                            .padding(horizontal = 4.dp)
                                            .width(36.dp)
                                            .height(40.dp)
                                            .focusRequester(focusRequester)
                                            .onFocusChanged {
                                                isKeyboardOpen = it.isFocused
                                            },
                                        elevation = 2.dp
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }

                        if (otpVerificationState.isLoading) {
                            LoadingIndicator()
                        }


                    }

                    if (otpVerificationState.verified) {

                        Row(horizontalArrangement = Arrangement.Center) {

                            Text(
                                text = "Aadhar verified Successfully",
                                color = Color.Green,
                                fontFamily = helveticaFontFamily
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LivelinessKYC(vm: KycViewModel, navController: NavController) {
    var expanded by remember { mutableStateOf(false) }
    var livelinessState = vm.livelinessState

//    var isUploadingImage by remember {
//        mutableStateOf(false)
//    }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Liveliness Check",
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    fontFamily = helveticaFontFamily
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (livelinessState.isLivelinessVerified) {
                        Image(
                            painter = painterResource(id = R.drawable.baseline_verified_24),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(MainColor),
                            modifier = Modifier
                                .height(24.dp)
                                .width(24.dp)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.exclamation1),
                            contentDescription = null,
                            modifier = Modifier
                                .height(24.dp)
                                .width(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    if (!livelinessState.isLivelinessVerified) {
                        Icon(
                            if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = if (expanded) "Collapse" else "Expand",
                            tint = Color.Gray,
                            modifier = Modifier.clickable {
                                navController.navigate(ScreenRoutes.CaptureImageScreen.route)
//                                expanded = !expanded
                            }
                        )
                    }
                }
            }

//            if(!livelinessState.isLivelinessVerified){
//                if (expanded) {
//                    Spacer(modifier = Modifier.height(16.dp))
//                    var fileName by remember { mutableStateOf("No file chosen") }
//                    val context = LocalContext.current
//                    val destinationFile = File(context.cacheDir, "${LocalDateTime.now()}_img.tmp")
//                    var uri: Uri? = null
//                    var uriImg by remember {
//                        mutableStateOf(uri)
//                    }
//                    var imageUriStr by remember {
//                        mutableStateOf("")
//                    }
//
//                    val launcher = rememberLauncherForActivityResult(
//                        contract = ActivityResultContracts.GetContent()
//                    ) { uri: Uri? ->
//                        uri?.let {
//                            isUploadingImage = !isUploadingImage
//                            uriImg = it
//                            uploadImageToFirebase(uri, context) { imgUrl ->
//                                imageUriStr = imgUrl
//                                Log.d("ImageUploaded", "url: $imageUriStr, uri: $uriImg")
//                                isUploadingImage = !isUploadingImage
//                            }
//
//                        }
//                    }
//                    Button(
//                        onClick = {
//                            launcher.launch("image/*")
//                        },
//                        modifier = Modifier.fillMaxWidth(),
//                        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
//                        shape = RoundedCornerShape(8.dp)
//                    ) {
//                        Text("Choose File", color = Color.Black)
//                    }
//                    Spacer(modifier = Modifier.height(8.dp))
//                    if(imageUriStr != null){
//                        fileName = "Image Selected"
//                    }
//                    Text(fileName, color = Color.Gray)
//                    Spacer(modifier = Modifier.height(16.dp))
//
//                    if (livelinessState.isLoading || isUploadingImage) {
//                        LoadingIndicator()
//                    }
//                    if (livelinessState.error != null) {
//                        ShowError(error = ""+livelinessState.error)
//                    }
//
//                    Button(
//                        onClick = {
//                            if (uriImg != null && imageUriStr != null) {
//                                val file = vm.getFileFromUri(context, uriImg!!, destinationFile)
//                                if (file != null) {
//                                    var verificationId = getVerificationId(10)
//                                    if (file.length() < 1_048_576) {
//                                        vm.uploadLiveliness(file, verificationId, imageUriStr!!)
//                                    } else {
//                                        val compressedFile = vm.compressImageToCache(context, file, 80)
//                                        if (compressedFile != null) {
//                                            vm.uploadLiveliness(file, verificationId, imageUriStr!!)
//                                        } else {
//                                            vm.largeImageUploadingError()
//                                        }
//                                    }
//                                } else {
//                                    Toast.makeText(context, "Image Not Found", Toast.LENGTH_SHORT).show()
//                                }
//                            } else {
//                                if(uriImg == null){
//                                    Toast.makeText(context, "Uri Processing...", Toast.LENGTH_SHORT).show()
//                                }
//                                if(imageUriStr == null){
//                                    Toast.makeText(context, "Image Processing...", Toast.LENGTH_SHORT).show()
//                                }
//
//                            }
//                        },
//                        modifier = Modifier.fillMaxWidth(),
//                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
//                        shape = RoundedCornerShape(8.dp)
//                    ) {
//                        Text("Verify", color = Color.White)
//                    }
//                }
//            }
        }
    }
}

@Composable
fun ShowError(error: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = error, color = Color.Red, fontSize = 12.sp, textAlign = TextAlign.Center)
        Text(
            text = "our team will verify the details you have submitted. This usually takes 24 hours",
            color = Color.Gray,
            textAlign = TextAlign.Center,
            fontSize = 12.sp
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
}


//@Preview
//@Composable
//fun KycScreenPreview() {
//panVerification(vm)
//KYCScreen(navController = rememberNavController())
//}
