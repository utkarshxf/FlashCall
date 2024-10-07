package com.example.myapplication.myapplication.flashcall.Screens.common

fun maskIfPhoneNumber(input: String?): String {
    return if (input != null && input.startsWith("+91")) {
        input.dropLast(5) + "XXXXX"
    }else{
        input?:"UNKNOWN"
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