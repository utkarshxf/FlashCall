package com.example.myapplication.myapplication.flashcall.Data.model

import io.getstream.video.android.core.Call

sealed interface SDKResponseState
{

    data class Success(val data: Call) : SDKResponseState
    data object Loading : SDKResponseState
    data object Error : SDKResponseState

}