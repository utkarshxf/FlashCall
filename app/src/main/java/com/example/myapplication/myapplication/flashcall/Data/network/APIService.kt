package com.example.myapplication.myapplication.flashcall.Data.network

import com.example.myapplication.myapplication.flashcall.Data.model.PanResponse
import com.example.myapplication.myapplication.flashcall.Data.model.AadhaarRequest
import com.example.myapplication.myapplication.flashcall.Data.model.AadhaarResponse
import com.example.myapplication.myapplication.flashcall.Data.model.CreateUser
import com.example.myapplication.myapplication.flashcall.Data.model.CreateUserResponse
import com.example.myapplication.myapplication.flashcall.Data.model.IsUserCreatedResponse
import com.example.myapplication.myapplication.flashcall.Data.model.LinkData
import com.example.myapplication.myapplication.flashcall.Data.model.nameMatch.NameMatchRequest
import com.example.myapplication.myapplication.flashcall.Data.model.Request
import com.example.myapplication.myapplication.flashcall.Data.model.ResendOTPResponse
import com.example.myapplication.myapplication.flashcall.Data.model.ResendRequest
import com.example.myapplication.myapplication.flashcall.Data.model.SendOTPResponseX
import com.example.myapplication.myapplication.flashcall.Data.model.UpdateUserRequest
import com.example.myapplication.myapplication.flashcall.Data.model.UpdateUserResponse
import com.example.myapplication.myapplication.flashcall.Data.model.UserDetailsResponse
import com.example.myapplication.myapplication.flashcall.Data.model.UsernameAvailabilityResponse
import com.example.myapplication.myapplication.flashcall.Data.model.ValidateRequest
import com.example.myapplication.myapplication.flashcall.Data.model.VerifyAadhaarOtpRequest
import com.example.myapplication.myapplication.flashcall.Data.model.VerifyOTPResponse
import com.example.myapplication.myapplication.flashcall.Data.model.VerifyPanRequest
import com.example.myapplication.myapplication.flashcall.Data.model.VerifyRequest
import com.example.myapplication.myapplication.flashcall.Data.model.aadharOtpResponse.AadharOtpResponse
import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.ValidateResponse
import com.example.myapplication.myapplication.flashcall.Data.model.deleteAdditionalLink.DeleteAdditionalLinks
import com.example.myapplication.myapplication.flashcall.Data.model.deleteAdditionalLink.DeletedAdditionalLinksResponse
import com.example.myapplication.myapplication.flashcall.Data.model.editAdditionalLink.EditAdditionalLinkRequest
import com.example.myapplication.myapplication.flashcall.Data.model.faceMatch.FaceMatchRequest
import com.example.myapplication.myapplication.flashcall.Data.model.faceMatch.FaceMatchResponse
import com.example.myapplication.myapplication.flashcall.Data.model.feedback.FeedbackResponse
import com.example.myapplication.myapplication.flashcall.Data.model.feedback.UpdateFeedback
import com.example.myapplication.myapplication.flashcall.Data.model.feedback.UpdateFeedbackResponse
import com.example.myapplication.myapplication.flashcall.Data.model.kycStatus.KycStatusResponse
import com.example.myapplication.myapplication.flashcall.Data.model.livelinessResponse.LivelinessResponse
import com.example.myapplication.myapplication.flashcall.Data.model.nameMatch.NameMatchResponse
import com.example.myapplication.myapplication.flashcall.Data.model.todaysWallet.TodaysWalletBalanceResponse
import com.example.myapplication.myapplication.flashcall.Data.model.wallet.TransactionsResponse
import com.example.myapplication.myapplication.flashcall.Data.model.wallet.UserId
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
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


    @PUT
    suspend fun editAdditionalLink(
        @Url url: String,
        @Body body: EditAdditionalLinkRequest
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


    @Multipart
    @POST("api/v1/userkyc/liveliness")
    suspend fun uploadLiveliness(
        @Part image: MultipartBody.Part, // Image file part
        @Part("verification_id") verificationId: RequestBody,
        @Part("userId") userId: RequestBody,
        @Part("img_url") imgUrl: RequestBody
    ): Response<LivelinessResponse>


    @POST
    suspend fun verifyPan(
        @Url url:String,
        @Body request: VerifyPanRequest
    ): retrofit2.Response<PanResponse>


    @POST
    suspend fun generateAadhaarOtp(
        @Url url:String,
        @Body request: AadhaarRequest
    ): retrofit2.Response<AadhaarResponse>


    @POST
    suspend fun verifyAadhaarOtp(
        @Url url:String,
        @Body request: VerifyAadhaarOtpRequest
    ): retrofit2.Response<AadharOtpResponse>


    @GET
    suspend fun getKycStatus(
        @Url url:String
    ): retrofit2.Response<KycStatusResponse>


    //"/api/v1/userKyc/name-match"
    @POST
    suspend fun nameMatch(
        @Url url:String,
        @Body request: NameMatchRequest
    ): retrofit2.Response<NameMatchResponse>


    @POST
    suspend fun faceMatch(
        @Url url: String,
        @Body request: FaceMatchRequest
    ): Response<FaceMatchResponse>

    @GET
    suspend fun todaysWalletBalance(
        @Url url: String
    ):Response < TodaysWalletBalanceResponse>

    @HTTP(method = "DELETE", path = "api/v1/creator/deleteLink", hasBody = true)
    suspend fun deleteAdditionalLink(
        @Body body: DeleteAdditionalLinks
    ):Response < DeletedAdditionalLinksResponse>



}