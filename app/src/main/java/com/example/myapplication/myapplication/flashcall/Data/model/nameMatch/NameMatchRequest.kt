package com.example.myapplication.myapplication.flashcall.Data.model.nameMatch

data class NameMatchRequest(
    val name1: String,
    val name2: String,
    val userId: String,
    val verificationId: String
)