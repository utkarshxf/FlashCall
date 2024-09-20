package com.example.myapplication.myapplication.flashcall.repository

import com.example.myapplication.myapplication.flashcall.Data.network.APIService
import com.example.myapplication.myapplication.flashcall.utils.SafeApiRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class KycRepository @Inject constructor(private val apiService: APIService): SafeApiRequest() {

    suspend fun verifyPan(pan: String): Flow<String>{
        return flow {
            val response = ""
            emit(response)
        }
    }
    suspend fun verifyAadhar(aadhar: String): Flow<String>{
        return flow {
            val response = ""
            emit(response)
        }
    }

    suspend fun verifyAadharOTP(otp: String): Flow<String>{
        return flow {
            val response = ""
            emit(response)
        }
    }

}