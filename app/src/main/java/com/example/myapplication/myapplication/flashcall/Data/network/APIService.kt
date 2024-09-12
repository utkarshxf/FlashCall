package com.example.myapplication.myapplication.flashcall.Data.network

import com.example.myapplication.myapplication.flashcall.Data.model.CreateUser
import com.example.myapplication.myapplication.flashcall.Data.model.CreateUserResponse
import com.example.myapplication.myapplication.flashcall.Data.model.IsUserCreatedResponse
import com.example.myapplication.myapplication.flashcall.Data.model.Request
import com.example.myapplication.myapplication.flashcall.Data.model.ResendOTPResponse
import com.example.myapplication.myapplication.flashcall.Data.model.ResendRequest
import com.example.myapplication.myapplication.flashcall.Data.model.SendOTPResponseX
import com.example.myapplication.myapplication.flashcall.Data.model.UpdateUserRequest
import com.example.myapplication.myapplication.flashcall.Data.model.UpdateUserResponse
import com.example.myapplication.myapplication.flashcall.Data.model.UserDetailsResponse
import com.example.myapplication.myapplication.flashcall.Data.model.UsernameAvailabilityResponse
import com.example.myapplication.myapplication.flashcall.Data.model.ValidateRequest
import com.example.myapplication.myapplication.flashcall.Data.model.VerifyOTPResponse
import com.example.myapplication.myapplication.flashcall.Data.model.VerifyRequest
import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.ValidateResponse
import com.example.myapplication.myapplication.flashcall.Data.model.feedback.FeedbackResponse
import com.example.myapplication.myapplication.flashcall.Data.model.feedback.UpdateFeedback
import com.example.myapplication.myapplication.flashcall.Data.model.feedback.UpdateFeedbackResponse
import com.example.myapplication.myapplication.flashcall.Data.model.wallet.TransactionsResponse
import com.example.myapplication.myapplication.flashcall.Data.model.wallet.UserId
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Url

interface APIService {

    companion object{
        const val BASE_URL = "https://flashcall.vercel.app/"
    }

    @POST
    suspend fun sendOTP(
        @Url url:String,
        @Body request: Request
    ): retrofit2.Response<SendOTPResponseX>


    @POST
    suspend fun resendOTP(
        @Url url:String,
        @Body resendOTP: ResendRequest
    ):Response <ResendOTPResponse>

    @POST
    suspend fun verifyOTP(
        @Url url:String,
        @Body resendOTP: VerifyRequest
    ) :Response <VerifyOTPResponse>

    @POST
    suspend fun createUSER(
        @Url url:String,
        @Body createUser: CreateUser
    ):Response < CreateUserResponse>

    @POST
    suspend fun validateUser(
        @Url url:String,
        @Body validateUser: ValidateRequest
    ) :Response < ValidateResponse>

    @GET
    suspend fun getFeedbacks(
        @Url url:String
    ) :Response < FeedbackResponse>

    @POST
    suspend fun updateFeedback(
        @Url url:String,
        @Body updateFeedback: UpdateFeedback
    ) :Response <UpdateFeedbackResponse>

    @GET
    suspend fun getTransactions(
        @Url url:String
    ) :Response < TransactionsResponse>

    @POST
    suspend fun isCreatedUser(
        @Url url: String,
        @Body request: Request
    ) : Response<IsUserCreatedResponse>

    @PUT
    suspend fun updateUser(
        @Url url: String,
        @Body updateUser: UpdateUserRequest
    ) :Response < UpdateUserResponse>

    @GET
    suspend fun checkUsernameAvailability(
        @Url url: String
    ):Response < UsernameAvailabilityResponse>

    @POST
    suspend fun getUserById(
        @Url url: String,
        @Body userId:UserId
    ):Response<UserDetailsResponse>
}