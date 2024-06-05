package com.example.myapplication.myapplication.flashcall.repository

import com.example.myapplication.myapplication.flashcall.Data.model.APIResponse
import com.example.myapplication.myapplication.flashcall.Data.model.CreateUserResponse
import com.example.myapplication.myapplication.flashcall.Data.model.ResendOTPResponse
import com.example.myapplication.myapplication.flashcall.Data.model.SendOTPResponseX
import com.example.myapplication.myapplication.flashcall.Data.model.VerifyOTPResponse
import kotlinx.coroutines.flow.Flow

interface IAuthRepo {

    suspend fun sendOtp(url: String, number: String): Flow<SendOTPResponseX?>

    suspend fun resendOtp(url: String, number: String): Flow<ResendOTPResponse>

    suspend fun verifyOtp(url: String, number: String, otp: String, token: String): Flow<VerifyOTPResponse>


}