package com.example.myapplication.myapplication.flashcall.Data.model.faceMatch


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FaceMatchRequest(
    @SerialName("first_img")
    var firstImg: String,
    @SerialName("second_img")
    var secondImg: String,
    @SerialName("userId")
    var userId: String,
    @SerialName("verificationId")
    var verificationId: String
)