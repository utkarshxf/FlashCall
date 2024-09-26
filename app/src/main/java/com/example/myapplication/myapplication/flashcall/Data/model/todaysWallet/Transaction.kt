package com.example.myapplication.myapplication.flashcall.Data.model.todaysWallet


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Transaction(
    @SerialName("amount")
    var amount: Double?,
    @SerialName("createdAt")
    var createdAt: String?,
    @SerialName("_id")
    var id: String?,
    @SerialName("type")
    var type: String?,
    @SerialName("updatedAt")
    var updatedAt: String?,
    @SerialName("userId")
    var userId: String?,
    @SerialName("userType")
    var userType: String?,
    @SerialName("__v")
    var v: Int?
)