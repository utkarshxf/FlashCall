package com.example.myapplication.myapplication.flashcall

import android.content.Context
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