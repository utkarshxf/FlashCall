package com.example.myapplication.myapplication.flashcall.Data.model.paymentSetting


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddBankDetailsRequest(
    @SerialName("bank_account")
    var bank_account: String?,
    @SerialName("ifsc")
    var ifsc: String?,
    @SerialName("userId")
    var userId: String?
)