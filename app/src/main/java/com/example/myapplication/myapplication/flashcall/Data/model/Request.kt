package com.example.myapplication.myapplication.flashcall.Data.model

import com.google.gson.annotations.SerializedName

data class Request(
    @SerializedName("phone")
    val phone : String
)

data class ResendRequest(
    @SerializedName("phone")
    val phone : String
)

data class VerifyRequest(
    @SerializedName("phone")
    val phone : String,
    @SerializedName("otp")
    val otp : String
)

data class CreateUser(

    @SerializedName("username")
    val username : String,
    @SerializedName("phone")
    val phone : String,
    @SerializedName("fullName")
    val fullName : String,
    @SerializedName("firstName")
    val firstName : String,
    @SerializedName("lastName")
    val lastName : String,
    @SerializedName("photo")
    val photo : String,
    @SerializedName("profession")
    val profession : String,
    @SerializedName("themeSelected")
    val themeSelected : String,
    @SerializedName("videoRate")
    val videoRate : String,
    @SerializedName("audioRate")
    val audioRate : String,
    @SerializedName("chatRate")
    val chatRate : String,
    @SerializedName("gender")
    val gender : String,
    @SerializedName("dob")
    val dob : String,
    @SerializedName("bio")
    val bio : String,
    @SerializedName("kyc_status")
    val kyc_status : String
)


data class ValidateRequest(
    @SerializedName("token")
    val token : String
)

data class UpdateUserRequest(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("user")
    val user: UserUpdateData
)
data class UserUpdateData(
    @SerializedName("fullName")
    val fullName: String?=null,
    @SerializedName("username")
    val username: String?=null,
    @SerializedName("phone")
    val phone: String?=null,
    @SerializedName("firstName")
    val firstName: String?=null,
    @SerializedName("lastName")
    val lastName: String?=null,

    @SerializedName("photo")
    val photo: String?=null,
    @SerializedName("profession")
    val profession: String?=null,
    @SerializedName("themeSelected")
    val themeSelected: String?=null,
    @SerializedName("videoRate")
    val videoRate: String?=null,
    @SerializedName("audioRate")
    val audioRate: String?=null,
    @SerializedName("chatRate")
    val chatRate: String?=null,
    @SerializedName("gender")
    val gender: String?=null,
    @SerializedName("dob")
    val dob: String?=null,
    @SerializedName("bio")
    val bio: String?=null
)

