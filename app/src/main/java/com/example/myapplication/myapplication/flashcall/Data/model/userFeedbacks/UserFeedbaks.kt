package com.example.myapplication.myapplication.flashcall.Data.model.userFeedbacks


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserFeedbaks(
    @SerialName("feedbacks")
    var feedbacks: List<Feedback>?
)