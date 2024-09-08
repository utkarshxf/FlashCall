package com.example.myapplication.myapplication.flashcall.Data.model.feedback

data class FeedbackResponse(
    val feedbacks: List<Feedback>
)
data class Feedback(
    val _id: String?=null,
    val creatorId: CreatorId?=null,
    val feedbacks: List<FeedbackX>?=null
)
data class FeedbackX(
    val clientId: ClientId?=null,
    val createdAt: String?=null,
    val feedback: String?=null,
    val position: Int?=null,
    val rating: Int?=null,
    val showFeedback: Boolean?=null
)
data class ClientId(
    val __v: Int?=null,
    val _id: String?=null,
    val bio: String?=null,
    val createdAt: String?=null,
    val firstName: String?=null,
    val lastName: String?=null,
    val onlineStatus: Boolean?=null,
    val phone: String?=null,
    val photo: String?=null,
    val role: String?=null,
    val updatedAt: String?=null,
    val username: String?=null,
    val walletBalance: Double?=null
)
data class CreatorId(
    val __v: Int?=null,
    val _id: String?=null,
    val audioAllowed: Boolean?=null,
    val audioRate: String?=null,
    val bio: String?=null,
    val chatAllowed: Boolean?=null,
    val chatRate: String?=null,
    val createdAt: String?=null,
    val creatorId: String?=null,
    val dob: String?=null,
    val firstName: String?=null,
    val fullName: String?=null,
    val gender: String?=null,
    val kyc_status: String?=null,
    val lastName: String?=null,
    val links: List<Any>?=null,
    val phone: String?=null,
    val photo: String?=null,
    val profession: String?=null,
    val referralAmount: Int?=null,
    val referredBy: Any?=null,
    val themeSelected: String?=null,
    val updatedAt: String?=null,
    val username: String?=null,
    val videoAllowed: Boolean?=null,
    val videoRate: String?=null,
    val walletBalance: Double?=null
)