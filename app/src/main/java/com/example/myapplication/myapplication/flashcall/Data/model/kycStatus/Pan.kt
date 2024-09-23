package com.example.myapplication.myapplication.flashcall.Data.model.kycStatus


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Pan(
    @SerialName("_id")
    var id: String?,
    @SerialName("pan_number")
    var panNumber: String?,
    @SerialName("reference_id")
    var referenceId: Int?,
    @SerialName("registered_name")
    var registeredName: String?,
    @SerialName("valid")
    var valid: Boolean?
)