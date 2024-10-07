package com.example.myapplication.myapplication.flashcall.Data.model.feedback

data class UpdateFeedbackCreatorRequest(
    val clientId: String?=null,
    val createdAt: String?=null,
    val creatorId: String?=null,
    val feedbackText: String?=null,
    val rating: Int?=null,
    val showFeedback: Boolean?=null,
    val position: Int?=null
)
data class UpdateFeedbackCallRequest(
    val callId: String? = null,
    val clientId: String?=null,
    val createdAt: String?=null,
    val creatorId: String?=null,
    val feedbackText: String?=null,
    val rating: Int?=null,
    val showFeedback: Boolean?=null,
    val position: Int?=null
)
