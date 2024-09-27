package com.example.myapplication.myapplication.flashcall.Data.model.userFeedbacks


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Feedback(
    @SerialName("callId")
    var callId: String?,
    @SerialName("creatorId")
    var creatorId: String?,
    @SerialName("feedbacks")
    var feedbacks: List<FeedbackX>?,
    @SerialName("_id")
    var id: String?
)