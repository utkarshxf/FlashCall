package com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import com.google.gson.annotations.SerializedName

data class ChatDataClass(
    @SerializedName("clientId")
    val clientId : String?=null,
    @SerializedName("createdAt")
    val createdAt : Timestamp?=null,
    @SerializedName("clientBalance")
    val clientBalance: Double?,
    @SerializedName("creatorId")
    val creatorId: String,
    @SerializedName("endedAt")
    val endedAt: Long?,
    @SerializedName("maxChatDuration")
    val maxChatDuration: Int?,
    @SerializedName("startedAt")
    val startedAt: Long?,
    @SerializedName("messages")
    val messages : List<MessageDataClass>?=null,
    @SerializedName("status")
    val status : String?=null,
    @SerializedName("timeLeft")
    val timeLeft : Double?=null,
    @SerializedName("timeUtilized")
    val timeUtilized: Double?,
    @SerializedName("clientName")
    val clientName : String?=null
)