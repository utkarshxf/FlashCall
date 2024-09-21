package com.example.myapplication.myapplication.flashcall.Data.model

data class AadhaarResponse(
    val `data`: Data,
    val success: Boolean
)

data class Data(
    val message: String,
    val ref_id: String,
    val status: String
)