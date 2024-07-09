package com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ServerTimestamp
import com.google.firebase.firestore.model.ServerTimestamps
import com.google.gson.annotations.SerializedName

data class MessageDataClass(
    @SerializedName("createdAt")
    val createdAt: Long?=null,
    @SerializedName("img")
    val img: String? = null,
    @SerializedName("audio")
    val audio: String? = null,
    @SerializedName("text")
    val text: String? = null,
    @SerializedName("senderId")
    val senderId: String?=null,
    @SerializedName("seen")
    val seen: Boolean?=null
)

//fun MessageDataClass.toMap(): Map<String, Any?> {
//    return mapOf(
//        // Exclude createdAt as it will be set by Firestore server-side
//        "createdAt" to FieldValue.serverTimestamp(),
//        "img" to img,
//        "audio" to audio,
//        "text" to text,
//        "senderId" to senderId,
//        "seen" to seen
//    )
//}