package com.example.myapplication.myapplication.flashcall.Data.model


data class UpdateUserResponse(
    val updatedUser: UpdatedUser
)

data class UpdatedUser(
    val _id: String,
    val username: String,
    val phone: String,
    val fullName: String,
    val firstName: String,
    val lastName: String,
    val photo: String,
    val profession: String,
    val themeSelected: String,
    val videoRate: String,
    val audioRate: String,
    val chatRate: String,
    val videoAllowed: Boolean,
    val audioAllowed: Boolean,
    val chatAllowed: Boolean,
    val gender: String,
    val dob: String,
    val bio: String,
    val kyc_status: String,
    val walletBalance: Int,
    val referredBy: String?,
    val referralAmount: Int,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)
