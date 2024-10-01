package com.example.myapplication.myapplication.flashcall.Data.model.editAdditionalLink


import com.example.myapplication.myapplication.flashcall.Data.model.LinkData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EditUserLink(
    @SerialName("links")
    var links: List<LinkData>?
)