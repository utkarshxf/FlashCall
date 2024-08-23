package com.example.myapplication.myapplication.flashcall

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreferenceStore @Inject constructor(
    @ApplicationContext
    private val context: Context
){

//    companion object {
//        private val Context.datastore by preferencesDataStore("settings")
//        val token = stringPreferencesKey("token")
//    }


}
