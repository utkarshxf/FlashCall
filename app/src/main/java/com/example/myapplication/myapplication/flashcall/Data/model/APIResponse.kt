package com.example.myapplication.myapplication.flashcall.Data.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

sealed class APIResponse<out T>() {

    object Empty : APIResponse<Nothing>()
    object Loading : APIResponse<Nothing>()

    class Success<out T>(val data: T) : APIResponse<T>()
    class Error<T>(val message: String?, data: T? = null) : APIResponse<T>()

}


