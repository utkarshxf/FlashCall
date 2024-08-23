package com.example.myapplication.myapplication.flashcall.Data.network

import com.example.myapplication.myapplication.flashcall.Data.ResendOTP
import com.example.myapplication.myapplication.flashcall.Data.SendOTP
import com.example.myapplication.myapplication.flashcall.Data.VerifyOTP
import com.example.myapplication.myapplication.flashcall.Data.model.APIResponse
import com.example.myapplication.myapplication.flashcall.Data.model.CreateUser
import com.example.myapplication.myapplication.flashcall.Data.model.CreateUserResponse
import com.example.myapplication.myapplication.flashcall.Data.model.IsUserCreatedResponse
import com.example.myapplication.myapplication.flashcall.Data.model.Request
import com.example.myapplication.myapplication.flashcall.Data.model.ResendOTPResponse
import com.example.myapplication.myapplication.flashcall.Data.model.ResendRequest
import com.example.myapplication.myapplication.flashcall.Data.model.SendOTPResponseX
import com.example.myapplication.myapplication.flashcall.Data.model.UpdateUserRequest
import com.example.myapplication.myapplication.flashcall.Data.model.UpdateUserResponse
import com.example.myapplication.myapplication.flashcall.Data.model.ValidateRequest
import com.example.myapplication.myapplication.flashcall.Data.model.VerifyOTPResponse
import com.example.myapplication.myapplication.flashcall.Data.model.VerifyRequest
import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.ValidateResponse
import com.example.myapplication.myapplication.flashcall.Data.model.feedback.FeedBackResponse
import com.example.myapplication.myapplication.flashcall.Data.model.wallet.Transaction
import com.example.myapplication.myapplication.flashcall.Data.model.wallet.TransactionResponse
import org.checkerframework.checker.guieffect.qual.PolyUIType
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
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
    ): Response<SendOTPResponseX>


    @POST
    suspend fun resendOTP(
        @Url url:String,
        @Body resendOTP: ResendRequest
    ):ResendOTPResponse

    @POST
    suspend fun verifyOTP(
        @Url url:String,
        @Body resendOTP: VerifyRequest
    ): VerifyOTPResponse

    @POST
    suspend fun createUSER(
        @Url url:String,
        @Body createUser: CreateUser
    ): CreateUserResponse

    @POST
    suspend fun validateUser(
        @Url url:String,
        @Body validateUser: ValidateRequest
    ) : ValidateResponse

    @GET
    suspend fun getFeedbacks(
        @Url url:String
    ) : List<FeedBackResponse>

    @GET
    suspend fun getTransactions(
        @Url url:String
    ) : TransactionResponse

    @POST
    suspend fun isCreatedUser(
        @Url url: String,
        @Body request: Request
    ) : Response<IsUserCreatedResponse>

    @PUT
    suspend fun updateUser(
        @Url url: String,
        @Body updateUser: UpdateUserRequest
    ) : UpdateUserResponse


}