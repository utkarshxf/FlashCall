package com.example.myapplication.myapplication.flashcall.Screens.common

fun maskIfPhoneNumber(input: String?): String {
    return input?.replace(Regex("""(\+91\d{5})\d{5}""")) {
        it.groupValues[1] + "XXXXX"
    } ?: "UNKNOWN"
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