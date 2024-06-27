package com.example.myapplication.myapplication.flashcall.repository

import com.example.myapplication.myapplication.flashcall.Data.SendOTP
import com.example.myapplication.myapplication.flashcall.Data.VerifyOTP
import com.example.myapplication.myapplication.flashcall.Data.model.APIResponse
import com.example.myapplication.myapplication.flashcall.Data.model.CreateUser
import com.example.myapplication.myapplication.flashcall.Data.model.CreateUserResponse
import com.example.myapplication.myapplication.flashcall.Data.model.Request
import com.example.myapplication.myapplication.flashcall.Data.model.ResendOTPResponse
import com.example.myapplication.myapplication.flashcall.Data.model.ResendRequest
import com.example.myapplication.myapplication.flashcall.Data.model.SendOTPResponseX
import com.example.myapplication.myapplication.flashcall.Data.model.ValidateRequest
import com.example.myapplication.myapplication.flashcall.Data.model.VerifyOTPResponse
import com.example.myapplication.myapplication.flashcall.Data.model.VerifyRequest
import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.ValidateResponse
import com.example.myapplication.myapplication.flashcall.Data.network.APIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(private val apiService: APIService) : IAuthRepo {

    override suspend fun sendOtp(url:String,number: String): Flow<SendOTPResponseX?> {

        return flow{
            val response = apiService.sendOTP(url, Request(number))
            if(response.isSuccessful){
                emit(response.body()!!)
            }else{
                emit(null)
            }
        }.flowOn(Dispatchers.IO)


    }

    override suspend fun resendOtp(url:String,number: String): Flow<ResendOTPResponse>{

        return flow{
            val response = apiService.resendOTP(url, ResendRequest(number))
            emit(response)
        }.flowOn(Dispatchers.IO)

    }

    override suspend fun verifyOtp(url:String,number: String, otp: String, token: String): Flow<VerifyOTPResponse>{

        return flow{
            val response = apiService.verifyOTP(url, VerifyRequest(number,otp,token))
            emit(response)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun validateUser(url:String, token: String): Flow<ValidateResponse>{

        return flow {
            val response = apiService.validateUser(url, ValidateRequest(token))
            emit(response)
        }.flowOn(Dispatchers.IO)
    }




}




