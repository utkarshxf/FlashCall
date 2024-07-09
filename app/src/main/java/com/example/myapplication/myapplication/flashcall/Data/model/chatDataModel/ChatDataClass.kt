package com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import com.google.gson.annotations.SerializedName

data class ChatDataClass(
    @SerializedName("clientId")
    val clientId : String?=null,
    @SerializedName("createdAt")
    val createdAt : Long?=null,
    @SerializedName("messages")
    val messages : List<MessageDataClass>?=null,
    @SerializedName("status")
    val status : String?=null,
)