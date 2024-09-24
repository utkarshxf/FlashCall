package com.example.myapplication.myapplication.flashcall.Data.model.paymentSetting


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BankDetails(
    @SerialName("accountType")
    var accountType: String?,
    @SerialName("bank_account")
    var bankAccount: String?,
    @SerialName("ifsc")
    var ifsc: String?
)