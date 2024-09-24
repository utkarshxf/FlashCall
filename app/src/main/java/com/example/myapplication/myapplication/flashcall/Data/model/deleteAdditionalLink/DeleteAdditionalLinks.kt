package com.example.myapplication.myapplication.flashcall.Data.model.deleteAdditionalLink


import com.example.myapplication.myapplication.flashcall.Data.model.LinkData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeleteAdditionalLinks(
    @SerialName("link")
    var link: LinkData?,
    @SerialName("userId")
    var userId: String?
)