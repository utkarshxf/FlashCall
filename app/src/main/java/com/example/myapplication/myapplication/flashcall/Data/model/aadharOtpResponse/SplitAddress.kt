package com.example.myapplication.myapplication.flashcall.Data.model.aadharOtpResponse


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SplitAddress(
    @SerialName("country")
    var country: String?,
    @SerialName("dist")
    var dist: String?,
    @SerialName("house")
    var house: String?,
    @SerialName("landmark")
    var landmark: String?,
    @SerialName("pincode")
    var pincode: String?,
    @SerialName("po")
    var po: String?,
    @SerialName("state")
    var state: String?,
    @SerialName("street")
    var street: String?,
    @SerialName("subdist")
    var subdist: String?,
    @SerialName("vtc")
    var vtc: String?
)