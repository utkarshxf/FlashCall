package com.example.myapplication.myapplication.flashcall.Data.model

data class VerifyAadhaarOtpRequest(
    val otp: String,
    val aadhaarNumber: String,
    val ref_id: String,
    val userId: String
)