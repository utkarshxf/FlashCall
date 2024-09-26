package com.example.myapplication.myapplication.flashcall.Data.model.kycStatus


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Liveliness(
    @SerialName("_id")
    var id: String?,
    val img_url: String,
    @SerialName("liveliness")
    var liveliness: Boolean?,
    @SerialName("reference_id")
    var referenceId: String?,
    @SerialName("status")
    var status: String?,
    @SerialName("verification_id")
    var verificationId: String?
)