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
    val otp : String,
    @SerializedName("token")
    val token : String
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