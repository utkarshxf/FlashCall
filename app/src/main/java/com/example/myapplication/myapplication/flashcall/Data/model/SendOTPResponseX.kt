package com.example.myapplication.myapplication.flashcall.Data.model


import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

data class SendOTPResponseX(

    @SerializedName("message")
    val message: String? = null,
    @SerializedName( "token")
    val token: String? = null

)

