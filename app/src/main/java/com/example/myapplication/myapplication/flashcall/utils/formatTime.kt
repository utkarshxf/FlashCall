package com.example.myapplication.myapplication.flashcall.utils

import android.content.Context
import android.content.pm.PackageManager
import java.time.LocalDate


fun formatTime(timeInSeconds: Double): String {
    val totalSeconds = timeInSeconds.toInt()
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}

fun capitalizeAfterSpace(input: String): String {
    return input.split(" ").joinToString(" ") { word ->
        word.replaceFirstChar { it.uppercase() }
    }
}

fun getAppVersionName(context: Context): String {
    return try {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        packageInfo.versionName ?: "Unknown Version"
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        "Unknown Version"
    }
}

fun getVerificationId(length: Int): String {
    val allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    var var_id: String = (1..length)
        .map { allowedChars.random() }
        .joinToString("")

    return ""+LocalDate.now() + var_id
}