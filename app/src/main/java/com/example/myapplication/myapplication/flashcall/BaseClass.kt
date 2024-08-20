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
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.LocalCacheSettings
import com.google.firebase.firestore.firestoreSettings
import com.google.gson.GsonBuilder
import dagger.hilt.android.HiltAndroidApp
import io.getstream.video.android.core.Call
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
        create(this)


// Optionally modify the settings (e.g., enable persistence)

        val firestoreSettings = firestoreSettings {
            isPersistenceEnabled = true
        }

        FirebaseFirestore.getInstance().firestoreSettings = firestoreSettings
    }

    fun create(context: Context) {

        //Constant Data Object
        val userId = "664c90ae43f0af8f1b3d5803"
        val notify: ((Call) -> Notification) = {
            Log.d("Call", "Call$it")
            Notification.Builder(context).build()
        }

        StreamVideoBuilder(
            context = context,
            apiKey = "9jpqevnvhfzv",
            token = StreamVideo.devToken(userId),
            user = User(
                id = userId,
                name = "Sujit",
                //Image Link by getUser API
                image = "https://drive.google.com/file/d/1fosIGjYB_Wgw2DBHVrWXGBoHlbT1dK7s/view?usp=drivesdk",
                role = "admin",
            ),
            ringNotification = notify,
        ).build()
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