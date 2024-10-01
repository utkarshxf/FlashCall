package com.example.myapplication.myapplication.flashcall.ViewModel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.myapplication.flashcall.R
import com.example.myapplication.myapplication.flashcall.Screens.uploadImageToFirebase
import com.example.myapplication.myapplication.flashcall.repository.ImageCaptureRepository
import com.example.myapplication.myapplication.flashcall.repository.UserPreferencesRepository
import com.example.myapplication.myapplication.flashcall.utils.getVerificationId
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ImageCaptureViewModel @Inject constructor(
    private val repository: ImageCaptureRepository
    ,private val userPreferencesRepository: UserPreferencesRepository
    ,@ApplicationContext private val context: Context
) : ViewModel() {

    data class ImageCaptureState(
        var isCameraPermissionGranted: Boolean = false,
        var success: String? = null,
        var error: String? = null,
        var isLoading: Boolean = false,
        var imageCapturedSucces: File? = null

    )

    var imageCaptureState by mutableStateOf(ImageCaptureState())
        private set



    fun cameraPermissionState(state: Boolean){
        imageCaptureState = imageCaptureState.copy(isCameraPermissionGranted = state)
    }
    fun captureAgain(){
        imageCaptureState = imageCaptureState.copy(isCameraPermissionGranted = true, imageCapturedSucces = null)
    }

    fun imageCaptured(isCaptured: Boolean, file: File?, error: String?){
        if(isCaptured && file != null){
            imageCaptureState = imageCaptureState.copy(isCameraPermissionGranted = true, imageCapturedSucces = file)
        }else{
            imageCaptureState = imageCaptureState.copy(isCameraPermissionGranted = true, error = error)
        }
    }

    fun uploadImage(context: Context){
        imageCaptureState = imageCaptureState.copy(isLoading = true)
        if(imageCaptureState.imageCapturedSucces!= null){
            val uri = getUriFromFile(context, imageCaptureState.imageCapturedSucces!!)
            if(uri != null){
                uploadImageToFirebase(uri, context) { imgUrl ->
                    Log.d("ImageUploaded", "url: $imgUrl, uri: $uri")
                    if(imgUrl.isNotEmpty()){
                        uploadLiveliness(imgUrl)
                    }else{
                        imageCaptureState = imageCaptureState.copy(isLoading = false, imageCapturedSucces = null, error = "Image URL Not Found")
                    }
                }
            }else{
                Toast.makeText(context, "Unable to get URI", Toast.LENGTH_SHORT).show()
            }

        }else{
            imageCaptureState = imageCaptureState.copy(isLoading = false,imageCapturedSucces = null, error = "Image Not Found")
        }
    }

    fun uploadLiveliness(imgUrl: String) {
        if(imageCaptureState.imageCapturedSucces != null){
            val imageFile = imageCaptureState.imageCapturedSucces
            val userId = userPreferencesRepository.getUser()?._id+""
            val verificationId = userId+getVerificationId(10)

            val requestFile = imageFile?.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("image", imageFile?.name, requestFile!!)
            val verificationIdPart = verificationId.toRequestBody("text/plain".toMediaTypeOrNull())
            val userIdPart = userId.toRequestBody("text/plain".toMediaTypeOrNull())
            val imgUrlPart = imgUrl.toRequestBody("text/plain".toMediaTypeOrNull())
            viewModelScope.launch {
                try {
                    repository.uploadLiveliness(imagePart, verificationIdPart, userIdPart, imgUrlPart)
                        .collect { response ->
                            Log.d("ImageUploaded","response: $response")
                            if (response.success != null && response.success == true) {
                                imageCaptureState = imageCaptureState.copy(isLoading = false, success = "Liveliness Uploaded Successfully")
                                if(response.kycStatus){
                                    makeKycDone()
                                }
                            } else {
                                imageCaptureState = imageCaptureState.copy(isLoading = false, error = "${response.message}")
                            }
                        }
                } catch (e: Exception) {
                    imageCaptureState = imageCaptureState.copy(isLoading = false, error = "${e.message}")
                }
            }
        }else{
            imageCaptureState = imageCaptureState.copy(isLoading = false, imageCapturedSucces = null, error = "Image File Not Found")
        }
    }

    fun getUriFromFile(context: Context, file: File): Uri? {
        if (file.length() < 1_048_576) {
            Log.d("ImageUploaded", "fileSize: less ${file.length()}")
            return FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )
        }else{
            val newFile = compressImageToCache(context, file, 80)
            Log.d("ImageUploaded", "fileSize: more ${file.length()}")
            return if(newFile != null){
                FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    newFile
                )
            }else{
                null
            }
        }


    }


    fun compressImage(inputFile: File, outputFile: File, quality: Int = 80) {
        // Decode the image file into a bitmap
        val bitmap = BitmapFactory.decodeFile(inputFile.absolutePath)

        // Get the correct rotation based on the EXIF data
        val rotatedBitmap = rotateImageIfRequired(bitmap, inputFile)

        // Create an output stream to save the compressed image
        val outputStream: OutputStream = FileOutputStream(outputFile)

        // Compress the bitmap and save it to the output file
        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)

        // Flush and close the stream
        outputStream.flush()
        outputStream.close()
    }

    // Function to rotate the image if required based on EXIF data
    fun rotateImageIfRequired(bitmap: Bitmap, inputFile: File): Bitmap {
        val exif = ExifInterface(inputFile.absolutePath)
        val orientation: Int = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270f)
            else -> bitmap // No rotation needed
        }
    }

    // Function to rotate the bitmap
    fun rotateImage(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }


    fun getOutputDirectory(context: Context): File {
        val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
            File(it, context.resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else context.filesDir
    }

    fun compressImageToCache(context: Context, imageFile: File, quality: Int): File? {
        val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
        val cacheFile = File(context.cacheDir, "${LocalDateTime.now()}_img.jpg")
        var outputStream = FileOutputStream(cacheFile)
        var currentQuality = quality
        bitmap.compress(Bitmap.CompressFormat.JPEG, currentQuality, outputStream)
        var fileSizeInBytes = cacheFile.length()
        while (fileSizeInBytes > 1_048_576 && currentQuality > 10) {  // Stop if quality is below 10%
            currentQuality -= 5 // Decrease quality in steps of 5
            outputStream.close()

            // Compress again with reduced quality
            outputStream = FileOutputStream(cacheFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, currentQuality, outputStream)
            outputStream.flush()
            outputStream.close()

            // Check the size of the compressed file again
            fileSizeInBytes = cacheFile.length()
        }
        // Step 7: If the compressed file is still larger than 1 MB, return null
        return if (fileSizeInBytes <= 1_048_576) cacheFile else null
    }


    fun makeKycDone(){
        userPreferencesRepository.saveKyc(true)
    }

}