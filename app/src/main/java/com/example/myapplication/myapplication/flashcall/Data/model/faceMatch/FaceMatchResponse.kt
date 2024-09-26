package com.example.myapplication.myapplication.flashcall.Data.model.faceMatch


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FaceMatchResponse(
    @SerialName("data")
    var `data`: Data,
    @SerialName("success")
    var success: Boolean
)