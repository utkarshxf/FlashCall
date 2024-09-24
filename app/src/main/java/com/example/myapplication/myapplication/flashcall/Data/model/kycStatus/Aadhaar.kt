package com.example.myapplication.myapplication.flashcall.Data.model.kycStatus


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//@Serializable
//data class Aadhaar(
//    @SerialName("_id")
//    var id: String?,
//    @SerialName("img_link")
//    var imgLink: String?,
//    @SerialName("name")
//    var name: String?,
//    @SerialName("ref_id")
//    var refId: Int?,
//    @SerialName("status")
//    var status: String?
//)
data class Aadhaar(
    val ref_id: Long,
    val name: String,
    val img_link: String,
    val id: String,
    val status: String
)