package com.example.myapplication.myapplication.flashcall.Data.useCase

import com.example.myapplication.myapplication.flashcall.Data.model.SendOTPResponseX
import com.example.myapplication.myapplication.flashcall.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

//class SendOtpUseCase @Inject constructor(private val repository: AuthRepository) {
//
//    suspend fun invoke(phoneNumber: String) : Flow<SendOTPResponseX> {
//
//        if (!isValidPhoneNumber(phoneNumber)) {
//            return Result.failure(Exception("Invalid phone number"))
//        }
//
//        return repository.sendOtp(phoneNumber)
//
//    }
//
//    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
//        return phoneNumber.length == 10
//    }
//}