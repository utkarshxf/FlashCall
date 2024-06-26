package com.example.myapplication.myapplication.flashcall.preferenceStore

import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow

val USER_KEY = stringPreferencesKey("user_Token")
interface userPref {

    fun getToken() : Flow<String>

    suspend fun saveToken(uid: String)
}