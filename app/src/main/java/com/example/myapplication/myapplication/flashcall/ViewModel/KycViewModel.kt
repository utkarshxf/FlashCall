package com.example.myapplication.myapplication.flashcall.ViewModel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.myapplication.flashcall.Data.model.APIResponse
import com.example.myapplication.myapplication.flashcall.Data.model.AadhaarResponse
import com.example.myapplication.myapplication.flashcall.Data.model.KYCResponse
import com.example.myapplication.myapplication.flashcall.Data.model.SDKResponseState
import com.example.myapplication.myapplication.flashcall.repository.KycRepository
import com.example.myapplication.myapplication.flashcall.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.getstream.video.android.core.Call
import io.getstream.video.android.core.ConnectionState
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class KycViewModel @Inject constructor(
    private val repository: KycRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    data class VerificationState(
        var isLoading: Boolean = false,
        val error: String? = null,
        var verified: Boolean = false
    )

    var panState by mutableStateOf(VerificationState())
        private set
    var aadharState by mutableStateOf(VerificationState())
        private set
    var livelinessState by mutableStateOf(VerificationState())
        private set

    var userId = userPreferencesRepository.getUser()?._id ?: "user_id"

    fun panVerification(panNumber: String) {
        Log.d("panNumber", panState.toString())
        Log.d("panNumber", panNumber)
        Log.d("panNumber", userId)
        panState = panState.copy(isLoading = true)
        viewModelScope.launch {
            try {
                repository.verifyPan(panNumber, userId).collect { response ->
                    panState = panState.copy(
                        isLoading = false,
                        verified = response.success
                    )
                }
            } catch (e: Exception) {
                panState = panState.copy(
                    isLoading = false,
                    verified = false,
                    error = e.message
                )
            }
        }
    }

    fun aadharVerification(aadhar: String) {
        aadharState = aadharState.copy(isLoading = true)
        viewModelScope.launch {
            try {
                repository.verifyAadhar(aadhar).collect { response ->
                    aadharState = aadharState.copy(
                        isLoading = false,
                        verified = response.success,
                        error = if (!response.success) response.data.toString() else null
                    )
                }
            } catch (e: Exception) {
                aadharState = aadharState.copy(
                    isLoading = false,
                    verified = false,
                    error = e.message
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
                repository.uploadLiveliness(imagePart, verificationIdPart, userIdPart, imgUrlPart).collect { response ->
                    livelinessState = livelinessState.copy(
                        isLoading = false,
                        verified = response.success,
                        error = if (!response.success) response.error else null
                    )
                }
            } catch (e: Exception) {
                livelinessState = livelinessState.copy(
                    isLoading = false,
                    verified = false,
                    error = e.message
                )
            }
        }
    }
}