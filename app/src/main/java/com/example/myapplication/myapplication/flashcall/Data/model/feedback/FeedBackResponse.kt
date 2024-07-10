package com.example.myapplication.myapplication.flashcall.Data.model.feedback

data class FeedBackResponse(
    val _id : String? = null,
    val creatorId : String? = null,
    val feedbacks : List<Feedback>? = null
)


data class Feedback(
    val clientId : Client? = null,
    val rating : Int? = null,
    val feedback : String? = null,
    val createdAt : String? = null
)

data class Client(
    val _id : String? = null,
    val clerkId : String? = null,
    val username : String? = null,
    val onlineStatus : Boolean? = null,
    val phone : String? = null,
    val firstName : String? = null,
    val lastName : String? = null,
    val photo : String? = null,
    val role : String? = null,
    val bio : String? = null,
    val walletBalance : Double? = null,
    val createdAt : String? = null,
    val updatedAt : String? = null,
    val __v : Int? = null
)