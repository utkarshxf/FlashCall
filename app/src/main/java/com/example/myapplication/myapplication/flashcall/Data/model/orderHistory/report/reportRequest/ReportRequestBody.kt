package com.example.myapplication.myapplication.flashcall.Data.model.orderHistory.report.reportRequest

data class ReportRequestBody(
    val callId: String,
    val client: String,
    val creator: String,
    val issue: String,
    val submittedBy: SubmittedBy
)