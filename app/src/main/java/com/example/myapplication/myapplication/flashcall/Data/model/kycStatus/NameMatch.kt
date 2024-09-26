package com.example.myapplication.myapplication.flashcall.Data.model.kycStatus


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NameMatch(
    @SerialName("_id")
    var id: String?,
    @SerialName("name_1")
    var name1: String?,
    @SerialName("name_2")
    var name2: String?,
    @SerialName("reason")
    var reason: String?,
    @SerialName("reference_id")
    var referenceId: Int?,
    @SerialName("score")
    var score: String?,
    @SerialName("status")
    var status: String?,
    @SerialName("verification_id")
    var verificationId: String?
)