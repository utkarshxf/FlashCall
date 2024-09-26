package com.example.myapplication.myapplication.flashcall.Data.model.userFeedbacks


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClientId(
    @SerialName("bio")
    var bio: String?,
    @SerialName("createdAt")
    var createdAt: String?,
    @SerialName("dob")
    var dob: String?,
    @SerialName("firstName")
    var firstName: String?,
    @SerialName("gender")
    var gender: String?,
    @SerialName("_id")
    var id: String?,
    @SerialName("lastName")
    var lastName: String?,
    @SerialName("onlineStatus")
    var onlineStatus: Boolean?,
    @SerialName("phone")
    var phone: String?,
    @SerialName("photo")
    var photo: String?,
    @SerialName("role")
    var role: String?,
    @SerialName("updatedAt")
    var updatedAt: String?,
    @SerialName("username")
    var username: String?,
    @SerialName("__v")
    var v: Int?,
    @SerialName("walletBalance")
    var walletBalance: Double?
)