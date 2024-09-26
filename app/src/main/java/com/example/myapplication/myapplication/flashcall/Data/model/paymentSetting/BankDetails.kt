package com.example.myapplication.myapplication.flashcall.Data.model.paymentSetting


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BankDetails(
    @SerialName("accountNumber")
    var accountNumber: String?,
    @SerialName("ifsc")
    var ifsc: String?
)