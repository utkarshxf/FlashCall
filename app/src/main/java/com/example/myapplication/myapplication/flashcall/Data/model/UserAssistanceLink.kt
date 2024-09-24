package com.example.myapplication.myapplication.flashcall.Data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserAssistanceLink(
    @SerialName("description")
    var description: String?,
    @SerialName("link")
    var link: String?
)