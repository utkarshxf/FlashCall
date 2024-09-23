package com.example.myapplication.myapplication.flashcall.Data.model.aadharOtpResponse


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Data(
    @SerialName("address")
    var address: String?,
    @SerialName("care_of")
    var careOf: String?,
    @SerialName("dob")
    var dob: String?,
    @SerialName("email")
    var email: String?,
    @SerialName("gender")
    var gender: String?,
    @SerialName("message")
    var message: String?,
    @SerialName("mobile_hash")
    var mobileHash: String?,
    @SerialName("name")
    var name: String?,
    val photo_link: String,
    @SerialName("ref_id")
    var refId: String?,
    @SerialName("split_address")
    var splitAddress: SplitAddress?,
    @SerialName("status")
    var status: String?,
    @SerialName("year_of_birth")
    var yearOfBirth: String?
)