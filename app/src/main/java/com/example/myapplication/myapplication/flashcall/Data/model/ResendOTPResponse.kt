package com.example.myapplication.myapplication.flashcall.Data.model

import com.google.gson.annotations.SerializedName

class ResendOTPResponse(
    @SerializedName("message")
    val message: String? = null,
    @SerializedName( "token")
    val token: String? = null
)
{

}