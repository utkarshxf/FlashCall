package com.example.myapplication.myapplication.flashcall.Data.model

import com.google.gson.annotations.SerializedName

data class AadhaarRequest(
    @SerializedName("aadhaarNumber")
    val aadhaarNumber: String
)