package com.example.myapplication.myapplication.flashcall.Screens

import android.Manifest
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.myapplication.myapplication.flashcall.R
import com.example.myapplication.myapplication.flashcall.ViewModel.ImageCaptureViewModel
import com.example.myapplication.myapplication.flashcall.ui.theme.MainColor
import com.example.myapplication.myapplication.flashcall.utils.LoadingIndicator
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import java.io.File


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CaptureImageScreen(navController: NavController, viewModel: ImageCaptureViewModel = hiltViewModel()) {
    val context: Context = LocalContext.current
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val requestLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts
            .RequestPermission()
    ) { isGranted ->
        viewModel.cameraPermissionState(isGranted)
    }
    LaunchedEffect(cameraPermissionState) {
        if (!cameraPermissionState.status.isGranted && cameraPermissionState.status.shouldShowRationale) {
            Toast.makeText(context, "Camera Permission Not Granted", Toast.LENGTH_SHORT)
                .show()
            // Show rationale if needed
        } else {
            requestLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }
    val captureState = viewModel.imageCaptureState

    if (captureState.isCameraPermissionGranted && captureState.imageCapturedSucces == null) {
        CameraPreview(
            outputDirectory = viewModel.getOutputDirectory(context),
            onImageCaptured = { file ->
                if (file.length() < 1_048_576) {
                    Log.d("ImageUploaded", "less size: ${file.length()}")
                    viewModel.imageCaptured(isCaptured = true, file = file, error = null)
                }
                else{
                    val outPutFolder = viewModel.getOutputDirectory(context)
                    val outPutFile = File(
                        outPutFolder,
                        "${System.currentTimeMillis()}.jpg"
                    )
                    viewModel.compressImage(file,outPutFile, 25)
                    Log.d("ImageUploaded", "compressed size: ${outPutFile.length()}")
                    viewModel.imageCaptured(isCaptured = true, file = outPutFile, error = null)
                }
            },
            onError = { exception ->
                viewModel.imageCaptured(isCaptured = false, file = null, error = exception.message)
                Log.e("CaptureImageScreen", "Image capture failed: ${exception.message}", exception)
            }
        )
    }

    if (captureState.imageCapturedSucces != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(captureState.imageCapturedSucces?.absolutePath.toString())
                        .crossfade(true).build(),
                    contentDescription = null,
                    placeholder = painterResource(id = R.drawable.profile_picture_holder),
                    modifier = Modifier
                        .size(300.dp)  // Ensure a consistent size
                        .clip(CircleShape)
                        .border(1.dp, color = MainColor, shape = CircleShape),  // Clip to a circle
                    contentScale = ContentScale.Crop  // Crop the image to fit within the circle
                )

                Spacer(modifier = Modifier.height(10.dp))

                if (captureState.isLoading) {
                    LoadingIndicator()
                    Spacer(modifier = Modifier.height(10.dp))
                }

                if (captureState.error != null) {
                    Text(text = "Image Capturing Error: ${captureState.error}", color = Color.Red)
                    Spacer(modifier = Modifier.height(10.dp))
                }

                if (captureState.success != null) {
                    Text(text = "Image Uploaded Successfully", color = MainColor)
                    Spacer(modifier = Modifier.height(10.dp))
                }

                if (captureState.success != null) {
                    Button(onClick = {
                        navController.popBackStack()
                    }) {
                        Text(text = "Back")
                    }
                }else{
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = {
                            viewModel.captureAgain()
                        }) {
                            Text(text = "Capure Again")
                        }
                        Button(onClick = {
                            viewModel.uploadImage(context = context)
                        }) {
                            Text(text = "Submit")
                        }
                    }
                }

            }
        }

    }


}


@Composable
fun CameraPreview(
    outputDirectory: File,
    onImageCaptured: (File) -> Unit,
    onError: (ImageCaptureException) -> Unit
) {
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val previewView = remember { PreviewView(context) }

    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var cameraSelector by remember { mutableStateOf(CameraSelector.DEFAULT_FRONT_CAMERA) }

    // Camera Preview
    LaunchedEffect(cameraProviderFuture) {
        val cameraProvider = cameraProviderFuture.get()

        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        imageCapture = ImageCapture.Builder().build()

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                context as androidx.lifecycle.LifecycleOwner,
                cameraSelector,
                preview,
                imageCapture
            )
        } catch (exc: Exception) {
            Log.e("CameraPreview", "Camera binding failed", exc)
        }
    }

    // Capture Button
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AndroidView(
            factory = { previewView },
            modifier = Modifier
                .height(300.dp)
                .width(300.dp)
                .clip(shape = CircleShape)
                .border(1.dp, shape = CircleShape, color = MainColor)
        )
        Button(onClick = {
            imageCapture?.let { capture ->
                val photoFile = File(
                    outputDirectory,
                    "${System.currentTimeMillis()}.jpg"
                )

                val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

                capture.takePicture(
                    outputOptions,
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onError(exc: ImageCaptureException) {
                            onError(exc)
                        }

                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            onImageCaptured(photoFile)
                        }
                    }
                )
            }
        }, modifier = Modifier.padding(top = 10.dp)) {
            Text("Capture Image")
        }
    }
}