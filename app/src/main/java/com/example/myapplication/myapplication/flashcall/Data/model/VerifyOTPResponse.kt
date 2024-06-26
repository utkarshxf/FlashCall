package com.example.myapplication.myapplication.flashcall.Data.model

import com.google.gson.annotations.SerializedName

data class VerifyOTPResponse(
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("sessionToken")
    val token: String? = null
)
