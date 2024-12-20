package com.example.myapplication.myapplication.flashcall.repository

import android.util.Log
import com.example.myapplication.myapplication.flashcall.Data.model.CreateUser
import com.example.myapplication.myapplication.flashcall.Data.model.CreateUserResponse
import com.example.myapplication.myapplication.flashcall.Data.model.LinkData
import com.example.myapplication.myapplication.flashcall.Data.model.ShareLinkResponse
import com.example.myapplication.myapplication.flashcall.Data.model.UpdateUserRequest
import com.example.myapplication.myapplication.flashcall.Data.model.UpdateUserResponse
import com.example.myapplication.myapplication.flashcall.Data.model.UserAssistanceLink
import com.example.myapplication.myapplication.flashcall.Data.model.UserDetailsResponse
import com.example.myapplication.myapplication.flashcall.Data.model.UserUpdateData
import com.example.myapplication.myapplication.flashcall.Data.model.UsernameAvailabilityResponse
import com.example.myapplication.myapplication.flashcall.Data.model.deleteAdditionalLink.DeleteAdditionalLinks
import com.example.myapplication.myapplication.flashcall.Data.model.deleteAdditionalLink.DeletedAdditionalLinksResponse
import com.example.myapplication.myapplication.flashcall.Data.model.editAdditionalLink.EditAdditionalLinkRequest
import com.example.myapplication.myapplication.flashcall.Data.model.spacialization.SpacializationResponse
import com.example.myapplication.myapplication.flashcall.Data.model.todaysWallet.TodaysWalletBalanceResponse
import com.example.myapplication.myapplication.flashcall.Data.model.wallet.UserId
import com.example.myapplication.myapplication.flashcall.Data.network.APIService
import com.example.myapplication.myapplication.flashcall.utils.SafeApiRequest
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

class CreateRepository @Inject constructor(private val apiService: APIService):SafeApiRequest() {

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
            val response =  apiService.createUSER(
                url,
                CreateUser(
                    creatorId = phone,
                    username = username,
                    phone = phone,
                    fullName = fullName,
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
                    bio = bio,
                    kyc_status = kyc_status
                )
            )
            emit(safeApiRequest { response})
        }
    }

    suspend fun updateUser(
        url: String,
        userId: String,
        fullName: String?=null,
        firstName: String?=null,
        lastName: String?=null,
        username: String?=null,
        phone: String?=null,
        photo: String?=null,
        profession: String?=null,
        themeSelected: String?=null,
        videoRate: String?=null,
        audioRate: String?=null,
        chatRate: String?=null,
        videoService: Boolean?=null,
        audioService: Boolean?=null,
        chatService: Boolean?=null,
        gender: String?=null,
        dob: String?=null,
        bio: String?=null,
        link: LinkData?=null
    ): Flow<UpdateUserResponse> {
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
                        videoAllowed = videoService,
                        audioAllowed = audioService,
                        chatAllowed = chatService,
                        gender = gender,
                        dob = dob,
                        bio = bio,
                        link = link
                    )
                )
            )
            Log.e("resonseUpdateUser", "$response")
            emit(safeApiRequest {  response})
        }
    }
    suspend fun checkUsernameAvailability(username: String): UsernameAvailabilityResponse {
        return safeApiRequest {  apiService.checkUsernameAvailability("https://flashcall.me/api/v1/user/getAllUsernames?username=$username")}
    }

    suspend fun getUserAdditionalLinks(url: String , userId: UserId): Flow<UserDetailsResponse> {
        return flow {
            val response = apiService.getUserById(url ,userId )
            emit(safeApiRequest { response } )
        }
    }

    suspend fun getTodaysWalletBalance(url: String): Flow<TodaysWalletBalanceResponse> {
        return flow {
            val response = apiService.todaysWalletBalance(url)
            emit(safeApiRequest { response } )
        }
    }
    suspend fun daleteAdditionalLink(body: DeleteAdditionalLinks): Flow<DeletedAdditionalLinksResponse> {
        return flow {
            val response = apiService.deleteAdditionalLink(body )
            emit(safeApiRequest { response } )
        }
    }

    suspend fun editAdditionalLink(
        url: String,
        body: EditAdditionalLinkRequest
    ): Flow<UpdateUserResponse> {
        return flow {
            val response = apiService.editAdditionalLink(
                url,
                body
            )
            emit(safeApiRequest {  response})
        }
    }

    suspend fun getShareLink(url: String): Flow<ShareLinkResponse>{
        return flow {
            val response = apiService.getShareLink(url)
            emit(safeApiRequest { response })
        }
    }

    suspend fun getUserAssistanceLink(url: String): Flow<UserAssistanceLink>{
        return flow {
            val response = apiService.getUserAssistanceLink(url)
            emit(safeApiRequest { response })
        }
    }

    suspend fun getSpacializations(url: String): Flow<SpacializationResponse>{
        return flow {
            val response = apiService.getSpacializations(url)
            emit(safeApiRequest { response })
        }
    }

}
