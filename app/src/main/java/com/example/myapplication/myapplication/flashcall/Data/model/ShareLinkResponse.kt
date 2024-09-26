package com.example.myapplication.myapplication.flashcall.Data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShareLinkResponse(
    @SerialName("creatorId")
    var creatorId: String?,
    @SerialName("creatorLink")
    var creatorLink: String?
)