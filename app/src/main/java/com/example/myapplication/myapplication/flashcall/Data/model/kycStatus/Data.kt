package com.example.myapplication.myapplication.flashcall.Data.model.kycStatus


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Data(
    @SerialName("aadhaar")
    var aadhaar: Aadhaar?,
    @SerialName("face_match")
    var faceMatch: FaceMatch?,
    @SerialName("_id")
    var id: String?,
    @SerialName("liveliness")
    var liveliness: Liveliness?,
    @SerialName("name_match")
    var nameMatch: NameMatch?,
    @SerialName("pan")
    var pan: Pan?,
    @SerialName("userId")
    var userId: String?,
    @SerialName("__v")
    var v: Int?
)