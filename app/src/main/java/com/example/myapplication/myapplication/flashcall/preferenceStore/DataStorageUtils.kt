package com.example.myapplication.myapplication.flashcall.preferenceStore

import android.content.Context
import android.content.SharedPreferences
import com.example.myapplication.myapplication.flashcall.Data.model.IsUserCreatedResponse

object DataStorageUtils {

    private const val PREFS_NAME = "user_prefs1"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveToken(context: Context, token: String) {
        getSharedPreferences(context).edit().apply {
            putString("user_token", token)
            apply()
        }
    }

    fun deleteToken(context: Context) {
        getSharedPreferences(context).edit().apply {
            remove("user_token")
            apply()
        }
    }

    fun getToken(context: Context): String? {
        return getSharedPreferences(context).getString("user_token", null)
    }

    fun saveUser(context: Context, userData: IsUserCreatedResponse) {
        getSharedPreferences(context).edit().apply {
            putString("user_id", userData._id)
            putString("username", userData.username)
            putString("phone", userData.phone)
            putString("full_name", userData.fullName)
            putString("first_name", userData.firstName)
            putString("last_name", userData.lastName)
            putString("photo", userData.photo)
            putString("theme_selected", userData.themeSelected)
            putString("bio", userData.bio)
            putString("profession", userData.profession)
            putString("dob", userData.dob)
            putString("gender", userData.gender)
            putFloat("wallet_balance", userData.walletBalance?.toFloat() ?: 0f)
            putBoolean("audio_allowed", userData.audioAllowed ?: false)
            putBoolean("chat_allowed", userData.chatAllowed ?: false)
            putBoolean("video_allowed", userData.videoAllowed ?: false)
            putString("user_type", userData.userType)
            putString("message", userData.message)
            apply()
        }
    }

    fun getUser(context: Context): IsUserCreatedResponse? {
        val prefs = getSharedPreferences(context)
        return IsUserCreatedResponse(
            _id = prefs.getString("user_id", null) ?: return null,
            username = prefs.getString("username", null),
            phone = prefs.getString("phone", null),
            fullName = prefs.getString("full_name", null),
            firstName = prefs.getString("first_name", null),
            lastName = prefs.getString("last_name", null),
            photo = prefs.getString("photo", null),
            themeSelected = prefs.getString("theme_selected", null),
            bio = prefs.getString("bio", null),
            profession = prefs.getString("profession", null),
            dob = prefs.getString("dob", null),
            gender = prefs.getString("gender", null),
            walletBalance = prefs.getFloat("wallet_balance", 0f).toDouble(),
            audioAllowed = prefs.getBoolean("audio_allowed", false),
            chatAllowed = prefs.getBoolean("chat_allowed", false),
            videoAllowed = prefs.getBoolean("video_allowed", false),
            userType = prefs.getString("user_type", null),
            message = prefs.getString("message", null)
        )
    }
}
