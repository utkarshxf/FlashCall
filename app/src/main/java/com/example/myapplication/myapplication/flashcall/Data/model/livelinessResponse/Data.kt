package com.example.myapplication.myapplication.flashcall.Data.model.livelinessResponse


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Data(
    @SerialName("liveliness")
    var liveliness: Boolean?,
    @SerialName("reference_id")
    var referenceId: Int?,
    @SerialName("score")
    var score: Double?,
    @SerialName("status")
    var status: String?,
    @SerialName("verification_id")
    var verificationId: String?
)