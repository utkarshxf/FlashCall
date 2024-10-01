package com.example.myapplication.myapplication.flashcall.repository

import android.util.Log
import com.example.myapplication.myapplication.flashcall.Data.model.AadhaarRequest
import com.example.myapplication.myapplication.flashcall.Data.model.AadhaarResponse
import com.example.myapplication.myapplication.flashcall.Data.model.VerifyAadhaarOtpRequest
import com.example.myapplication.myapplication.flashcall.Data.model.VerifyPanRequest
import com.example.myapplication.myapplication.flashcall.Data.model.aadharOtpResponse.AadharOtpResponse
import com.example.myapplication.myapplication.flashcall.Data.model.kycStatus.KycStatusResponse
import com.example.myapplication.myapplication.flashcall.Data.model.livelinessResponse.KycResponse
import com.example.myapplication.myapplication.flashcall.Data.network.APIService
import com.example.myapplication.myapplication.flashcall.utils.SafeApiRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class KycRepository @Inject constructor(private val apiService: APIService): SafeApiRequest() {

    suspend fun verifyPan(url: String, pan: String,userId:String): Flow<KycResponse?>{
        return flow {
            val response1 = safeApiRequest{ apiService.verifyPan(url , VerifyPanRequest(pan , userId))}
            emit(response1)
        }
    }
    suspend fun verifyAadhar(url: String, aadhar: String): Flow<AadhaarResponse?>{
        return flow {
            val response = safeApiRequest { apiService.generateAadhaarOtp(url,AadhaarRequest(aadhar))}
            emit(response)
        }
    }

    suspend fun verifyAadharOTP(url: String, body: VerifyAadhaarOtpRequest): Flow<KycResponse>{
        return flow {
            val response = safeApiRequest {apiService.verifyAadhaarOtp(url,body)}
            emit(response)
        }
    }
//    suspend fun uploadLiveliness(
//        imagePart: MultipartBody.Part,
//        verificationIdPart: RequestBody,
//        userIdPart: RequestBody,
//        imgUrlPart: RequestBody
//    ): Flow<KycResponse> {
//        Log.d("ImageUploaded","repository")
//        return flow {
//            val response = safeApiRequest {apiService.uploadLiveliness(imagePart , verificationIdPart , userIdPart , imgUrlPart)}
//            emit(response)
//        }
//    }

    suspend fun getKycStatus(url: String): Flow<KycStatusResponse>{
        return flow {
            val response = safeApiRequest { apiService.getKycStatus(url) }
            emit(response)
        }
    }

}