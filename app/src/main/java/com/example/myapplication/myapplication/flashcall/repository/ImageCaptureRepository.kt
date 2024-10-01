package com.example.myapplication.myapplication.flashcall.repository

import android.util.Log
import com.example.myapplication.myapplication.flashcall.Data.model.livelinessResponse.KycResponse
import com.example.myapplication.myapplication.flashcall.Data.network.APIService
import com.example.myapplication.myapplication.flashcall.utils.SafeApiRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class ImageCaptureRepository @Inject constructor(private val apiService: APIService): SafeApiRequest() {

    suspend fun uploadLiveliness(
        imagePart: MultipartBody.Part,
        verificationIdPart: RequestBody,
        userIdPart: RequestBody,
        imgUrlPart: RequestBody
    ): Flow<KycResponse> {
        return flow {
            val response = safeApiRequest {apiService.uploadLiveliness(imagePart , verificationIdPart , userIdPart , imgUrlPart)}
            emit(response)
        }
    }

}