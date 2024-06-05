package com.example.myapplication.myapplication.flashcall.Data.model

import android.provider.ContactsContract.CommonDataKinds.Phone
import android.provider.ContactsContract.CommonDataKinds.Photo
import com.google.gson.annotations.SerializedName

class CreateUserResponse(

    @SerializedName("username")
    val username: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("photo")
    val photo: String,
    @SerializedName("profession")
    val profession: String,
    @SerializedName("themeSelected")
    val themeSelected: String,
    @SerializedName("videoRate")
    val videoRate: String,
    @SerializedName("audioRate")
    val audioRate: String,
    @SerializedName("chatRate")
    val chatRate: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("dob")
    val dob: String,
    @SerializedName("bio")
    val bio: String,
    @SerializedName("_id")
    val _id: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("__v")
    val __v: String,
)

