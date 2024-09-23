package com.example.myapplication.myapplication.flashcall.Data.model.kycStatus


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KycStatusResponse(
    @SerialName("data")
    var `data`: Data?,
    @SerialName("success")
    var success: Boolean?
)