package com.example.myapplication.myapplication.flashcall.repository

import android.util.Log
import com.example.myapplication.myapplication.flashcall.Data.SendOTP
import com.example.myapplication.myapplication.flashcall.Data.VerifyOTP
import com.example.myapplication.myapplication.flashcall.Data.model.APIResponse
import com.example.myapplication.myapplication.flashcall.Data.model.CreateUser
import com.example.myapplication.myapplication.flashcall.Data.model.CreateUserResponse
import com.example.myapplication.myapplication.flashcall.Data.model.IsUserCreatedResponse
import com.example.myapplication.myapplication.flashcall.Data.model.Request
import com.example.myapplication.myapplication.flashcall.Data.model.ResendOTPResponse
import com.example.myapplication.myapplication.flashcall.Data.model.ResendRequest
import com.example.myapplication.myapplication.flashcall.Data.model.SendOTPResponseX
import com.example.myapplication.myapplication.flashcall.Data.model.ValidateRequest
import com.example.myapplication.myapplication.flashcall.Data.model.VerifyOTPResponse
import com.example.myapplication.myapplication.flashcall.Data.model.VerifyRequest
import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.ValidateResponse
import com.example.myapplication.myapplication.flashcall.Data.network.APIService
import com.example.myapplication.myapplication.flashcall.utils.SafeApiRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(private val apiService: APIService) : IAuthRepo , SafeApiRequest() {

    override suspend fun sendOtp(url:String,number: String): Flow<SendOTPResponseX?> {
        return flow {
            val response1 = safeApiRequest{ apiService.sendOTP(url , Request(number))}
            emit(response1)
//            val abc = Request(number)
//            Log.e("phone4", "$number $url $abc"  )
//            val response = apiService.sendOTP(url, Request(number))
//            Log.e("phone3", response.toString())
//            if(response.isSuccessful){
//                emit(response.body()!!)
//            }else{
//                emit(null)
//            }
        }.flowOn(Dispatchers.IO)

    }

    override suspend fun resendOtp(url:String,number: String): Flow<ResendOTPResponse>{

        return flow{
            val response = safeApiRequest {  apiService.resendOTP(url, ResendRequest(number))}
            emit(response)
        }.flowOn(Dispatchers.IO)

    }

    override suspend fun verifyOtp(url:String,number: String, otp: String): Flow<VerifyOTPResponse>{

        return flow{
            val response = safeApiRequest {  apiService.verifyOTP(url, VerifyRequest(number,otp))}
            emit(response)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun validateUser(url:String, token: String): Flow<ValidateResponse>{

        return flow {
            val response = safeApiRequest {  apiService.validateUser(url, ValidateRequest(token))}
            emit(response)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun isCreatedUser(url:String, phone: String): Flow<IsUserCreatedResponse?> {

        return flow{
            val response = safeApiRequest { apiService.isCreatedUser(url, Request(phone))}
            emit(response)
//            if(response.isSuccessful){
//                emit(response.body()!!)
//            }else{
//                emit(null)
//            }
        }.flowOn(Dispatchers.IO)
    }
}




