package com.example.myapplication.myapplication.flashcall.Data.model.aadharOtpResponse


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AadharOtpResponse(
    @SerialName("data")
    var `data`: Data?,
    @SerialName("success")
    var success: Boolean?
)