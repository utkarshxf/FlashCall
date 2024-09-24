package com.example.myapplication.myapplication.flashcall.Data.model.paymentSetting


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentSettingResponse(
    @SerialName("data")
    var `data`: PaymentSettingData?,
    @SerialName("success")
    var success: Boolean?
)