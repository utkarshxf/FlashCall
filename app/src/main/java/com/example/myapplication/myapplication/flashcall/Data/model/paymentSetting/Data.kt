package com.example.myapplication.myapplication.flashcall.Data.model.paymentSetting


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentSettingData(
    @SerialName("bankDetails")
    var bankDetails: BankDetails?,
    @SerialName("_id")
    var id: String?,
    @SerialName("paymentMode")
    var paymentMode: String?,
    @SerialName("upiId")
    var upiId: String?,
    @SerialName("userId")
    var userId: String?,
    @SerialName("__v")
    var v: Int?
)