package com.example.myapplication.myapplication.flashcall.Data.model

import com.example.myapplication.myapplication.flashcall.Data.model.nameMatch.Data
import com.google.gson.annotations.SerializedName

data class AadhaarResponse(
    @SerializedName("data")
    val `data`: aadharData?,
    @SerializedName("success")
    val success: Boolean?
)

data class aadharData(
    @SerializedName("message")
    val message: String?,
    @SerializedName("ref_id")
    val ref_id: String?,
    @SerializedName("status")
    val status: String?
)