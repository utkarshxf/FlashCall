package com.example.myapplication.myapplication.flashcall.Data

import androidx.media3.exoplayer.ExoPlayer

data class AudioPlayerState(
    val player: ExoPlayer,
    var isPlaying: Boolean = false,
    var currentPosition: Long = 0L,
    var totalDuration: Long = 0L,
)