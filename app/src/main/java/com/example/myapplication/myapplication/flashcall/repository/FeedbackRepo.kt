package com.example.myapplication.myapplication.flashcall.repository

import android.util.Log
import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.ValidateResponse
import com.example.myapplication.myapplication.flashcall.Data.model.feedback.UpdateFeedback
import com.example.myapplication.myapplication.flashcall.Data.model.feedback.UpdateFeedbackResponse
import com.example.myapplication.myapplication.flashcall.Data.model.userFeedbacks.UserFeedbaks
import com.example.myapplication.myapplication.flashcall.Data.network.APIService
import com.example.myapplication.myapplication.flashcall.utils.SafeApiRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FeedbackRepo @Inject constructor(private val apiService: APIService) : IFeedbackRepo,SafeApiRequest() {

    override suspend fun getFeedbacks(url: String): Flow<UserFeedbaks> {

        return flow {
//            https://app.flashcall.me/api/v1/feedback/creator/selected?creatorId
            val response = apiService.getFeedbacks(url)
            emit(safeApiRequest {response})
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun updateFeedback(url: String, updateFeedback: UpdateFeedback): Flow<UpdateFeedbackResponse> {
        return flow {
            val response = apiService.updateFeedback("https://flashcall.me/api/v1/feedback/creator/setFeedback", updateFeedback)
            emit(safeApiRequest { response } )
        }.flowOn(Dispatchers.IO)
    }
}