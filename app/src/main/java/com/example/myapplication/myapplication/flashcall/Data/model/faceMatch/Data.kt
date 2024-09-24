package com.example.myapplication.myapplication.flashcall.Data.model.faceMatch


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Data(
    @SerialName("face_match_result")
    var faceMatchResult: String,
    @SerialName("face_match_score")
    var faceMatchScore: Int,
    @SerialName("reference_id")
    var referenceId: String,
    @SerialName("status")
    var status: String,
    @SerialName("verification_id")
    var verificationId: String
)