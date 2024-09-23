package com.example.myapplication.myapplication.flashcall.Data.model.deleteAdditionalLink


import com.example.myapplication.myapplication.flashcall.Data.model.LinkData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeletedAdditionalLinksResponse(
    @SerialName("audioAllowed")
    var audioAllowed: Boolean?,
    @SerialName("audioRate")
    var audioRate: String?,
    @SerialName("bio")
    var bio: String?,
    @SerialName("chatAllowed")
    var chatAllowed: Boolean?,
    @SerialName("chatRate")
    var chatRate: String?,
    @SerialName("createdAt")
    var createdAt: String?,
    @SerialName("creatorId")
    var creatorId: String?,
    @SerialName("dob")
    var dob: String?,
    @SerialName("firstName")
    var firstName: String?,
    @SerialName("fullName")
    var fullName: String?,
    @SerialName("gender")
    var gender: String?,
    @SerialName("_id")
    var id: String?,
    @SerialName("kyc_status")
    var kycStatus: String?,
    @SerialName("lastName")
    var lastName: String?,
    @SerialName("links")
    var links: List<LinkData>?,
    @SerialName("phone")
    var phone: String?,
    @SerialName("photo")
    var photo: String?,
    @SerialName("profession")
    var profession: String?,
    @SerialName("referralAmount")
    var referralAmount: Int?,
    @SerialName("referredBy")
    var referredBy: Any?,
    @SerialName("themeSelected")
    var themeSelected: String?,
    @SerialName("updatedAt")
    var updatedAt: String?,
    @SerialName("username")
    var username: String?,
    @SerialName("__v")
    var v: Int?,
    @SerialName("videoAllowed")
    var videoAllowed: Boolean?,
    @SerialName("videoRate")
    var videoRate: String?,
    @SerialName("walletBalance")
    var walletBalance: Double?
)