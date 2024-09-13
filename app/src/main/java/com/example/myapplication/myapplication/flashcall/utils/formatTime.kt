package com.example.myapplication.myapplication.flashcall.utils


fun formatTime(timeInSeconds: Double): String {
    val totalSeconds = timeInSeconds.toInt()
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}