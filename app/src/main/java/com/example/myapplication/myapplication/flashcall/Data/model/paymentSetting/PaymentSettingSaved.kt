package com.example.myapplication.myapplication.flashcall.Data.model.paymentSetting


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentSettingSaved(
    @SerialName("data")
    var `data`: String?,
    @SerialName("success")
    var success: Boolean?
)

data class LocalPaymentSetting(
    var isPayment: Boolean = false,
    var paymentMode: String = "",
    var vpa: String = "",
    var accountNumber: String ="",
    var ifsc: String = ""
)