package com.example.myapplication.myapplication.flashcall.Data.model.orderHistory

data class Data(
    val callId: String,
    val creator: String,
    val duration: Int,
    val endedAt: String,
    val members: List<String>,
    val startedAt: String,
    val status: String
)