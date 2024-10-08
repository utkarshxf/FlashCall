package com.example.myapplication.myapplication.flashcall.Data.model.orderHistory.report.responseBody

data class ReportResponseBody(
    val __v: Int,
    val _id: String,
    val callId: String,
    val client: String,
    val createdAt: String,
    val creator: String,
    val issue: String,
    val status: String,
    val submittedBy: SubmittedBy,
    val updatedAt: String
)