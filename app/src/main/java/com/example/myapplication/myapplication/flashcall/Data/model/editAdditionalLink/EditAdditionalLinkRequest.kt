package com.example.myapplication.myapplication.flashcall.Data.model.editAdditionalLink


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EditAdditionalLinkRequest(
    @SerialName("user")
    var user: EditUserLink?,
    @SerialName("userId")
    var userId: String?
)