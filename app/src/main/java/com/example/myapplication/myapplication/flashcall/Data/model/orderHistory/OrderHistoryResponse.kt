package com.example.myapplication.myapplication.flashcall.Data.model.orderHistory

data class OrderHistoryResponse(
    val calls: List<Call>,
    val hasMore: Boolean,
    val totalCalls: Int
)