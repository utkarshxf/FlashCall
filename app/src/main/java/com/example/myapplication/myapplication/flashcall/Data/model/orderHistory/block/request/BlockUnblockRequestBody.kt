package com.example.myapplication.myapplication.flashcall.Data.model.orderHistory.block.request

data class BlockUnblockRequestBody(
    val action: String,
    val blockClientId: String
)