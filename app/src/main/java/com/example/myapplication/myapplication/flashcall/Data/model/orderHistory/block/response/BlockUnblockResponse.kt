package com.example.myapplication.myapplication.flashcall.Data.model.orderHistory.block.response

data class BlockUnblockResponse(
    val action: String,
    val blocked: List<String>,
    val success: Boolean
)