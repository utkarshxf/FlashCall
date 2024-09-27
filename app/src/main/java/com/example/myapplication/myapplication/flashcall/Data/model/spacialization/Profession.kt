package com.example.myapplication.myapplication.flashcall.Data.model.spacialization


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Profession(
    @SerialName("icon")
    var icon: String?,
    @SerialName("id")
    var id: String?,
    @SerialName("name")
    var name: String?
)