package com.example.myapplication.myapplication.flashcall.Data.model.paymentSetting


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddUpiRequest(
    @SerialName("userId")
    var userId: String?,
    @SerialName("vpa")
    var vpa: String?
)