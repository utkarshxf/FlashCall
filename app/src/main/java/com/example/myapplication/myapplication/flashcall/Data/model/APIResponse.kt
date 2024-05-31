package com.example.myapplication.myapplication.flashcall.Data.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

sealed class APIResponse<T>(
    val data: T? = null,
    val message: String? = null
) {

    class Success<T>(data: T) : APIResponse<T>(data)

    class Error<T>(message: String?, data: T? = null) : APIResponse<T>(data, message)

    class Loading<T> : APIResponse<T>()

}


