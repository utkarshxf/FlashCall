package com.example.myapplication.myapplication.flashcall.Data.model.spacialization


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpacializationResponse(
    @SerialName("professions")
    var professions: List<Profession?>?
)