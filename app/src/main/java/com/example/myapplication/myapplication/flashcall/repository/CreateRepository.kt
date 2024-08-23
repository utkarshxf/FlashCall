package com.example.myapplication.myapplication.flashcall.repository

import android.util.Log
import com.example.myapplication.myapplication.flashcall.Data.model.CreateUser
import com.example.myapplication.myapplication.flashcall.Data.model.CreateUserResponse
import com.example.myapplication.myapplication.flashcall.Data.model.UpdateUserRequest
import com.example.myapplication.myapplication.flashcall.Data.model.UpdateUserResponse
import com.example.myapplication.myapplication.flashcall.Data.model.UserUpdateData
import com.example.myapplication.myapplication.flashcall.Data.network.APIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

//class CreateRepository @Inject constructor(private val apiService: APIService) : ICreateRepo {
//
//
//    override suspend fun createUser(
//        url: String,
//        username: String,
//        phone: String,
//        fullName: String,
//        firstName: String,
//        lastName: String,
//        photo: String,
//        profession: String,
//        themeSelected: String,
//        videoRate: String,
//        audioRate: String,
//        chatRate: String,
//        gender: String,
//        dob: String,
//        bio: String,
//        kyc_status: String
//    ): Flow<CreateUserResponse> {
//        return flow {
//            val response = apiService.createUSER(
//                url,
//                CreateUser(
//                    username,
//                    phone,
//                    fullName,
//                    firstName,
//                    lastName,
//                    photo,
//                    profession,
//                    themeSelected,
//                    videoRate,
//                    audioRate,
//                    chatRate,
//                    gender,
//                    dob,
//                    bio,
//                    kyc_status
//                )
//            )
//            emit(response)
//        }.flowOn(Dispatchers.IO)
//    }
//}

class CreateRepository @Inject constructor(private val apiService: APIService) {

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
        kyc_status: String
    ): Flow<CreateUserResponse> {
        return flow {
            val response = apiService.createUSER(
                url,
                CreateUser(
                    username,
                    phone,
                    fullName,
                    firstName,
                    lastName,
                    photo,
                    profession,
                    themeSelected,
                    videoRate,
                    audioRate,
                    chatRate,
                    gender,
                    dob,
                    bio,
                    kyc_status
                )
            )
            emit(response)
        }
    }

    suspend fun updateUser(
        url: String,
        userId: String,
        fullName: String,
        firstName: String,
        lastName: String,
        username: String,
        phone: String,
        photo: String,
        profession: String,
        themeSelected: String,
        videoRate: String,
        audioRate: String,
        chatRate: String,
        gender: String,
        dob: String,
        bio: String
    ): Flow<UpdateUserResponse> {
//        Log.e("resonseUpdateUser", "$response")
        return flow {
            val response = apiService.updateUser(
                url,
                UpdateUserRequest(
                    userId = userId,
                    user = UserUpdateData(
                        fullName = fullName,
                        username = username,
                        phone = phone,
                        firstName = firstName,
                        lastName = lastName,
                        photo = photo,
                        profession = profession,
                        themeSelected = themeSelected,
                        videoRate = videoRate,
                        audioRate = audioRate,
                        chatRate = chatRate,
                        gender = gender,
                        dob = dob,
                        bio = bio
                    )
                )
            )
            emit(response)
            Log.e("resonseUpdateUser", "$response")
        }
    }
}
