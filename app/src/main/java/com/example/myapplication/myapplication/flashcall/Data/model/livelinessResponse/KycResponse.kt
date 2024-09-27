package com.example.myapplication.myapplication.flashcall.Data.model.livelinessResponse


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KycResponse(
    @SerialName("message")
    var `message`: String?,
    @SerialName("success")
    var success: Boolean?,
    @SerialName("kycStatus")
    var kycStatus: Boolean = false
)