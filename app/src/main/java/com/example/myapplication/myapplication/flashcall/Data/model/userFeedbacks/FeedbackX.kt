package com.example.myapplication.myapplication.flashcall.Data.model.userFeedbacks


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeedbackX(
    @SerialName("clientId")
    var clientId: ClientId?,
    @SerialName("createdAt")
    var createdAt: String?,
    @SerialName("feedback")
    var feedback: String?,
    @SerialName("position")
    var position: Int?,
    @SerialName("rating")
    var rating: Int?,
    @SerialName("showFeedback")
    var showFeedback: Boolean?
)