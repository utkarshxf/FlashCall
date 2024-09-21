package com.example.myapplication.myapplication.flashcall.Data.model

data class PanResponse(
    val `data`: PanData,
    val success: Boolean
)
data class PanData(
    val father_name: String,
    val message: String,
    val name_provided: String,
    val pan: String,
    val reference_id: Int,
    val registered_name: String,
    val type: String,
    val valid: Boolean
)