package com.example.myapplication.myapplication.flashcall.Data.model.feedback

data class UpdateFeedback(
    val clientId: String?=null,
    val createdAt: String?=null,
    val creatorId: String?=null,
    val feedbackText: String?=null,
    val rating: Int?=null,
    val showFeedback: Boolean?=null
)