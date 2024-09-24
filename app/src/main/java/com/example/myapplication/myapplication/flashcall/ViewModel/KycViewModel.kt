package com.example.myapplication.myapplication.flashcall.ViewModel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.myapplication.flashcall.Data.model.faceMatch.FaceMatchRequest
import com.example.myapplication.myapplication.flashcall.Data.model.nameMatch.NameMatchRequest
import com.example.myapplication.myapplication.flashcall.Screens.imageUrl
import com.example.myapplication.myapplication.flashcall.repository.KycRepository
import com.example.myapplication.myapplication.flashcall.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class KycViewModel @Inject constructor(
    private val repository: KycRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

//    init {
//        checkKycStatus()
//    }

    data class AadharVerified(
        var verified: Boolean = false,
        var name: String? = null,
        var image: String? = null
    )

    data class PanVerified(
        var verified: Boolean = false,
        var name: String? = null
    )

    data class LivelinessVerified(
        var verified: Boolean = false,
        var imageUrl: String? = null
    )

    data class VerificationState(
        var isLoading: Boolean = false,
        val error: String? = null,
        val aadharVerified: AadharVerified = AadharVerified(verified = false),
        var panVerified: PanVerified = PanVerified(verified = false),
        var livelinessVerified: LivelinessVerified = LivelinessVerified(verified = false),
        var faceMatchVerified: Boolean = false,
        var nameMatchVerified: Boolean = false
    )

    data class RequiredDataToStartVerification(
        var ref_id: String? = null,
        var otpCheckStart: Boolean = false
    )

    data class AadharOTPVerificationState(
        var isLoading: Boolean = false,
        var otpVerificationStart: RequiredDataToStartVerification,
        val error: String? = null,
        var verified: Boolean = false
    )

    var panState by mutableStateOf(VerificationState())
        private set
    var aadharState by mutableStateOf(VerificationState())
        private set

    var aadharOTPVerificationState by mutableStateOf(
        AadharOTPVerificationState(
            isLoading = false,
            otpVerificationStart = RequiredDataToStartVerification(ref_id = null, false)
        )
    )
        private set
    var livelinessState by mutableStateOf(VerificationState())
        private set


    var faceMatchState by mutableStateOf(VerificationState())
        private set

    var nameMatchState by mutableStateOf(VerificationState())
        private set


    var userId = userPreferencesRepository.getUser()?._id ?: "user_id"

    fun panVerification(panNumber: String) {
        panState = panState.copy(isLoading = true)
        viewModelScope.launch {
            try {
                repository.verifyPan("api/v1/userkyc/verifyPan", panNumber, userId)
                    .collect { response ->
                        Log.d("panNumber", "response: ${response?.success}")
                        if (response?.success!! && response.data != null) {
                            panState = panState.copy(
                                isLoading = false,
                                panVerified = PanVerified(true, response.data.registered_name)
                            )
                        } else {
                            panState = panState.copy(
                                isLoading = false,
                                error = "pan verification error"
                            )
                        }
                    }
            } catch (e: Exception) {
                Log.d("panNumber", "response: ${e.message}")
                panState = panState.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun aadharGenerateOTP(aadhar: String) {
        aadharState = aadharState.copy(isLoading = true)
        viewModelScope.launch {
            try {
                repository.verifyAadhar(
                    "api/v1/userkyc/generateAadhaarOtp",
                    aadhar
                ).collect { response ->
                    if (response?.success!!) {
                        aadharState = aadharState.copy(
                            isLoading = false
                        )
                    } else {
                        aadharState = aadharState.copy(
                            isLoading = false,
                            error = "unable to generate otp, server error"
                        )
                    }
                    if (response.data?.ref_id != null) {
                        aadharOTPVerificationState = aadharOTPVerificationState.copy(
                            isLoading = false,
                            otpVerificationStart = RequiredDataToStartVerification(
                                response.data.ref_id,
                                true
                            ),
                            error = null,
                            verified = false
                        )
                    } else {
                        aadharState = aadharState.copy(
                            isLoading = false,
                            error = "unable to generate ref_id, server error"
                        )
                    }
                }
            } catch (e: Exception) {
                Log.d("aadharNumber", "response: ${e.message}")
                aadharState = aadharState.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun aadharOTPVerification(aadharOTP: String, ref_id: String) {
        aadharOTPVerificationState = aadharOTPVerificationState.copy(isLoading = true)
        viewModelScope.launch {
            try {
                repository.verifyAadharOTP(
                    "api/v1/userkyc/verifyAadhaarOtp",
                    aadharOTP,
                    ref_id,
                    userId
                ).collect { response ->
                    Log.d("AadharOtpVerification","response: $response")
                    if (response.success != null && response.success == true) {
                        if (response.data != null && response.data?.name != null && response.data?.photo_link != null) {
                            aadharOTPVerificationState = aadharOTPVerificationState.copy(
                                isLoading = false,
                                verified = true
                            )
                            aadharState = aadharState.copy(
                                isLoading = false,
                                aadharVerified = AadharVerified(
                                    verified = true,
                                    name = response.data?.name,
                                    image = response.data?.photo_link
                                )
                            )
                        } else {
                            aadharOTPVerificationState = aadharOTPVerificationState.copy(
                                isLoading = false,
                                error = "otp verification failed, server error"
                            )
                        }
                    } else {
                        aadharOTPVerificationState = aadharOTPVerificationState.copy(
                            isLoading = false,
                            error = "otp verification failed"
                        )
                    }
                }
            } catch (e: Exception) {
                aadharOTPVerificationState = aadharOTPVerificationState.copy(
                    isLoading = false,
                    error = "error: ${e.message}"
                )
            }
        }
    }

    fun uploadLiveliness(imageFile: File, verificationId: String, imgUrl: String) {
        livelinessState = livelinessState.copy(isLoading = true)
        val requestFile = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)
        val verificationIdPart = verificationId.toRequestBody("text/plain".toMediaTypeOrNull())
        val userIdPart = userId.toRequestBody("text/plain".toMediaTypeOrNull())
        val imgUrlPart = imgUrl.toRequestBody("text/plain".toMediaTypeOrNull())
        viewModelScope.launch {
            try {
                repository.uploadLiveliness(imagePart, verificationIdPart, userIdPart, imgUrlPart)
                    .collect { response ->
                        if (response.success != null && response.success == true) {
                            if (response.data != null && response.data?.status.equals("SUCCESS")) {
                                livelinessState = livelinessState.copy(
                                    isLoading = false,
                                    livelinessVerified = LivelinessVerified(
                                        verified = true,
                                        imageUrl = imgUrl
                                    )
                                )
                            } else {
                                livelinessState = livelinessState.copy(
                                    isLoading = false,
                                    error = "liveliness status unsuccessful, server error"
                                )
                            }
                        } else {
                            livelinessState = livelinessState.copy(
                                isLoading = false,
                                error = "liveliness server error"
                            )
                        }
                    }
            } catch (e: Exception) {
                livelinessState = livelinessState.copy(
                    isLoading = false,
                    error = "internal error"
                )
            }
        }
    }

    fun getFileFromUri(context: Context, uri: Uri, destinationFile: File): File? {
        return try {
            // Get the content resolver and open input stream from the URI
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)

            // Create an output stream to write to the destination file
            val outputStream = FileOutputStream(destinationFile)

            // Copy data from the input stream to the output stream
            inputStream?.copyTo(outputStream)

            // Close streams
            inputStream?.close()
            outputStream.close()

            // Return the file
            destinationFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getVerificationId(length: Int): String {
        val allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        var var_id: String = (1..length)
            .map { allowedChars.random() }
            .joinToString("")

        return userId + LocalDate.now() + var_id
    }

    fun getKysStatus() {
        viewModelScope.launch {
            repository.getKycStatus("api/v1/userkyc/getKyc?userId=$userId")
                .collect { response ->
                    Log.d("FaceMatchPrint", "img1: ${response.data?.liveliness?.img_url}")
                    if (response.success != null && response.success == true) {
                        if (response.data != null) {
                            // Aadhar Check
                            if (response.data?.aadhaar != null) {
                                if (response.data?.aadhaar!!.status.equals("VALID")) {
                                    Log.d("AadharKycStatus","link: ${response.data?.aadhaar!!.img_link}")
                                    aadharState = aadharState.copy(
                                        aadharVerified = AadharVerified(
                                            verified = true,
                                            name = response.data?.aadhaar!!.name,
                                            image = response.data?.aadhaar!!.img_link
                                        )
                                    )
                                } else {
                                    aadharState = aadharState.copy(
                                        aadharVerified = AadharVerified(
                                            verified = false
                                        )
                                    )
                                }
                            } else {
                                aadharState = aadharState.copy(
                                    aadharVerified = AadharVerified(
                                        verified = false
                                    )
                                )
                            }

                            // Pan Check
                            if (response.data?.pan != null) {
                                if (response.data?.pan!!.valid == true) {
                                    panState = panState.copy(
                                        panVerified = PanVerified(
                                            verified = true,
                                            name = response.data?.pan!!.registeredName
                                        )
                                    )
                                } else {
                                    panState = panState.copy(
                                        panVerified = PanVerified(verified = false)
                                    )
                                }
                            } else {
                                panState = panState.copy(
                                    panVerified = PanVerified(verified = false)
                                )
                            }

                            // Liveliness Check
                            if (response.data?.liveliness != null) {
                                if (response.data?.liveliness!!.status.equals("SUCCESS")) {
                                    livelinessState = livelinessState.copy(
                                        livelinessVerified = LivelinessVerified(
                                            verified = true,
                                            imageUrl = response.data?.liveliness!!.img_url
                                        )
                                    )
                                } else {
                                    livelinessState = livelinessState.copy(
                                        livelinessVerified = LivelinessVerified(verified = false)
                                    )
                                }
                            } else {
                                livelinessState = livelinessState.copy(
                                    livelinessVerified = LivelinessVerified(verified = false)
                                )
                            }

                            // FaceMatch Check
                            if (response.data?.faceMatch != null && response.data?.faceMatch?.status.equals("SUCCESS")) {
                                faceMatchState = faceMatchState.copy(faceMatchVerified = true)
                            }
                            else {
                                faceMatchState = faceMatchState.copy(faceMatchVerified = false)
                            }

                            // NameMatch Check
                            if (response.data?.nameMatch != null && response.data?.nameMatch?.status.equals("SUCCESS")) {
                                nameMatchState = nameMatchState.copy(faceMatchVerified = true)
                            }
                            else {
                                nameMatchState = nameMatchState.copy(faceMatchVerified = false)
                            }

                        }
                    }


                }

        }
    }

    fun nameMatch() {
        val body = NameMatchRequest(aadharState.aadharVerified.name?:"",panState.panVerified.name?:"",userId,getVerificationId(15))
        Log.d("NameMatchBody","requestBody: $body")
        viewModelScope.launch {
            repository.nameMatch("api/v1/userkyc/nameMatch", body).collect { response ->
                Log.d("FaceMatchPrint","responseBody: $response")
                if (response.success) {
                    nameMatchState = nameMatchState.copy(nameMatchVerified = true)
                } else {
                    nameMatchState = nameMatchState.copy(nameMatchVerified = false)
                }
            }
        }
    }
    fun faceMatch() {
        val body = FaceMatchRequest(livelinessState.livelinessVerified.imageUrl?:"",aadharState.aadharVerified.image?:"",userId,getVerificationId(15))
        Log.d("FaceMatchPrint","requestBody: uid ${body.userId}")
        Log.d("FaceMatchPrint","requestBody: vid ${body.verificationId}")
        Log.d("FaceMatchPrint","requestBody: firebase ${body.firstImg}")
        Log.d("FaceMatchPrint","requestBody: aadhar ${body.secondImg}...")
        viewModelScope.launch {
            repository.faceMatch("api/v1/userkyc/faceMatch", body).collect { response ->
                Log.d("FaceMatchPrint","responseBody: $response")
                if (response.success) {
                    faceMatchState = faceMatchState.copy(faceMatchVerified = true)
                } else {
                    faceMatchState = faceMatchState.copy(faceMatchVerified = false)
                }
            }
        }
    }

    fun compressImageToCache(context: Context, imageFile: File, quality: Int): File? {
        // Step 1: Decode the image file into a Bitmap
        val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)

        // Step 2: Create a temporary file in the cache directory
        val cacheFile = File(context.cacheDir, "${LocalDateTime.now()}_img.jpg")

        // Step 3: Initialize the output stream to write the compressed image to cache
        var outputStream = FileOutputStream(cacheFile)

        // Step 4: Compress the bitmap with the specified quality
        var currentQuality = quality
        bitmap.compress(Bitmap.CompressFormat.JPEG, currentQuality, outputStream)

        // Step 5: Check the size of the compressed file
        var fileSizeInBytes = cacheFile.length()

        // Step 6: Reduce quality if the file size is larger than 1 MB (1,048,576 bytes)
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

    fun largeImageUploadingError() {
        livelinessState = livelinessState.copy(
            isLoading = false, error = "Image size shouldn't be more than 1 MB."
        )
    }

    fun checkKycStatus() {
        if (userPreferencesRepository.isKyc()) {
            panState = panState.copy(panVerified = PanVerified(verified = true))
            aadharState = aadharState.copy(aadharVerified = AadharVerified(verified = true))
            livelinessState = livelinessState.copy(livelinessVerified = LivelinessVerified(verified = true))
            faceMatchState = faceMatchState.copy(faceMatchVerified = true)
            nameMatchState = nameMatchState.copy(nameMatchVerified = true)
        }else{
            getKysStatus()
        }
    }

    fun makeKycDone(){
        userPreferencesRepository.saveKyc(true)
    }

}