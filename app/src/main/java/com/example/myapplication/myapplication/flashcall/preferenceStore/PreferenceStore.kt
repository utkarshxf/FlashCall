package com.example.myapplication.myapplication.flashcall.preferenceStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class PreferenceStore(
    private val dataStore: DataStore<Preferences>,
) : userPref {
    override fun getToken(): Flow<String> {
        return dataStore.data.catch {
            emit(emptyPreferences())
        }.map {
            it[USER_KEY]!!.toString()
        }
    }

    override suspend fun saveToken(token: String) {
        dataStore.edit {
            it[USER_KEY] = token
        }
    }
}