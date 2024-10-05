package com.example.myapplication.myapplication.flashcall.Data.model.userFeedbacks


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


data class FeedbackResponseItem(
    val _id: String?,
    val callId: String?,
    val creatorId: String?,
    val feedback: FeedbackItem?
)

data class FeedbackItem(
    val clientId: Client?,
    val rating: Int?,
    val feedback: String?,
    val showFeedback: Boolean?,
    val createdAt: String?,
    val position: Int?
)

data class Client(
    val _id: String?,
    val username: String?,
    val onlineStatus: Boolean?,
    val phone: String?,
    val firstName: String?,
    val lastName: String?,
    val photo: String?,
    val role: String?,
    val bio: String?,
    val walletBalance: Double?,
    val createdAt: String?,
    val updatedAt: String?,
    val __v: Int?,
    val dob: String?,
    val gender: String?
)