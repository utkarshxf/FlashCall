package com.example.myapplication.myapplication.flashcall

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import com.example.myapplication.myapplication.flashcall.utils.CustomNotificationHandler
import com.example.myapplication.myapplication.flashcall.utils.PreferencesKey
import com.example.myapplication.myapplication.flashcall.utils.TimestampConverter
import com.google.firebase.FirebaseApp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestoreSettings
import com.google.gson.GsonBuilder
import dagger.hilt.android.HiltAndroidApp
import io.getstream.android.push.firebase.FirebasePushDeviceGenerator
import io.getstream.video.android.core.StreamVideo
import io.getstream.video.android.core.StreamVideoBuilder
import io.getstream.video.android.core.logging.HttpLoggingLevel
import io.getstream.video.android.core.logging.LoggingLevel
import io.getstream.video.android.model.User
import io.getstream.log.Priority
import io.getstream.video.android.core.notifications.NotificationConfig

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

    }

    fun streamBuilder(context: Context) {
        val sharedPreferences = context.getSharedPreferences("user_prefs1", Context.MODE_PRIVATE)
        var userId = "user_Id"
        var userName = "Unknown User"
        var profileImage = ""
        var phoneNumber = ""

        // Attempt to retrieve values from SharedPreferences with proper error handling
        try {
            userId = sharedPreferences.getString(PreferencesKey.UserId.key, "user_Id") ?: "user_Id"
            userName = sharedPreferences.getString(PreferencesKey.FirstName.key, "Unknown User") ?: "Unknown User"
            profileImage = sharedPreferences.getString(PreferencesKey.Photo.key, "") ?: ""
            phoneNumber = sharedPreferences.getString(PreferencesKey.Phone.key, "") ?: ""
        } catch (e: Exception) {
            Log.e("BaseClass", "Error reading SharedPreferences: ${e.message}")
        }
        // Log retrieved values for debugging
        Log.d("BaseClass", "userId: $userId, userName: $userName, profileImage: $profileImage")
        try {
            StreamVideoBuilder(
                context = context,
                apiKey = "9jpqevnvhfzv",
                token = StreamVideo.devToken(userId),  // Token requires valid userId
                user = User(
                    id = userId,
                    name = userName,
                    image = profileImage ?: "null",
                    role = "admin"
                ),
                loggingLevel = LoggingLevel(Priority.VERBOSE, HttpLoggingLevel.BODY),
                notificationConfig = NotificationConfig(
                    hideRingingNotificationInForeground = true,
                    // Make sure that the provider name is equal to the "Name" of the configuration in Stream Dashboard.
                    pushDeviceGenerators = listOf(
                        FirebasePushDeviceGenerator(providerName = "Test"),
                        FirebasePushDeviceGenerator(providerName = "FlashCall")
                    ),
                    notificationHandler = CustomNotificationHandler(
                        context.applicationContext as Application,
                    ),
                )
            ).build()
        } catch (e: Exception) {
            Log.e("BaseClass", "Error initializing StreamVideo: ${e.message}")
        }
        updateUserStatus(phoneNumber, true)
    }
    fun streamRemoveClient() {
        if (StreamVideo.isInstalled) {
            StreamVideo.instance().logOut()
            StreamVideo.removeClient()
        }
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
        const val CHANNEL_ID = "default_channel"
    }
    fun updateUserStatus(phoneNumber: String, isOnline: Boolean) {
        val db = FirebaseFirestore.getInstance()

        // Reference to the user document in the "userStatus" collection
        val userRef = db.collection("userStatus").document(phoneNumber)

        // Set the status to either "Online" or "Offline"
        val status = if (isOnline) "Online" else "Offline"

        // Check if the document exists
        userRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Document exists, update the status field
                    updateStatus(userRef, status, phoneNumber)
                } else {
                    // Document doesn't exist, create it with the status field
                    val userData = hashMapOf("status" to status)
                    userRef.set(userData)
                        .addOnSuccessListener {
                            println("New userStatus document created with status $status for user: $phoneNumber")
                        }
                        .addOnFailureListener { e ->
                            println("Error creating userStatus document: $e")
                        }
                }
            }
            .addOnFailureListener { e ->
                println("Error checking document existence: $e")
            }
    }

    private fun updateStatus(userRef: DocumentReference, status: String, phoneNumber: String) {
        userRef.update("status", status)
            .addOnSuccessListener {
                println("Status updated to $status for user: $phoneNumber")
            }
            .addOnFailureListener { e ->
                println("Error updating status: $e")
            }
    }
}
