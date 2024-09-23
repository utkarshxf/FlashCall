package com.example.myapplication.myapplication.flashcall.Data.model.todaysWallet


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TodaysWalletBalanceResponse(
    @SerialName("transactions")
    var transactions: List<Transaction?>?
)