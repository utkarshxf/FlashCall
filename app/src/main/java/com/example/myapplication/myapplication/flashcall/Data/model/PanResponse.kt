package com.example.myapplication.myapplication.flashcall.Data.model

import com.google.gson.annotations.SerializedName

data class PanResponse(
    @SerializedName("data")
    val data: PanData? = null,
    @SerializedName( "success")
    val success: Boolean? = null
)
data class PanData(
    @SerializedName("father_name")
    val father_name: String?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("name_provided")
    val name_provided: String?,
    @SerializedName("pan")
    val pan: String?,
    @SerializedName("reference_id")
    val reference_id: Int?,
    @SerializedName("registered_name")
    val registered_name: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("valid")
    val valid: Boolean?
)
