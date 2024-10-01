package com.example.myapplication.myapplication.flashcall.Screens.common

fun maskIfPhoneNumber(input: String?): String {
    return when {
        input == null -> "Unknown"
        input.all { it.isDigit() } -> maskPhoneNumber(input)
        else -> input
    }
}

private fun maskPhoneNumber(phoneNumber: String): String {
    return when {
        phoneNumber.length <= 4 -> phoneNumber
        else -> {
            val lastFourDigits = phoneNumber.takeLast(4)
            val maskedPart = "X".repeat(phoneNumber.length - 4)
            "$maskedPart$lastFourDigits"
        }
    }
}