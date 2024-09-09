package com.example.myapplication.myapplication.flashcall

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import com.example.myapplication.myapplication.flashcall.utils.TimestampConverter
import com.google.firebase.FirebaseApp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestoreSettings
import com.google.gson.GsonBuilder
import dagger.hilt.android.HiltAndroidApp
import io.getstream.video.android.core.StreamVideo
import io.getstream.video.android.core.StreamVideoBuilder
import io.getstream.video.android.model.User

@HiltAndroidApp
class BaseClass : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        FirebaseApp.initializeApp(this)

        val gson = GsonBuilder()
            .registerTypeAdapter(Timestamp::class.java, TimestampConverter())
            .create()

        // Initialize Firestore settings
        val firestoreSettings = firestoreSettings {
            isPersistenceEnabled = true
        }
        FirebaseFirestore.getInstance().firestoreSettings = firestoreSettings

        streamBuilder(this)
    }

    fun streamBuilder(context: Context) {
        val sharedPreferences = context.getSharedPreferences("user_prefs1", Context.MODE_PRIVATE)
        var userId = "user_Id"
        var userName = "Unknown User"
        var profileImage = ""

        // Attempt to retrieve values from SharedPreferences with proper error handling
        try {
            userId = sharedPreferences.getString("user_id", "user_Id") ?: "user_Id"
            userName = sharedPreferences.getString("full_name", "Unknown User") ?: "Unknown User"
            profileImage = sharedPreferences.getString("photo", "") ?: ""
        } catch (e: Exception) {
            Log.e("BaseClass", "Error reading SharedPreferences: ${e.message}")
        }


        // Log retrieved values for debugging
        Log.d("BaseClass", "userId: $userId, userName: $userName, profileImage: $profileImage")

        // Build the StreamVideo session with safe nullable checks
        try {
            StreamVideoBuilder(
                context = context,
                apiKey = "9jpqevnvhfzv",
                token = StreamVideo.devToken(userId),  // Token requires valid userId
                user = User(
                    id = userId,
                    name = userName,
                    image = profileImage?:"null",
                    role = "admin",
                ),
                ringNotification = { call -> Notification.Builder(context).build() }
            ).build()
        } catch (e: Exception) {
            Log.e("BaseClass", "Error initializing StreamVideo: ${e.message}")
        }
    }
    fun streamRemoveClient()
    {
        StreamVideo.removeClient()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Chat Notifications"
            val descriptionText = "Notifications for chat requests"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ID = "CHAT_REQUEST_CHANNEL"
    }
}
