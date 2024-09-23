package com.example.myapplication.myapplication.flashcall.Data.model.kycStatus

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class FaceMatch(
    @SerialName("reference_id")
    var referenceId: Int?,
    @SerialName("verification_id")
    var verificationId: String?,
    @SerialName("status")
    var status: String?,
    @SerialName("face_match_result")
    var faceMatchResult: String?,
    @SerialName("face_match_score")
    var faceMatchScore: Double?,
    @SerialName("_id")
    var id: String?
)