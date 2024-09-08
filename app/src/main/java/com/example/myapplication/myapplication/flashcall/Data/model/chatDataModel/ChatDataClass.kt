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
    val clientBalance: Double? = null,
    @SerializedName("creatorId")
    val creatorId: String?=null,
    @SerializedName("endedAt")
    val endedAt: Long?=null,
    @SerializedName("maxChatDuration")
    val maxChatDuration: Int?=null,
    @SerializedName("startedAt")
    val startedAt: Long?=null,
    @SerializedName("messages")
    val messages : List<MessageDataClass>?=null,
    @SerializedName("status")
    val status : String?=null,
    @SerializedName("timeLeft")
    val timeLeft : Double?=null,
    @SerializedName("timeUtilized")
    val timeUtilized: Double?=null,
    @SerializedName("clientName")
    val clientName : String?=null
)