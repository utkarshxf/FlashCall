package com.example.myapplication.myapplication.flashcall.Data

data class SendOTP(
    val phone: String
)

data class VerifyOTP(
    val phone: String,
    val otp: String
)


data class ResendOTP(
    val phone: String
)

