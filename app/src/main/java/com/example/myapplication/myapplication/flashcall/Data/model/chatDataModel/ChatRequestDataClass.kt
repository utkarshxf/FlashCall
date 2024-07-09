package com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel

import android.os.Parcelable
import androidx.versionedparcelable.VersionedParcelize
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import com.google.gson.annotations.SerializedName


data class ChatRequestDataClass(
    @SerializedName("chatId")
    val chatId : String?=null,
    @SerializedName("clientId")
    val clientId : String?=null,
    @SerializedName("createdAt")
    val createdAt : Long?=null,
    @SerializedName("creatorId")
    val creatorId : String?=null,
    @SerializedName("status")
    val status : String?=null,
    //    val clientName : String
)