package com.example.myapplication.myapplication.flashcall.Data.model.livelinessResponse


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LivelinessResponse(
    @SerialName("data")
    var `data`: Data?,
    @SerialName("success")
    var success: Boolean?
)