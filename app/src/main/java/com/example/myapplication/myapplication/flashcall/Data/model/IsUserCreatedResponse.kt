package com.example.myapplication.myapplication.flashcall.Data.model

import com.google.gson.annotations.SerializedName


data class IsUserCreatedResponse(
    @SerializedName("_id")
    val _id: String?,
    @SerializedName("username")
    val username: String?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("fullName")
    val fullName: String?,
    @SerializedName("firstName")
    val firstName: String?,
    @SerializedName("lastName")
    val lastName: String?,
    @SerializedName("photo")
    val photo: String?,
    @SerializedName("themeSelected")
    val themeSelected: String?,
    @SerializedName("bio")
    val bio: String?,
    @SerializedName("profession")
    val profession: String?,
    @SerializedName("dob")
    val dob: String?,
    @SerializedName("gender")
    val gender: String?,
    @SerializedName("walletBalance")
    val walletBalance: Double?,
    @SerializedName("audioAllowed")
    val audioAllowed: Boolean?,
    @SerializedName("chatAllowed")
    val chatAllowed: Boolean?,
    @SerializedName("videoAllowed")
    val videoAllowed: Boolean?,
    @SerializedName("userType")
    val userType: String?,
    @SerializedName("message")
    val message: String?
)

