package com.example.myapplication.myapplication.flashcall.Data.model.orderHistory

data class Call(
    val __v: Int,
    val _id: String,
    val callId: String,
    val creator: String,
    val duration: String,
    val endedAt: String,
    val expertDetails: Any,
    val members: List<Member>,
    val startedAt: String,
    val status: String,
    val type: String
)