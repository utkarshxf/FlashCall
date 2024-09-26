package com.example.myapplication.myapplication.flashcall.Data.model.firestore

data class UserServicesResponse(
    val prices: Prices = Prices(),
    val services: Services = Services()
)

data class Prices(
    val audioCall: String? = null,
    val chat: String? = null,
    val videoCall: String? = null
)

data class Services(
    val audioCall: Boolean = false,
    val chat: Boolean = false,
    val myServices: Boolean = false,
    val videoCall: Boolean = false
)