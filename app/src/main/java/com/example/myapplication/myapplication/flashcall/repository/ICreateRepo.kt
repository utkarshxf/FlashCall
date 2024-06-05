package com.example.myapplication.myapplication.flashcall.repository

import com.example.myapplication.myapplication.flashcall.Data.model.CreateUserResponse
import kotlinx.coroutines.flow.Flow

interface ICreateRepo {

    suspend fun createUser(
        url: String,
        username: String,
        phone: String,
        fullName: String,
        firstName: String,
        lastName: String,
        photo: String,
        profession: String,
        themeSelected: String,
        videoRate: String,
        audioRate: String,
        chatRate: String,
        gender: String,
        dob: String,
        bio: String,
        kyc_status: String,
    ) : Flow<CreateUserResponse>

}