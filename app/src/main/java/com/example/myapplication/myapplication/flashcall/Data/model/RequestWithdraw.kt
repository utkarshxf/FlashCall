package com.example.myapplication.myapplication.flashcall.Data.model

import android.webkit.ConsoleMessage
import com.example.myapplication.myapplication.flashcall.Data.model.wallet.UserId


data class RequestWithdraw(
    val userId: String,
    val phoneNumber: String
)

data class RequestWithdrawResponse(
    val success: Boolean? = false,
    val message: String? = null
)