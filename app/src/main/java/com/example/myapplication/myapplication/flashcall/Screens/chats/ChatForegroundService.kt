package com.example.myapplication.myapplication.flashcall.Screens.chats

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.myapplication.myapplication.flashcall.R

private const val CHANNEL_ID = "my_channel_id"

class ChatForegroundService: Service() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel(this)  // Ensure you call this before showing the notification
    }

    @SuppressLint("ForegroundServiceType")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.d("IsForeground","Running")

        // Create and show the foreground notification
        val notification: Notification = buildForegroundNotification()
        startForeground(111, notification)

        // Starting the activity even if the app is in the background
        val activityIntent = Intent(this, IncomingChatActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        startActivity(activityIntent)

        return START_NOT_STICKY
    }

    private fun buildForegroundNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Incoming Call")
            .setContentText("You have an incoming call.")
            .setSmallIcon(R.drawable.logo) // Replace with your own icon
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC) // Set visibility
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "My Channel"
            val descriptionText = "Channel description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}