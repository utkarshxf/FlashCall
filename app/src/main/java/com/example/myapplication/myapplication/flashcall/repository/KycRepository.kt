package com.example.myapplication.myapplication.flashcall.repository

import com.example.myapplication.myapplication.flashcall.Data.model.AadhaarRequest
import com.example.myapplication.myapplication.flashcall.Data.model.AadhaarResponse
import com.example.myapplication.myapplication.flashcall.Data.model.KYCResponse
import com.example.myapplication.myapplication.flashcall.Data.model.PanResponse
import com.example.myapplication.myapplication.flashcall.Data.model.VerifyAadhaarOtpRequest
import com.example.myapplication.myapplication.flashcall.Data.model.VerifyPanRequest
import com.example.myapplication.myapplication.flashcall.Data.network.APIService
import com.example.myapplication.myapplication.flashcall.Screens.imageUrl
import com.example.myapplication.myapplication.flashcall.utils.SafeApiRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.await
import javax.inject.Inject

class KycRepository @Inject constructor(private val apiService: APIService): SafeApiRequest() {

    suspend fun verifyPan(pan: String,userId:String): Flow<PanResponse>{
        return flow {
            val response = safeApiRequest { apiService.verifyPan(VerifyPanRequest(pan , userId)).await()}
            emit(response)
        }
    }
    suspend fun verifyAadhar(aadhar: String): Flow<AadhaarResponse>{
        return flow {
            val response = safeApiRequest { apiService.generateAadhaarOtp(AadhaarRequest(aadhar))}
            emit(response)
        }
    }

    suspend fun verifyAadharOTP(otp: String , ref_id:String ,userId:String): Flow<KYCResponse>{
        return flow {
            val response = safeApiRequest {apiService.verifyAadhaarOtp(VerifyAadhaarOtpRequest(otp , ref_id , userId))}
            emit(response)
        }
    }
    suspend fun uploadLiveliness(
        imagePart: MultipartBody.Part,
        verificationIdPart: RequestBody,
        userIdPart: RequestBody,
        imgUrlPart: RequestBody
    ): Flow<KYCResponse>{
        return flow {
            val response = safeApiRequest {apiService.uploadLiveliness(imagePart , verificationIdPart , userIdPart , imgUrlPart)}
            emit(response)
        }
    }

}