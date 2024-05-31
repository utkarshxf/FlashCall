package com.example.myapplication.myapplication.flashcall.Data.model

import com.google.gson.annotations.SerializedName

data class Request(
    @SerializedName("phone")
    val phone : String
)

data class ResendRequest(
    @SerializedName("phone")
    val phone : String
)

data class VerifyRequest(
    @SerializedName("phone")
    val phone : String,
    @SerializedName("otp")
    val otp : String,
    @SerializedName("token")
    val token : String
)
